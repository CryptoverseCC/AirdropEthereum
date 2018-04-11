package io.userfeeds.airdrop.components

import io.userfeeds.airdrop.processing.AddressProcessing
import io.userfeeds.airdrop.collecting.AddressCollecting
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
class MongoDB(
        private val mongoOwnerRepository: MongoOwnerRepository,
        private val mongoTimeRepository: MongoTimeRepository
) : AddressCollecting.AddressStore, AddressProcessing.MongoDB {

    override fun saveTime(timestamp: Long) {
        val id = mongoTimeRepository.findAll().firstOrNull()?.id
        mongoTimeRepository.save(MongoTime(id, timestamp))
    }

    override fun getTime(): Long? {
        return mongoTimeRepository.findAll().firstOrNull()?.timestamp
    }

    override fun saveOwners(owners: List<AddressCollecting.Owner>, processed: Boolean) {
        val oldAddresses = mongoOwnerRepository.findAllById(owners.map { it.address }).map { it.address }
        owners
                .filterNot { it.address in oldAddresses }
                .map { MongoOwner(address = it.address, processed = processed) }
                .let { mongoOwnerRepository.saveAll(it) }
    }

    override fun getNotProcessedOwnersAddresses(): List<String> {
        return mongoOwnerRepository.findByProcessedFalse().map { it.address }
    }

    override fun saveProcessedOwnersAddresses(addresses: List<String>) {
        mongoOwnerRepository.findAllById(addresses)
                .map { it.copy(processed = true) }
                .let { mongoOwnerRepository.saveAll(it) }
    }
}

data class MongoOwner(
        @Id val address: String,
        val processed: Boolean? = false
)

data class MongoTime(
        val id: String? = null,
        val timestamp: Long
)

interface MongoOwnerRepository : MongoRepository<MongoOwner, String> {
    fun findByProcessedFalse(): MutableList<MongoOwner>
}

interface MongoTimeRepository : MongoRepository<MongoTime, String>
