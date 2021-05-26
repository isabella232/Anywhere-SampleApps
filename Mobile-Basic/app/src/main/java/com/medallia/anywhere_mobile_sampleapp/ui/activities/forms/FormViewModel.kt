package com.medallia.anywhere_mobile_sampleapp.ui.activities.forms

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.medallia.anywhere_mobile_sampleapp.model.FeedbackData
import com.medallia.anywhere_mobile_sampleapp.model.Form
import com.medallia.anywhere_mobile_sampleapp.repository.SubmitFeedbackRepository

class FormViewModel @ViewModelInject constructor(private var submitFeedbackRepository: SubmitFeedbackRepository): ViewModel() {

    suspend fun submitFeedback(form: Form) : FeedbackData {
        return submitFeedbackRepository.submitFeedBack(form)
    }
}