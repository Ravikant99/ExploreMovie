package com.ravi.exploremovie.webServices

sealed class ApiError {

    open class ApiFailedResponse(
        val code: Int = -1,
        val errorCode: String = "",
        val message: String = ""
    ) : ApiError()

    data class ExceptionError (
        val message: String
    ): ApiError()

    data class UnknownError (
        val code: Int = -1,
        val message: String = "Unknown error"
    ) : ApiError()

    data class SignUpApiFailedResponse(
        val conflicts: List<ConflictField> = emptyList()
    ) : ApiFailedResponse()

    class ConflictField(
        val field: String = "",
        val value: String = "",
        val errorCode: String = ""
    )
}