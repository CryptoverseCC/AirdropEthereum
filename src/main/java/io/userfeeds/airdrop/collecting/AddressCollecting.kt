package io.userfeeds.airdrop.collecting

interface AddressCollecting {

    interface NewAddressProvider {
        fun getOwners(since: Long?): List<Owner>
    }

    interface AddressStore {
        fun getTime(): Long?
        fun saveOwners(owners: List<Owner>, processes: Boolean)
        fun saveTime(timestamp: Long)
    }

    data class Owner(val address: String,
                     val timestamp: Long)

}