package com.medallia.anywhere_mobile_sampleapp.utils

enum class ExternalError(val error: String) {

    SDK_IS_ALREADY_INITIALIZED("SDK is already initialized"),
    API_TOKEN_NOT_VALID("Api token is not valid"),
    ACCESS_TOKEN_PARSE_ERROR("Could not parse access token"),
    AUTHORIZATION_FAILED("Authorization failed"),
    CONFIGURATION_ERROR("Configuration error"),
    ACCESS_TOKEN_TTL_IS_PASSED("Access token ttl is passed"),
    SUBMIT_FEEDBACK_ERROR("Submit feedback error"),
    GET_UUID_ERROR("Error on get UUID")
}