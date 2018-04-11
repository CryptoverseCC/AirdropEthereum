package io.userfeeds.airdrop

import com.nhaarman.mockito_kotlin.*
import io.userfeeds.airdrop.collecting.AddressCollecting
import io.userfeeds.airdrop.collecting.AddressCollectingService
import org.junit.Test
import java.util.*

class AddressCollectingServiceTest {

    private val mongo = mock<AddressCollecting.AddressStore>()
    private val neo = mock<AddressCollecting.NewAddressProvider>()
    private val service = AddressCollectingService(mongo, neo, skipAddresses = false)

    @Test
    fun shouldSaveNewOwners() {
        val owners = listOf(newOwner(1))
        setup(newOwnersFromNeo = owners, mongoLastTimeStamp = null)
        service.updateOwnerList()
        verify(mongo).saveTime(1L)
        verify(mongo).saveOwners(owners, false)
    }

    @Test
    fun shouldAskOnlyForNewOwners() {
        val lastTimestamp = 2L
        setup(newOwnersFromNeo = emptyList(), mongoLastTimeStamp = lastTimestamp)
        service.updateOwnerList()
        verify(neo).getOwners(since = lastTimestamp)
    }

    @Test
    fun shouldNotSaveOlderTime() {
        setup(newOwnersFromNeo = emptyList(), mongoLastTimeStamp = 1L)
        service.updateOwnerList()
        verify(mongo, never()).saveTime(any())
        verify(mongo, never()).saveOwners(any(), any())
    }

    private fun setup(newOwnersFromNeo: List<AddressCollecting.Owner>, mongoLastTimeStamp: Long?) {
        whenever(mongo.getTime()).thenReturn(mongoLastTimeStamp)
        whenever(neo.getOwners(any())).thenReturn(newOwnersFromNeo)
    }
}

fun newOwner(time: Long = 1L, address: String = UUID.randomUUID().toString()) = AddressCollecting.Owner(address, time)
