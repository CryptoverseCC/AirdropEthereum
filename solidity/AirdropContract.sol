contract Ownable {

  address owner;
  address pendingOwner;

  modifier onlyOwner {
    require(msg.sender == owner);
    _;
  }

  modifier onlyPendingOwner {
    require(msg.sender == pendingOwner);
    _;
  }

  function Ownable() public {
    owner = msg.sender;
  }

  function transferOwnership(address newOwner) public onlyOwner {
    pendingOwner = newOwner;
  }

  function claimOwnership() public onlyPendingOwner {
    owner = pendingOwner;
  }
}

contract Destructible is Ownable {

  function destroy() public onlyOwner {
    selfdestruct(msg.sender);
  }
}

contract WithClaim {

  event Claim(string data);
}

// Rinkeby: 0x73cDd7e5Cf3DA3985f985298597D404A90878BD9
// Ropsten: 0xA7828A4369B3e89C02234c9c05d12516dbb154BC
// Kovan:   0x5301F5b1Af6f00A61E3a78A9609d1D143B22BB8d

contract UserfeedsClaimWithValueMultiSendUnsafe is Destructible, WithClaim {

  function post(string data, address[] recipients) public payable {
    emit Claim(data);
    send(recipients);
  }

  function post(string data, bytes20[] recipients) public payable {
    emit Claim(data);
    send(recipients);
  }

  function send(address[] recipients) public payable {
    uint amount = msg.value / recipients.length;
    for (uint i = 0; i < recipients.length; i++) {
      recipients[i].send(amount);
    }
    msg.sender.transfer(address(this).balance);
  }

  function send(bytes20[] recipients) public payable {
    uint amount = msg.value / recipients.length;
    for (uint i = 0; i < recipients.length; i++) {
      address(recipients[i]).send(amount);
    }
    msg.sender.transfer(address(this).balance);
  }
}

contract AirdropContract {

    function post(string data, bytes20[] recipients) public payable {
    }
}