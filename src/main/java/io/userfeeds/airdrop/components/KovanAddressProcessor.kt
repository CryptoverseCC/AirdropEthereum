package io.userfeeds.airdrop.components

import io.userfeeds.airdrop.components.solidity.AirdropContract
import io.userfeeds.airdrop.processing.AddressProcessing
import io.userfeeds.cache.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.math.BigInteger

@Component
class KovanAddressProcessor(
        @Value("\${PRIVATE_KEY}") private val privateKey: String,
        @Value("\${AIRDROP_CLAIM_ID}") private val airdropClaimId: String,
        @Value("\${INFURA_TOKEN}") private val infuraToken: String,
        @Value("\${PROCESS_ADDRESSES}") private val processAddresses: Boolean,
        @Value("\${AMOUNT_SEND_TO_EACH_ADDRESS_IN_WEI}") amountSendToEachAddressInWei: String
) : AddressProcessing.Processor {

    private val logger = logger()

    private val weiSendToEachAddress = BigInteger(amountSendToEachAddressInWei)
    private val contractAddress = "0x5301f5b1af6f00a61e3a78a9609d1d143b22bb8d"
    private val web3j = Web3j.build(HttpService("https://kovan.infura.io/$infuraToken"))
    private val contract = AirdropContract.load(
            contractAddress,
            web3j,
            Credentials.create(privateKey),
            BigInteger.valueOf(1_000_000_000L),
            BigInteger.valueOf(7_950_000) //Slightly less than 8M kovan limit
    )

    override fun processAddresses(addresses: List<String>) {
        if (!processAddresses) return
        logger.info(addresses.toString())
        contract.post("""{"target":"$airdropClaimId"}""", convertToByteArrays(addresses), getTotalWeiValue(addresses)).send()
    }

    private fun convertToByteArrays(addresses: List<String>) =
            addresses.map { it.hexStringToByteArray() }

    private fun getTotalWeiValue(addresses: List<String>) =
            weiSendToEachAddress.times(BigInteger.valueOf(addresses.size.toLong()))

    private fun String.hexStringToByteArray(): ByteArray {
        return drop(2).chunked(2)
                .map {
                    ((Character.digit(it[0], 16) shl 4) + (Character.digit(it[1], 16))).toByte()
                }
                .toByteArray()
    }
}
