package com.chargebee.example

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chargebee.android.billingservice.CBCallback
import com.chargebee.android.billingservice.CBPurchase
import com.chargebee.android.exceptions.CBException
import com.chargebee.android.exceptions.CBProductIDResult
import com.chargebee.android.models.Products

class MainViewModel : ViewModel() {

    var mResult: MutableLiveData<Array<String>> = MutableLiveData()
    var mProductResult: MutableLiveData<ArrayList<Products>> = MutableLiveData()
    var mError: MutableLiveData<String> = MutableLiveData()

    fun retrieveProductIdList(){
        val queryParam = arrayOf("100")
        CBPurchase.retrieveProductIDs(queryParam) {
            when (it) {
                is CBProductIDResult.ProductIds -> {
                    val array = it.productIdList.toTypedArray()
                    mResult.postValue(array)
                }
                is CBProductIDResult.Error -> {
                    Log.e(javaClass.simpleName, " ${it.exp.message}")
                    val empty = arrayOf("Product IDs not found on this site for play store")
                    mResult.postValue(empty)

                }
            }
        }
    }
    fun retrieveProducts(productIdList: ArrayList<String>, context: Context){
        CBPurchase.retrieveProducts(
                context,
                productIdList,
                object : CBCallback.ListProductsCallback<ArrayList<Products>> {
                    override fun onSuccess(productDetails: ArrayList<Products>) {
                        mProductResult.postValue(productDetails)
                    }
                    override fun onError(error: CBException) {
                        Log.e(javaClass.simpleName, "Error:  ${error.message}")
                        mError.postValue(error.message)
                    }
                })
    }
}