package com.medallia.anywhere_versioning_sampleapp.model

data class FeedbackData(
    var formId: Int = 0,
    var dynamicData: FeedbackDynamicData?  = null,
    var formLanguage: String = ""
)




