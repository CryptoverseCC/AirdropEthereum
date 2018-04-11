package io.userfeeds.airdrop.components

import io.reactivex.Single
import io.userfeeds.airdrop.collecting.AddressCollecting
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

@Component
class HttpNewAddressProvider(
        @Value("\${MONITORED_ERC721}") private val asset:String
) : AddressCollecting.NewAddressProvider {

    private val baseUrl = "https://api-staging.userfeeds.io/ranking/"
    private val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    private val api = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NewAddressProviderApi::class.java)


    override fun getOwners(since: Long): List<AddressCollecting.Owner> {
        return api.receivers(
                NewAddressProviderApi.Request(flow = listOf(
                        NewAddressProviderApi.Algorithm(algorithm = "experimental_receivers", params = mapOf(
                                "timestamp" to since,
                                "asset" to asset
                        ))
                ))).blockingGet().items
    }
}

interface NewAddressProviderApi {

    @POST("./")
    fun receivers(@Body request: Request): Single<Response>

    data class Request(val flow: List<Algorithm>)

    data class Algorithm(val algorithm: String, val params: Map<String, Any>)

    data class Response(val items: List<AddressCollecting.Owner>)
}
