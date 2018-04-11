package io.userfeeds.airdrop

import com.nhaarman.mockito_kotlin.*
import io.userfeeds.airdrop.processing.AddressProcessing
import io.userfeeds.airdrop.processing.AddressProcessingService
import org.junit.Test

class AddressProcessingServiceTest {

    private val mongo = mock<AddressProcessing.MongoDB>()
    private val processor = mock<AddressProcessing.Processor>()
    private val addressProcessingService = AddressProcessingService(mongo, processor)

    @Test
    fun shouldProcessAddresses() {
        val addresses = listOf("0x0")
        whenever(mongo.getNotProcessedOwnersAddresses()).thenReturn(addresses)
        addressProcessingService.processOwners()
        verify(processor).processAddresses(addresses)
        verify(mongo).saveProcessedOwnersAddresses(addresses)
    }

    @Test
    fun shouldProcessAddressesInBatches() {
        val addresses = (1..600).map { "0x$it" }
        whenever(mongo.getNotProcessedOwnersAddresses()).thenReturn(addresses)
        addressProcessingService.processOwners()
        verify(processor).processAddresses((1..300).map { "0x$it" })
        verify(processor).processAddresses((301..600).map { "0x$it" })
        verify(mongo).saveProcessedOwnersAddresses((1..300).map { "0x$it" })
        verify(mongo).saveProcessedOwnersAddresses((301..600).map { "0x$it" })
    }

    @Test
    fun shouldNotSaveUnprocessedAddresses() {
        val addresses = listOf("0x0")
        whenever(mongo.getNotProcessedOwnersAddresses()).thenReturn(addresses)
        whenever(processor.processAddresses(any())).thenThrow(RuntimeException())
        addressProcessingService.processOwners()
        verify(mongo, never()).saveProcessedOwnersAddresses(addresses)
    }
}
