package io.userfeeds.airdrop

import com.nhaarman.mockito_kotlin.*
import io.userfeeds.airdrop.processing.AddressProcessingService
import io.userfeeds.airdrop.update.AlreadyProcessed
import io.userfeeds.airdrop.update.AlreadyProcessedService
import org.junit.Test

class AlreadyProcessedServiceTest {

    private val addressProvider = mock<AlreadyProcessed.ProcessedAddressProvider>()
    private val addressStore = mock<AlreadyProcessed.AddressStore>()
    private val alreadyProcessedService = AlreadyProcessedService(addressProvider, addressStore)

    @Test
    fun shouldSaveProcessedAddresses() {
        val owners = listOf(newOwner())
        whenever(addressProvider.getProcessedOwners()).thenReturn(owners)
        alreadyProcessedService.updateProcessedAddressesList()
        verify(addressStore).saveProcessedOwners(owners)
    }
}
