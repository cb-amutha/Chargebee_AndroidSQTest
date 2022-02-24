package com.chargebee.android;

interface ChargebeeError {
    fun toCBError(statusCode: Int): ErrorDetail
}

data class ErrorDetail(
    val message: String?,
    val type: String? = null,
    val apiErrorCode: String? = null,
    val param: String? = null,
    val httpStatusCode: Int? = null
) : ChargebeeError {
    override fun toCBError(statusCode: Int): ErrorDetail {
        return this
    }
}

internal data class InternalErrorWrapper(val errors: Array<InternalErrorDetail>): ChargebeeError {
    override fun toCBError(statusCode: Int): ErrorDetail {
        val message = errors.getOrNull(0)?.message ?: ""
        return ErrorDetail(message, httpStatusCode=statusCode)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InternalErrorWrapper

        if (!errors.contentEquals(other.errors)) return false

        return true
    }

    override fun hashCode(): Int {
        return errors.contentHashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}

internal data class InternalErrorDetail(val message: String)
