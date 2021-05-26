package com.medallia.anywhere_versioning_sampleapp.model

data class FeedbackDynamicData(
    var pages: List<FeedbackPage>? = null,
    var customParams: List<CustomParam>? = null
)