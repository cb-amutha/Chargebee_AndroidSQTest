package com.chargebee.android.resources

import android.text.TextUtils
import com.chargebee.android.Chargebee
import com.chargebee.android.ErrorDetail
import com.chargebee.android.exceptions.CBException
import com.chargebee.android.exceptions.ChargebeeResult
import com.chargebee.android.loggers.CBLogger
import com.chargebee.android.network.Auth
import com.chargebee.android.network.CBAuthResponse
import com.chargebee.android.network.CBAuthentication
import com.chargebee.android.network.CBAuthenticationBody
import com.chargebee.android.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch

@RunWith(MockitoJUnitRunner::class)
class AuthResourceTest {

    val logger: CBLogger? = null
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
    @Test
    fun test_validateSdkKey_success(){

        val authentication = CBAuthentication("123","item","active","","","")
        val cbAuthResponse = CBAuthResponse(authentication)
        val queryParam = Chargebee.sdkKey
        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
        val lock = CountDownLatch(1)
        CBAuthentication.isSDKKeyValid(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(CBAuthResponse::class.java)
                    )
                }
                is ChargebeeResult.Error -> {
                    lock.countDown()
                    System.out.println("Error :"+it.exp.message)
                }
            }
        }
        lock.await()

        val body =CBAuthenticationBody(
            auth.sKey,
            auth.applicationId,
            auth.appName,
            auth.channel
        )
        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
                ChargebeeResult.Success(
                        cbAuthResponse
                )
            )
            Mockito.`when`(CBAuthenticationBody.fromCBAuthBody(auth)).thenReturn(
                body
            )
            verify(CBAuthenticationBody, times(1)).fromCBAuthBody(auth)
            verify(AuthResource(), times(1)).authenticate(auth)
        }
    }
    @Test
    fun test_Authenticate_success(){

        val authentication = CBAuthentication("123","item","active","","","")
        val cbAuthResponse = CBAuthResponse(authentication)

        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
        val lock = CountDownLatch(1)
        CBAuthentication.authenticate(auth) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(CBAuthResponse::class.java)
                    )
                }
                is ChargebeeResult.Error -> {
                    lock.countDown()
                    System.out.println("Error :"+it.exp.message)
                }
            }
        }
        lock.await()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
                ChargebeeResult.Success(
                    cbAuthResponse
                )
            )
            verify(AuthResource(), times(1)).authenticate(auth)
        }
    }
    @Test
    fun test_validateEmptySdkKey_success(){

        val authentication = CBAuthentication("123","item","active","","","")
        val cbAuthResponse = CBAuthResponse(authentication)

        Chargebee.sdkKey = ""
        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
        val lock = CountDownLatch(1)
        CBAuthentication.verifyAppDetails(auth,logger) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                            (it.data),
                            Matchers.instanceOf(CBAuthResponse::class.java)
                    )
                }
                is ChargebeeResult.Error -> {
                    lock.countDown()
                    System.out.println("Error :"+it.exp.message)
                }
            }
        }
        lock.await()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
                    ChargebeeResult.Success(
                            cbAuthResponse
                    )
            )
           verify(AuthResource(),

               times(1)).authenticate(auth)
            verify(CBAuthentication, times(1)).verifyAppDetails(auth,logger){
                ChargebeeResult.Error(
                    exp = CBException(
                        error = ErrorDetail(message = "SDK key is empty", apiErrorCode = "400")
                    ))
            }
        }
    }
    @Test
    fun test_validateEmptyApplicationId_success(){

        val authentication = CBAuthentication("123","item","active","","","")
        val cbAuthResponse = CBAuthResponse(authentication)

        Chargebee.applicationId = ""
        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
        val lock = CountDownLatch(1)
        CBAuthentication.verifyAppDetails(auth,logger = null) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                            (it.data),
                            Matchers.instanceOf(CBAuthResponse::class.java)
                    )
                }
                is ChargebeeResult.Error -> {
                    lock.countDown()
                    System.out.println("Error :"+it.exp.message)
                }
            }
        }
        lock.await()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
                    ChargebeeResult.Success(
                            cbAuthResponse
                    )
            )
            verify(AuthResource(), times(1)).authenticate(auth)
            verify(CBAuthentication, times(1)).verifyAppDetails(auth,logger){
                ChargebeeResult.Error(
                    exp = CBException(
                        error = ErrorDetail(message = "Application ID is empty", apiErrorCode = "400")
                    ))
            }
        }
    }
    @Test
    fun test_validateEmptyAppName_success(){

        val authentication = CBAuthentication("123","item","active","","","")
        val cbAuthResponse = CBAuthResponse(authentication)
        val queryParam = ""
        Chargebee.appName = ""
        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
        val lock = CountDownLatch(1)
        CBAuthentication.verifyAppDetails(auth,logger = null) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                            (it.data),
                            Matchers.instanceOf(CBAuthResponse::class.java)
                    )
                }
                is ChargebeeResult.Error -> {
                    lock.countDown()
                    System.out.println("Error :"+it.exp.message)
                }
            }
        }
        lock.await()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
                    ChargebeeResult.Success(
                            cbAuthResponse
                    )
            )
            verify(AuthResource(), Mockito.times(1)).authenticate(auth)
            verify(CBAuthentication, times(1)).verifyAppDetails(auth,logger){
                ChargebeeResult.Error(
                    exp = CBException(
                        error = ErrorDetail(message = "App Name is empty", apiErrorCode = "400")
                    ))
            }
        }
    }
    @Test
    fun test_validateSdkKey_error(){
        val exception = CBException(ErrorDetail("Error"))
        val queryParam = Chargebee.sdkKey
        val auth = Auth(Chargebee.sdkKey, Chargebee.applicationId, Chargebee.appName, Chargebee.channel)
        val lock = CountDownLatch(1)
        CBAuthentication.isSDKKeyValid(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(CBAuthResponse::class.java)
                    )
                }
                is ChargebeeResult.Error -> {
                    lock.countDown()
                    System.out.println("Error :"+it.exp.message)
                }
            }
        }
        lock.await()

        CoroutineScope(Dispatchers.IO).launch {
            Mockito.`when`(AuthResource().authenticate(auth)).thenReturn(
                ChargebeeResult.Error(
                    exception
                )
            )
            verify(AuthResource(), times(1)).authenticate(auth)
        }
    }
}