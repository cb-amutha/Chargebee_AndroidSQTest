package com.chargebee.android.resources

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.android.billingclient.api.*
import com.chargebee.android.Chargebee
import com.chargebee.android.ErrorDetail
import com.chargebee.android.billingservice.BillingClientManager
import com.chargebee.android.billingservice.CBCallback
import com.chargebee.android.billingservice.CBCallback.ListProductsCallback
import com.chargebee.android.billingservice.CBPurchase
import com.chargebee.android.billingservice.PurchaseModel
import com.chargebee.android.exceptions.CBException
import com.chargebee.android.exceptions.CBProductIDResult
import com.chargebee.android.exceptions.ChargebeeResult
import com.chargebee.android.models.Products
import com.chargebee.android.network.CBReceiptRequestBody
import com.chargebee.android.network.CBReceiptResponse
import com.chargebee.android.network.Params
import com.chargebee.android.network.ReceiptDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
class BillingClientManagerTest  {

    var billingClient: BillingClientManager? = null
    @Mock
    lateinit var billingClient1: BillingClient

    var mContext: Context? = null
    private var callBack : ListProductsCallback<ArrayList<Products>>? = null
    private var callBackPurchase : CBCallback.PurchaseCallback<PurchaseModel>? = null
    val productIdList = arrayListOf("merchant.pro.android", "merchant.premium.android")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mContext = ApplicationProvider.getApplicationContext()

        Chargebee.configure(site = "cb-imay-test", publishableApiKey = "test_AVrzSIux7PHMmiMdi7ixAiqoVYE9jHbz", sdkKey = "cb-x2wiixyjr5bl5ihugstyp2exbi") // For play store

        billingClient = callBack?.let {
            BillingClientManager(
                    ApplicationProvider.getApplicationContext(),
                    BillingClient.SkuType.SUBS,
                    productIdList, it
            )
        }
    }
    @After
    fun tearDown(){
        mContext = null
        billingClient = null
    }

    @Test
    fun test_loadProducts_success(){

        billingClient?.startBillingServiceConnection()
        billingClient?.queryAllPurchases()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(billingClient1.queryPurchasesAsync(BillingClient.SkuType.SUBS))
            Mockito.verify(billingClient1, times(1))
                ?.queryPurchasesAsync(BillingClient.SkuType.SUBS)
        }
    }

    @Test
    fun test_billingClient_ready(){

        billingClient?.isBillingClientReady()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(billingClient1.isReady()).thenReturn(true)
            Mockito.verify(billingClient1, times(1))
                ?.isReady()
        }
    }
    @Test
    fun test_featureSupported(){

        billingClient?.isFeatureSupported()

        val response = BillingResult()
        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(billingClient1.isFeatureSupported("subscriptions")).thenReturn(response)
            Mockito.verify(billingClient1, times(1)).isFeatureSupported("subscriptions")
        }
    }

