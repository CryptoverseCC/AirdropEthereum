package io.userfeeds.airdrop.collecting

import io.userfeeds.airdrop.dto.Owner

interface AddressCollecting {

    interface NewAddressProvider {
        fun getOwners(since: Long?): List<Owner>
    }

    interface AddressStore {
        fun getTime(): Long?
        fun saveOwners(owners: List<Owner>, processes: Boolean)
        fun saveTime(timestamp: Long)
    }

}