package com.chargebee.android.resources

import com.chargebee.android.CBResult
import com.chargebee.android.Chargebee
import com.chargebee.android.ErrorDetail
import com.chargebee.android.billingservice.CBCallback
import com.chargebee.android.exceptions.CBException
import com.chargebee.android.exceptions.ChargebeeResult
import com.chargebee.android.models.*
import com.chargebee.android.repository.ItemsRepository
import com.chargebee.android.responseFromServer
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
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch

@RunWith(MockitoJUnitRunner::class)
class ItemResourceTest {

    @Mock
    lateinit var repo : ItemsRepository

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
    fun test_retrieveItemsList_success(){

        val item = Items("123","item","active","play_store")
        val itemWrapper = ItemWrapper(item)

        val list = ArrayList<ItemWrapper>()
        list.add(itemWrapper)
        val itemsWrapper = ItemsWrapper(list)

        val queryParam = arrayOf("8", "Standard", Chargebee.channel)
        val lock = CountDownLatch(1)
        Items.retrieveAllItems(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(PlansWrapper::class.java)
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
            Mockito.`when`(ItemsResource().retrieveAllItems(queryParam)).thenReturn(
                ChargebeeResult.Success(
                        itemsWrapper
                )
            )
            Mockito.verify(ItemsResource(), Mockito.times(1)).retrieveAllItems(queryParam)
        }
    }
    @Test
    fun test_retrieveItemsList_error(){
        val exception = CBException(ErrorDetail("Error"))
        val queryParam = arrayOf("Standard", "play_store")
        val lock = CountDownLatch(1)
        Items.retrieveAllItems(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(PlansWrapper::class.java)
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
            Mockito.`when`(ItemsResource().retrieveAllItems(queryParam)).thenReturn(
                ChargebeeResult.Error(
                    exception
                )
            )
            Mockito.verify(ItemsResource(), Mockito.times(1)).retrieveAllItems(queryParam)
        }
    }
    @Test
    fun test_retrieveItemsParamEmpty_error(){
        val queryParam = arrayOf("null")
        val lock = CountDownLatch(1)
        Items.retrieveAllItems(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                            (it.data),
                            Matchers.instanceOf(PlansWrapper::class.java)
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
            Mockito.`when`(repo.retrieveAllItems("","","","","","",""))
            Mockito.verify(repo, Mockito.times(1)).retrieveAllItems("","","","","","","")
        }
    }
    @Test
    fun test_retrieveItem_success(){

        val items = Items("","","","")
        val itemWrapper = ItemWrapper(items)

        val queryParam = "Standard"
        val lock = CountDownLatch(1)
        Items.retrieveItem(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(PlansWrapper::class.java)
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
            Mockito.`when`(ItemsResource().retrieveItem(queryParam)).thenReturn(
                ChargebeeResult.Success(
                        itemWrapper
                )
            )
            Mockito.verify(ItemsResource(), Mockito.times(1)).retrieveItem(queryParam)
        }
    }
    @Test
    fun test_retrieveItem_error(){
        val exception = CBException(ErrorDetail("Error"))
        val queryParam = "Standard"
        val lock = CountDownLatch(1)
        Items.retrieveItem(queryParam) {
            when (it) {
                is ChargebeeResult.Success -> {
                    lock.countDown()
                    System.out.println("List plans :"+it.data)
                    MatcherAssert.assertThat(
                        (it.data),
                        Matchers.instanceOf(PlansWrapper::class.java)
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
            Mockito.`when`(ItemsResource().retrieveItem(queryParam)).thenReturn(
                ChargebeeResult.Error(
                    exception
                )
            )
            Mockito.verify(ItemsResource(), Mockito.times(1)).retrieveItem(queryParam)
        }
    }

}
