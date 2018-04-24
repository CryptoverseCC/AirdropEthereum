package io.userfeeds.airdrop.collecting

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AddressCollectingService(private val addressStore: AddressCollecting.AddressStore,
                               private val addressProvider: AddressCollecting.NewAddressProvider,
                               @Value("\${SKIP_ADDRESSES}") private val skipAddresses: Boolean) {

    fun updateOwnerList() {
        val newOwners = addressProvider.getOwners(addressStore.getTime())
        if (newOwners.isNotEmpty()) {
            addressStore.saveOwners(newOwners, skipAddresses)
            addressStore.saveTime(newOwners.map { it.timestamp }.max() ?: 0)
        }
    }
}