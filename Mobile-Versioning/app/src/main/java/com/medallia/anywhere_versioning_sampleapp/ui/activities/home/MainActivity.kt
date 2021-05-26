package com.medallia.anywhere_versioning_sampleapp.ui.activities.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medallia.anywhere_versioning_sampleapp.R
import com.medallia.anywhere_versioning_sampleapp.database.Storage
import com.medallia.anywhere_versioning_sampleapp.model.Form
import com.medallia.anywhere_versioning_sampleapp.ui.activities.forms.FormActivity
import com.medallia.anywhere_versioning_sampleapp.ui.activities.forms.FormAdapter
import com.medallia.anywhere_versioning_sampleapp.utils.DataState
import com.medallia.anywhere_versioning_sampleapp.utils.ExternalError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FormAdapter.OnFormClickListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var formAdapter: FormAdapter
    private lateinit var formData: Form

    @Inject
    lateinit var storage: Storage
    private val handlerException = CoroutineExceptionHandler { _, exception ->
        Timber.e("%s %s",ExternalError.SDK_INITIALIZE_ERROR,
            exception.message.toString())
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        formAdapter = FormAdapter(this@MainActivity)
        recyclerView.adapter = formAdapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        recyclerView.setHasFixedSize(true)

        lifecycleScope.launch(handlerException) {
            viewModel.init(storage.getString(Storage.API_TOKEN).first())
            subscribeObservers()
        }
    }


    private suspend fun subscribeObservers() {
        viewModel.getForms().observe(this, Observer { dataState ->
            when (dataState) {

                is DataState.Success<List<Form>> -> {
                    displayProgressBar(false)
                    initRecycleView(dataState.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        message?.let {
            showToast(it)
            Timber.e(it)
        } ?: run {
            showToast("Unknown Error")
        }


    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        jobProgressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }


    private fun showToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }


    private fun initRecycleView(formList: List<Form>?) {

        formList?.let {
            if (it.isEmpty()) {
                recyclerView.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
            } else {
                formAdapter.setForms(it)
                recyclerView.visibility = View.VISIBLE
            }
        }

    }

    override fun onFormClicked(form: Form) {
        formData = form

        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra("formData", formData)
        startActivity(intent)
    }
}