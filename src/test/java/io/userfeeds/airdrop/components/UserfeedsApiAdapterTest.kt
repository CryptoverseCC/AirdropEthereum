package io.userfeeds.airdrop.components

import org.junit.Ignore
import org.junit.Test

@Ignore
class UserfeedsApiAdapterTest {

    private val asset = "ethereum:0x06012c8cf97bead5deae237070f9587f8e7a266d"
    private val airdropClaimId = "claim:0x8c45121c6ab22c5d25a719679f2444151b0708eba9bebd876d4a7f8a9bf66004:0"
    private val userfeedsApiAdapter: UserfeedsApiAdapter = UserfeedsApiAdapter(asset, airdropClaimId)

    @Test
    fun shouldFetchAddressesFromApi() {
        userfeedsApiAdapter.getOwners(1523096603927L).forEach { println(it) }
    }

    @Test
    fun shouldFetchAllAddressesFromApi() {
        userfeedsApiAdapter.getOwners(null).forEach { println(it) }
    }

    @Test
    fun shouldFetchAlreadyProcessedAddresses() {
        userfeedsApiAdapter.getProcessedOwners().forEach { println(it) }
    }
}