package io.userfeeds.airdrop.components

import io.userfeeds.airdrop.collecting.AddressCollecting
import io.userfeeds.airdrop.dto.Owner
import io.userfeeds.airdrop.processing.AddressProcessing
import io.userfeeds.airdrop.update.AlreadyProcessed
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
class MongoDB(
        private val mongoOwnerRepository: MongoOwnerRepository,
        private val mongoTimeRepository: MongoTimeRepository
) : AddressCollecting.AddressStore, AlreadyProcessed.AddressStore, AddressProcessing.MongoDB {

    override fun saveTime(timestamp: Long) {
        val id = mongoTimeRepository.findAll().firstOrNull()?.id
        mongoTimeRepository.save(MongoTime(id, timestamp))
    }

    override fun getTime(): Long? {
        return mongoTimeRepository.findAll().firstOrNull()?.timestamp
    }

    override fun saveProcessedOwners(owners: List<Owner>) {
        saveOwners(owners, true)
    }

    override fun saveOwners(owners: List<Owner>, processed: Boolean) {
        val oldOwners = mongoOwnerRepository.findAllById(owners.map { it.address }).toSet()
        val mongoOwners = owners.map { MongoOwner(address = it.address, processed = processed) }
        if (processed) {
            mongoOwners
                    .filterNot { it in oldOwners }
                    .let { mongoOwnerRepository.saveAll(it) }
        } else {
            val oldAddresses = oldOwners.map { it.address }
            mongoOwners
                    .filterNot { it.address in oldAddresses }
                    .let { mongoOwnerRepository.saveAll(it) }
        }
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
