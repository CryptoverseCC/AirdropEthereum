package io.userfeeds.airdrop.components

import org.junit.Ignore
import org.junit.Test

@Ignore
class HttpNewAddressProviderTest {

    private val asset = "ethereum:0x06012c8cf97bead5deae237070f9587f8e7a266d"
    private val httpNewAddressProvider: HttpNewAddressProvider = HttpNewAddressProvider(asset)

    @Test
    fun shouldFetchAddressesFromApi() {
        httpNewAddressProvider.getOwners(1523096603927L).forEach { println(it) }
    }

    @Test
    fun shouldFetchAllAddressesFromApi() {
        httpNewAddressProvider.getOwners(null).forEach { println(it) }
    }
}