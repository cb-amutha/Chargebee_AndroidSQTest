package com.chargebee.android.resources

import com.android.billingclient.api.SkuDetails
import com.chargebee.android.Chargebee
import com.chargebee.android.ErrorDetail
import com.chargebee.android.billingservice.CBCallback
import com.chargebee.android.billingservice.CBPurchase
import com.chargebee.android.billingservice.PurchaseModel
import com.chargebee.android.exceptions.CBException
import com.chargebee.android.exceptions.ChargebeeResult
import com.chargebee.android.models.Products
import com.chargebee.android.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch

@RunWith(MockitoJUnitRunner::class)
class PurchaseProductTest {

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        Chargebee.configure(
                site = "cb-imay-test",
                publishableApiKey = "test_EojsGoGFeHoc3VpGPQDOZGAxYy3d0FF3",
                sdkKey = "cb-x2wiixyjr5bl5ihugstyp2exbi"
        )
    }
    @After
    fun tearDown(){

    }

//    @Test
//    fun test_purchaseProduct_success(){
//        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
//        val skuDetails = SkuDetails(jsonDetails)
//        val products = Products("merchant.pro.android","","",skuDetails,true)
//        val lock = CountDownLatch(1)
//        CBPurchase.purchaseProduct(products, object : CBCallback.PurchaseCallback<PurchaseModel>{
//            override fun onSuccess(success: PurchaseModel) {
//                lock.countDown()
//                assertThat(success,instanceOf(PurchaseModel::class.java))
//            }
//            override fun onError(error: CBException) {
//                lock.countDown()
//                println(" Error :  ${error.message}")
//            }
//        })
//        lock.await()
//        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
//        val authentication = CBAuthentication("123","item","active","","","")
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
//                    ChargebeeResult.Success(
//                            authentication
//                    )
//            )
//            Mockito.verify(AuthResource(), Mockito.times(1)).authenticate(auth)
//        }
//    }
//    @Test
//    fun test_purchaseProduct_error(){
//        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
//        val skuDetails = SkuDetails(jsonDetails)
//        val products = Products("","","",skuDetails,true)
//        val lock = CountDownLatch(1)
//        CBPurchase.purchaseProduct(products, object : CBCallback.PurchaseCallback<PurchaseModel>{
//            override fun onSuccess(success: PurchaseModel) {
//                lock.countDown()
//                assertThat(success,instanceOf(PurchaseModel::class.java))
//            }
//            override fun onError(error: CBException) {
//                lock.countDown()
//                println(" Error :  ${error.message}")
//            }
//        })
//        lock.await()
//
//        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
//        val exception = CBException(ErrorDetail("Error"))
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
//                    ChargebeeResult.Error(
//                            exception
//                    )
//            )
//            Mockito.verify(AuthResource(), Mockito.times(1)).authenticate(auth)
//        }
//    }
    @Test
    fun test_validateReceipt_success(){
        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
        val skuDetails = SkuDetails("{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}")
        val products = Products("merchant.pro.android","","",skuDetails,true)
        val lock = CountDownLatch(1)
        CBPurchase.purchaseProduct(products, object : CBCallback.PurchaseCallback<PurchaseModel>{
            override fun onSuccess(success: PurchaseModel) {
                lock.countDown()
                assertThat(success,instanceOf(PurchaseModel::class.java))
            }
            override fun onError(error: CBException) {
                lock.countDown()
                println(" Error :  ${error.message}")
            }
        })
        lock.await()

        val params = Params(
                "purchaseToken",
                products.productId,
                Chargebee.site,
                Chargebee.channel
        )

        val response = ReceiptDetail("","","")
        CoroutineScope(Dispatchers.IO).launch {

            Mockito.`when`(ReceiptResource().validateReceipt(params)).thenReturn(
                    ChargebeeResult.Success(
                            response
                    )
            )
            Mockito.verify(ReceiptResource(), Mockito.times(1)).validateReceipt(params)
        }
    }
    @Test
    fun test_validateReceipt_error(){
        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
        val skuDetails = SkuDetails(jsonDetails)
        val products = Products("","","",skuDetails,true)
        val lock = CountDownLatch(1)
        val params = Params(
                "purchaseToken",
                products.productId,
                Chargebee.site,
                Chargebee.channel
        )
        CBPurchase.purchaseProduct(products, object : CBCallback.PurchaseCallback<PurchaseModel>{
            override fun onSuccess(success: PurchaseModel) {
                lock.countDown()
                assertThat(success,instanceOf(PurchaseModel::class.java))
            }
            override fun onError(error: CBException) {
                lock.countDown()
                println(" Error :  ${error.message}")
            }
        })
        lock.await()
        val exception = CBException(ErrorDetail("Error"))
        CoroutineScope(Dispatchers.IO).launch {

            Mockito.`when`(ReceiptResource().validateReceipt(params)).thenReturn(
                    ChargebeeResult.Error(
                            exception
                    )
            )
            Mockito.verify(ReceiptResource(), Mockito.times(1)).validateReceipt(params)
        }
    }
}