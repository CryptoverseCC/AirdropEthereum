package io.userfeeds.airdrop.components

import io.userfeeds.airdrop.components.solidity.AirdropContract
import io.userfeeds.airdrop.processing.AddressProcessing
import io.userfeeds.cache.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.web3j.abi.Utils
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes20
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import java.math.BigInteger
import java.util.*

@Component
class KovanAddressProcessor(
        @Value("\${PRIVATE_KEY}") private val privateKey: String,
        @Value("\${AIRDROP_CLAIM_ID}") private val airdropClaimId: String,
        @Value("\${INFURA_TOKEN}") private val infuraToken: String,
        @Value("\${PROCESS_ADDRESSES}") private val processAddresses: Boolean
) : AddressProcessing.Processor {

    private val logger = logger()

    private val weiSendToEachAddress = 1_000_000_000L
    private val contractAddress = "0x5301F5b1Af6f00A61E3a78A9609d1D143B22BB8d"
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
            BigInteger.valueOf(weiSendToEachAddress).times(BigInteger.valueOf(addresses.size.toLong()))

    private fun String.hexStringToByteArray(): ByteArray {
        return drop(2).chunked(2)
                .map {
                    ((Character.digit(it[0], 16) shl 4) + (Character.digit(it[1], 16))).toByte()
                }
                .toByteArray()
    }
}
