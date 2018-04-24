package io.userfeeds.airdrop.update

import org.springframework.stereotype.Component

@Component
class AlreadyProcessedService(
        private val addressProvider: AlreadyProcessed.ProcessedAddressProvider,
        private val addressStore: AlreadyProcessed.AddressStore
) {
    fun updateProcessedAddressesList() {
        addressProvider
                .getProcessedOwners()
                .let { owners ->
                    addressStore.saveProcessedOwners(owners)
                }
    }
}
