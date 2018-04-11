package io.userfeeds.airdrop.components.solidity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class AirdropContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60f98061001d6000396000f300606060405260043610603e5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166334716f6781146043575b600080fd5b60c760046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437820191505050505050919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284375094965060c995505050505050565b005b50505600a165627a7a72305820322370ae333622d8280251808873ff98d9e70d607560119105a6ddffdcd805420029";

    protected AirdropContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AirdropContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> post(String data, List<byte[]> recipients, BigInteger weiValue) {
        final Function function = new Function(
                "post", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(data), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes20>(
                        org.web3j.abi.Utils.typeMap(recipients, org.web3j.abi.datatypes.generated.Bytes20.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public static RemoteCall<AirdropContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AirdropContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<AirdropContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AirdropContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static AirdropContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AirdropContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static AirdropContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AirdropContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
