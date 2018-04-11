package io.userfeeds.airdrop.collecting

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AddressCollectingService(private val mongo: AddressCollecting.AddressStore,
                               private val neo: AddressCollecting.NewAddressProvider,
                               @Value("\${SKIP_ADDRESSES}") private val skipAddresses: Boolean) {

    fun updateOwnerList() {
        val lastUpdate = mongo.getTime() ?: 0
        val newOwners = neo.getOwners(lastUpdate)
        if (newOwners.isNotEmpty()) {
            mongo.saveOwners(newOwners, skipAddresses)
            mongo.saveTime(newOwners.map { it.timestamp }.max() ?: 0)
        }
    }
}