package com.medallia.anywhere_versioning_sampleapp.ui.activities.forms

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.medallia.anywhere_versioning_sampleapp.R
import com.medallia.anywhere_versioning_sampleapp.model.Form
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


const val DELAY = 1500L

@AndroidEntryPoint
class FormActivity : AppCompatActivity() {

    private lateinit var currentForm: Form
    private val viewModel: FormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        getCurrentForm()
        initClickListeners()
    }

    private fun getCurrentForm() {
        val intent = intent
        if (intent.extras != null) {
            currentForm = intent.getSerializableExtra("formData") as Form
            toolbarFormTitle.text = currentForm.title
        }
    }

    private suspend fun onSuccessFeedbackSubmit() {
        Snackbar.make(
            findViewById(android.R.id.content),
            getString(R.string.submit_success),
            Snackbar.LENGTH_LONG
        ).show()
        Timber.i(getString(R.string.submit_success))

        delay(DELAY)
    }

    private fun initClickListeners() {
        submitFeedback.setOnClickListener {
            lifecycleScope.launch {
                val feedbackData = viewModel.submitFeedback(currentForm)
                feedbackData.let {
                    onSuccessFeedbackSubmit()
                    submitFeedback.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    feedbackDataPayload.text = gson.toJson(feedbackData)
                }
            }
        }
    }
}