package com.chargebee.android.exceptions

import java.util.ArrayList

sealed class CBProductIDResult<out S> {
    data class ProductIds(val productIdList: ArrayList<String>) : CBProductIDResult<ArrayList<String>>()
    data class Error(val exp: CBException): CBProductIDResult<Nothing>()
}
