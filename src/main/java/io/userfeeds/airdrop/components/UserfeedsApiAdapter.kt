package io.userfeeds.airdrop.components

import io.reactivex.Single
import io.userfeeds.airdrop.collecting.AddressCollecting
import io.userfeeds.airdrop.dto.Owner
import io.userfeeds.airdrop.update.AlreadyProcessed
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
class UserfeedsApiAdapter(
        @Value("\${MONITORED_ERC721}") private val asset: String,
        @Value("\${AIRDROP_CLAIM_ID}") private val airdropClaimId: String
) : AddressCollecting.NewAddressProvider, AlreadyProcessed.ProcessedAddressProvider {

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
            .create(UserfeedsApi::class.java)


    override fun getOwners(since: Long?): List<Owner> {
        return if (since != null) {
            ownersSince(since)
        } else {
            allOwners()
        }
    }

    private fun ownersSince(since: Long): List<Owner> {
        return newRequest(
                algorithm = "experimental_receivers",
                params = mapOf(
                        "timestamp" to since,
                        "asset" to asset
                )
        ).execute()
    }

    private fun allOwners(): List<Owner> {
        return newRequest(
                algorithm = "experimental_all_receivers",
                params = mapOf("asset" to asset)
        ).execute()
    }

    override fun getProcessedOwners(): List<Owner> {
        return newRequest(
                algorithm = "experimental_airdrop_receivers",
                params = mapOf("id" to airdropClaimId)
        ).execute()
    }

    private fun newRequest(algorithm: String, params: Map<String, Any>): UserfeedsApi.Request {
        return UserfeedsApi.Request(flow = listOf(UserfeedsApi.Algorithm(algorithm = algorithm, params = params)))
    }

    private fun UserfeedsApi.Request.execute(): List<Owner> {
        return api.receivers(this).blockingGet().items
    }
}

interface UserfeedsApi {

    @POST("./")
    fun receivers(@Body request: Request): Single<Response>

    data class Request(val flow: List<Algorithm>)

    data class Algorithm(val algorithm: String, val params: Map<String, Any>)

    data class Response(val items: List<Owner>)
}
