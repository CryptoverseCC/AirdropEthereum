#!/bin/bash
set -e

solc solidity/AirdropContract.sol --overwrite --bin --abi --optimize -o solidity
web3j solidity generate solidity/AirdropContract.bin solidity/AirdropContract.abi -o src/main/java -p io.userfeeds.airdrop.components.solidity
rm solidity/*.bin | true
rm solidity/*.abi | true