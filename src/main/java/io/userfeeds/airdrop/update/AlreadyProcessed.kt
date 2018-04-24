package io.userfeeds.airdrop.update

import io.userfeeds.airdrop.dto.Owner

interface AlreadyProcessed {

    interface ProcessedAddressProvider {
        fun getProcessedOwners(): List<Owner>
    }

    interface AddressStore {
        fun saveProcessedOwners(owners: List<Owner>)
    }
}