//    @Test
//    fun test_retrieveProducts_success(){
//        val productIdList = arrayListOf("merchant.pro.android", "merchant.premium.android")
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val skuType = CBPurchase.SkuType.SUBS
//            Mockito.`when`(mContext?.let {
//                CBPurchase.retrieveProducts(
//                    it,
//                    productIdList,
//                    object : ListProductsCallback<ArrayList<Products>> {
//                        override fun onSuccess(productDetails: ArrayList<Products>) {
//                            println("List products :$productDetails")
//                            assertThat(
//                                productDetails,
//                                instanceOf(Products::class.java)
//                            )
//                        }
//
//                        override fun onError(error: CBException) {
//                            println("Error in retrieving all items :${error.message}")
//                        }
//                    })
//            }).thenReturn(Unit)
//        }
//    }
//
//    @Test
//    fun test_retrieveProducts_error(){
//        val productIdList = arrayListOf("")
//
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(mContext?.let {
//                CBPurchase.retrieveProducts(
//                    it,
//                    productIdList,
//                    object : ListProductsCallback<ArrayList<Products>> {
//                        override fun onSuccess(productDetails: ArrayList<Products>) {
//                            println("List products :$productDetails")
//                            assertThat(
//                                productDetails,
//                                instanceOf(Products::class.java)
//                            )
//                        }
//
//                        override fun onError(error: CBException) {
//                            println("Error in retrieving all items :${error.message}")
//                        }
//                    })
//            }).thenReturn(Unit)
//        }
//    }
//
//    @Test
//    fun test_retrieveProductIds_success(){
//        val queryParam = arrayOf("100")
//
//        val IDs =  java.util.ArrayList<String>()
//        IDs.add("")
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(CBPurchase.retrieveProductIDs(queryParam) {
//                when (it) {
//                    is CBProductIDResult.ProductIds -> {
//                        assertThat(it,instanceOf(CBProductIDResult::class.java))
//                    }
//                    is CBProductIDResult.Error -> {
//                        println(" Error ${it.exp.message}")
//                    }
//                }
//            }).thenReturn(Unit)
//
//        }
//    }
//    @Test
//    fun test_retrieveProductIdsList_success(){
//        val queryParam = arrayOf("100")
//
//        Chargebee.version = CatalogVersion.V2.value
//        val IDs =  java.util.ArrayList<String>()
//        IDs.add("")
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(CBPurchase.retrieveProductIDList(queryParam) {
//                when (it) {
//                    is CBProductIDResult.ProductIds -> {
//                        assertThat(it,instanceOf(CBProductIDResult::class.java))
//                    }
//                    is CBProductIDResult.Error -> {
//                        println(" Error ${it.exp.message}")
//                    }
//                }
//            }).thenReturn(Unit)
//
//        }
//    }
//    @Test
//    fun test_retrieveProductIdsListV1_success(){
//        val queryParam = arrayOf("100")
//
//        Chargebee.version = CatalogVersion.V1.value
//        val IDs =  java.util.ArrayList<String>()
//        IDs.add("")
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(CBPurchase.retrieveProductIDList(queryParam) {
//                when (it) {
//                    is CBProductIDResult.ProductIds -> {
//                        assertThat(it,instanceOf(CBProductIDResult::class.java))
//                    }
//                    is CBProductIDResult.Error -> {
//                        println(" Error ${it.exp.message}")
//                    }
//                }
//            }).thenReturn(Unit)
//
//        }
//    }
//    @Test
//    fun test_retrieveProductIdsListUnknown_success(){
//        val queryParam = arrayOf("100")
//
//        Chargebee.version = CatalogVersion.Unknown.value
//        val IDs =  java.util.ArrayList<String>()
//        IDs.add("")
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(CBPurchase.retrieveProductIDList(queryParam) {
//                when (it) {
//                    is CBProductIDResult.ProductIds -> {
//                        assertThat(it,instanceOf(CBProductIDResult::class.java))
//                    }
//                    is CBProductIDResult.Error -> {
//                        println(" Error ${it.exp.message}")
//                    }
//                }
//            }).thenReturn(Unit)
//
//        }
//    }
//
//    @Test
//    fun test_retrieveProductIds_error(){
//        val queryParam = arrayOf("0")
//
//        val IDs =  java.util.ArrayList<String>()
//        IDs.add("")
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(CBPurchase.retrieveProductIDs(queryParam) {
//                when (it) {
//                    is CBProductIDResult.ProductIds -> {
//                        assertThat(it,instanceOf(CBProductIDResult::class.java))
//                    }
//                    is CBProductIDResult.Error -> {
//                        println(" Error ${it.exp.message}")
//                    }
//                }
//            }).thenReturn(Unit)
//
//        }
//    }
//    @Test
//    fun test_purchaseProduct_success(){
//        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
//        val skuDetails = SkuDetails(jsonDetails)
//        val products = Products("","","",skuDetails,true)
//         val lock = CountDownLatch(1)
//        CoroutineScope(Dispatchers.IO).launch {
//            CBPurchase.purchaseProduct(
//                products,
//                object : CBCallback.PurchaseCallback<PurchaseModel> {
//                    override fun onSuccess(success: PurchaseModel) {
//                        lock.countDown()
//                        assertThat(success, instanceOf(PurchaseModel::class.java))
//                    }
//                    override fun onError(error: CBException) {
//                        lock.countDown()
//                        println(" Error :  ${error.message}")
//                    }
//                })
//
//        }
//        lock.await()
//
//
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(callBackPurchase?.let { billingClient?.purchase(products, it) }).thenReturn(Unit)
//            callBackPurchase?.let { verify(billingClient, times(1))?.purchase(products,purchaseCallBack = it) }
//        }
//
//    }
//    @Test
//    fun test_purchaseProduct_error(){
//        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
//        val skuDetails = SkuDetails(jsonDetails)
//        val products = Products("","","",skuDetails,true)
//       // val lock = CountDownLatch(1)
//        CoroutineScope(Dispatchers.IO).launch {
//            CBPurchase.purchaseProduct(
//                products,
//                object : CBCallback.PurchaseCallback<PurchaseModel> {
//                    override fun onSuccess(success: PurchaseModel) {
//                        //lock.countDown()
//                        assertThat(success, instanceOf(PurchaseModel::class.java))
//                    }
//
//                    override fun onError(error: CBException) {
//                        //lock.countDown()
//                        println(" Error :  ${error.message}")
//                    }
//                })
//        }
//       // lock.await()
//    }
//    @Test
//    fun test_validateReceipt_success(){
//        val purchaseToken = "56sadmnagdjsd"
//        val jsonDetails = "{\"productId\":\"merchant.premium.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
//        val skuDetails = SkuDetails(jsonDetails)
//        val products = Products("merchant.premium.android","Premium Plan (Chargebee Example)","₹2,650.00",skuDetails,true)
//        val lock = CountDownLatch(1)
//        CoroutineScope(Dispatchers.IO).launch {
//            CBPurchase.validateReceipt(purchaseToken, products) {
//                when (it) {
//                    is ChargebeeResult.Success -> {
//                        lock.countDown()
//                        assertThat(it, instanceOf(CBReceiptResponse::class.java))
//                    }
//                    is ChargebeeResult.Error -> {
//                        lock.countDown()
//                        println(" Error :  ${it.exp.message}")
//                    }
//                }
//            }
//        }
//        lock.await()
//
//        val params = Params(
//            purchaseToken,
//            products.productId,
//            Chargebee.site,
//            Chargebee.channel
//        )
//        val receiptDetail = ReceiptDetail("subscriptionId","customerId","planId")
//        val response = CBReceiptResponse(receiptDetail)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(ReceiptResource().validateReceipt(params)).thenReturn(
//                ChargebeeResult.Success(
//                    receiptDetail
//                )
//            )
//            Mockito.verify(ReceiptResource(), times(1)).validateReceipt(params)
//            Mockito.verify(CBReceiptRequestBody("receipt","","",""), times(1)).toCBReceiptReqBody()
//        }
//    }
//    @Test
//    fun test_validateReceipt_error(){
//        val purchaseToken = "56sadmnagdjsd"
//        val jsonDetails = "{\"productId\":\"merchant.premium.test.android\",\"type\":\"subs\",\"title\":\"Premium Plan (Chargebee Example)\",\"name\":\"Premium Plan\",\"price\":\"₹2,650.00\",\"price_amount_micros\":2650000000,\"price_currency_code\":\"INR\",\"description\":\"Every 6 Months\",\"subscriptionPeriod\":\"P6M\",\"skuDetailsToken\":\"AEuhp4J0KiD1Bsj3Yq2mHPBRNHUBdzs4nTJY3PWRR8neE-22MJNssuDzH2VLFKv35Ov8\"}"
//        val skuDetails = SkuDetails(jsonDetails)
//        val products = Products("merchant.premium.test.android","Premium Plan (Chargebee Example)","₹2,650.00",skuDetails,true)
//       // val lock = CountDownLatch(1)
//        CoroutineScope(Dispatchers.IO).launch {
//            CBPurchase.validateReceipt(purchaseToken, products) {
//                when (it) {
//                    is ChargebeeResult.Success -> {
//                       // lock.countDown()
//                        assertThat(it, instanceOf(CBReceiptResponse::class.java))
//                    }
//                    is ChargebeeResult.Error -> {
//                       // lock.countDown()
//                        println(" Error :  ${it.exp.message}")
//                    }
//                }
//            }
//        }
//       // lock.await()
//
//        val params = Params(
//            purchaseToken,
//            products.productId,
//            CBPurchase.price,
//            Chargebee.channel
//        )
//        val exception = CBException(ErrorDetail("Error"))
//        CoroutineScope(Dispatchers.IO).launch {
//            Mockito.`when`(ReceiptResource().validateReceipt(params)).thenReturn(
//                ChargebeeResult.Error(
//                    exception
//                )
//            )
//            Mockito.verify(ReceiptResource(), times(1)).validateReceipt(params)
//            Mockito.verify(CBReceiptRequestBody("receipt","","",""), times(1)).toCBReceiptReqBody()
//        }
//    }
}