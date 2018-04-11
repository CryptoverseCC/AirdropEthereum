package io.userfeeds.airdrop.components

import io.userfeeds.airdrop.newOwner
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@Ignore
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner::class)
class MongoDBTest {

    @Autowired
    lateinit var mongoOwnerRepository: MongoOwnerRepository

    @Autowired
    lateinit var mongoTimeRepository: MongoTimeRepository

    @Autowired
    lateinit var mongo: MongoDB

    @Test
    fun shouldSaveOwnerToMongoDatabase() {
        mongo.saveOwners(listOf(newOwner(2L)), false)
        Assert.assertEquals(1, mongoOwnerRepository.count())
    }

    @Test
    fun shouldSaveTimeToMongoDatabase() {
        mongo.saveTime(2L)
        mongo.saveTime(3L)
        mongo.saveTime(4L)
        Assert.assertEquals(4L, mongo.getTime())
        Assert.assertEquals(1, mongoTimeRepository.count())
    }

    @Test
    fun shouldDontSaveDuplicatedOwnersComperingThemByAddress() {
        mongo.saveOwners(listOf(newOwner(address = "0x0"), newOwner(address = "0x0"), newOwner(address = "0x1")), false)
        Assert.assertEquals(2, mongoOwnerRepository.count())
    }

    @Test
    fun shouldReturnUnprocessedAddresses() {
        val owners = listOf(newOwner())
        mongo.saveOwners(owners, false)
        val notProcessedAddresses = mongo.getNotProcessedOwnersAddresses()
        assertThat(notProcessedAddresses).containsExactlyElementsOf(owners.map { it.address })
    }

    @Test
    fun shouldNotReturnAlreadyProcessedAddresses() {
        val owners = listOf(newOwner())
        mongo.saveOwners(owners, false)
        mongo.saveProcessedOwnersAddresses(owners.map { it.address })
        val notProcessedAddresses = mongo.getNotProcessedOwnersAddresses()
        assertThat(notProcessedAddresses).isEmpty()
    }

    @Test
    fun shouldNotRemoveInformationAboutProcessedState() {
        val address = "0x0"
        mongo.saveOwners(listOf(newOwner(address = address)), false)
        mongo.saveProcessedOwnersAddresses(listOf(address))
        mongo.saveOwners(listOf(newOwner(address = address)), false)
        val notProcessedAddresses = mongo.getNotProcessedOwnersAddresses()
        assertThat(notProcessedAddresses).isEmpty()
    }

    @After
    fun tearDown() {
        mongoOwnerRepository.deleteAll()
        mongoTimeRepository.deleteAll()
    }
}