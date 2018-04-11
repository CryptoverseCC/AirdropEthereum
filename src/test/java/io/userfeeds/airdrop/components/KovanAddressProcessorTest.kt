package io.userfeeds.airdrop.components

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@Ignore
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner::class)
class KovanAddressProcessorTest {

    @Autowired
    lateinit var kovanAddressProcessor: KovanAddressProcessor

    @Test
    fun shouldMakeAirdrop() {
        kovanAddressProcessor.processAddresses(listOf("0x9a0Ec83a80fd17606250B6F8E33A8b94f7864176"))
    }
}