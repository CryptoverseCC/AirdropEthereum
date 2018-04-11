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
class HttpNewAddressProviderTest {

    @Autowired
    lateinit var httpNewAddressProvider: HttpNewAddressProvider

    @Test
    fun shouldFetchAddressesFromApi() {
        httpNewAddressProvider.getOwners(1523096603927L).forEach { println(it) }
    }
}