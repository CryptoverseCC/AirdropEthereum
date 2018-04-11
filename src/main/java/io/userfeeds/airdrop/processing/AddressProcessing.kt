package io.userfeeds.airdrop.processing

interface AddressProcessing {

    interface MongoDB {
        fun getNotProcessedOwnersAddresses() : List<String>
        fun saveProcessedOwnersAddresses(addresses: List<String>)
    }


    interface Processor {
        fun processAddresses(addresses: List<String>)
    }
}
