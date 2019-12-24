package bd.com.supply.web3.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class BigPackingBox extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b6040516118cc3803806118cc833981016040528080518201919060200180519060200190919050505b5b33600060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b60008173ffffffffffffffffffffffffffffffffffffffff161415151561009f5760006000fd5b80600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006004819055505b50505b6117d1806100fb6000396000f300606060405236156100b8576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063292a3f7e146100ba578063306aea7a146100e057806358cbd2ae1461015657806379ba5097146101ef5780638d9f9f67146102015780638da5cb5b14610253578063a64719d7146102a5578063d4ee1d90146102f7578063e209faa514610349578063ed73b4d51461039b578063f28a3c27146103fe578063f2fde38b14610450575bfe5b34156100c257fe5b6100ca610486565b6040518082815260200191505060405180910390f35b34156100e857fe5b61015460048080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509190803573ffffffffffffffffffffffffffffffffffffffff1690602001909190505061048c565b005b341561015e57fe5b610166610758565b60405180806020018281038252838181518152602001915080519060200190808383600083146101b5575b8051825260208311156101b557602082019150602081019050602083039250610191565b505050905090810190601f1680156101e15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101f757fe5b6101ff6108d5565b005b341561020957fe5b610211610ab5565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561025b57fe5b610263610ae0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156102ad57fe5b6102b5610b06565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156102ff57fe5b610307610b2c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561035157fe5b610359610b52565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156103a357fe5b6103fc600480803590602001909190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610b7d565b005b341561040657fe5b61040e610ea7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561045857fe5b610484600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610ecd565b005b60045481565b6000600060045414151561049c57fe5b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168073ffffffffffffffffffffffffffffffffffffffff166381c4f3a332600160068111156104e957fe5b6000604051602001526040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b151561057357fe5b6102c65a03f1151561058157fe5b5050506040518051905015156105975760006000fd5b600084511115156105a85760006000fd5b60008373ffffffffffffffffffffffffffffffffffffffff16141515156105cf5760006000fd5b82600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600091505b83518210156106a75760028054806001018281610632919061172c565b916000526020600020900160005b868581518110151561064e57fe5b90602001906020020151909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505b8180600101925050610615565b60006002805490501415156106bf5760016004819055505b60405180807f616464207061636b696e67426f78207375636365737300000000000000000000815250601601905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab846001600181111561073a57fe5b6040518082815260200191505060405180910390a35b5b505b505050565b610760611758565b60006000600280549050141561078857602060405190810160405280600081525091506108d1565b600090505b6002805490508110156108d0576001600280549050038114156108145761080d826108086002848154811015156107c057fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16610fca565b6111d8565b91506108c2565b6108bf8261087260028481548110151561082a57fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16610fca565b604060405190810160405280600181526020017f240000000000000000000000000000000000000000000000000000000000000081525060206040519081016040528060008152506113b9565b91505b5b808060010191505061078d565b5b5090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156109325760006000fd5b7f343765429aea5a34b3ff6a3785a98a5abb2597aca87bfbb58632c173d585373a600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a1600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690505b90565b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690505b90565b6000600060006006811115610b8e57fe5b8414158015610ba9575060016006811115610ba557fe5b8414155b8015610bc1575060026006811115610bbd57fe5b8414155b8015610bd9575060036006811115610bd557fe5b8414155b8015610bf1575060046006811115610bed57fe5b8414155b8015610c09575060056006811115610c0557fe5b8414155b8015610c21575060066006811115610c1d57fe5b8414155b15610cbb5760405180807f496e76616c6964207065726f6964000000000000000000000000000000000000815250600e01905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab8460006001811115610ca157fe5b6040518082815260200191505060405180910390a3610ea1565b600091505b600280549050821015610e1057600282815481101515610cdc57fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff1663ed73b4d585856040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018083815260200180602001828103825283818151815260200191508051906020019080838360008314610da9575b805182526020831115610da957602082019150602081019050602083039250610d85565b505050905090810190601f168015610dd55780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1515610df157fe5b6102c65a03f11515610dff57fe5b5050505b8180600101925050610cc0565b60405180807f626967426f7820617070656e64456e74727920737563636573732e0000000000815250601b01905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab8460016001811115610e8b57fe5b6040518082815260200191505060405180910390a35b50505050565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610f2657fe5b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614151515610f845760006000fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b5b50565b610fd2611758565b60006000602a604051805910610fe55750595b908082528060200260200182016040525b5092507f300000000000000000000000000000000000000000000000000000000000000083600081518110151561102957fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053507f780000000000000000000000000000000000000000000000000000000000000083600181518110151561108957fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350602991505b60028260ff161015156111d057600f841690506010848115156110dd57fe5b049350600a8160ff16101561115857603081017f010000000000000000000000000000000000000000000000000000000000000002838360ff1681518110151561112357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053506111c3565b6061600a8203017f010000000000000000000000000000000000000000000000000000000000000002838360ff1681518110151561119257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b5b816001900391506110be565b5b5050919050565b6111e0611758565b6111e861176c565b6111f061176c565b6111f8611758565b61120061176c565b60006000889550879450845186510160405180591061121c5750595b908082528060200260200182016040525b50935083925060009150600090505b85518110156112f057858181518110151561125357fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838060010194508151811015156112b257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b808060010191505061123c565b600090505b84518110156113a957848181518110151561130c57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561136b57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b80806001019150506112f5565b8396505b50505050505092915050565b6113c1611758565b6113c961176c565b6113d161176c565b6113d961176c565b6113e161176c565b6113e9611758565b6113f161176c565b600060008c97508b96508a95508994508451865188518a510101016040518059106114195750595b908082528060200260200182016040525b50935083925060009150600090505b87518110156114ed57878181518110151561145057fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838060010194508151811015156114af57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611439565b600090505b86518110156115a657868181518110151561150957fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561156857fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b80806001019150506114f2565b600090505b855181101561165f5785818151811015156115c257fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561162157fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b80806001019150506115ab565b600090505b845181101561171857848181518110151561167b57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838060010194508151811015156116da57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611664565b8398505b5050505050505050949350505050565b815481835581811511611753578183600052602060002091820191016117529190611780565b5b505050565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b6117a291905b8082111561179e576000816000905550600101611786565b5090565b905600a165627a7a72305820087935056c5a406485cc9090ec3ed618b721a9444a0fb10fb4458a177346e3970029";

    protected BigPackingBox(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BigPackingBox(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<ResponseEventResponse> getResponseEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Response", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ResponseEventResponse> responses = new ArrayList<ResponseEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ResponseEventResponse typedResponse = new ResponseEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.errmsg = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.errno = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ResponseEventResponse> responseEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Response", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ResponseEventResponse>() {
            @Override
            public ResponseEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ResponseEventResponse typedResponse = new ResponseEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.errmsg = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.errno = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<OwnerUpdateEventResponse> getOwnerUpdateEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnerUpdate", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<OwnerUpdateEventResponse> responses = new ArrayList<OwnerUpdateEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnerUpdateEventResponse typedResponse = new OwnerUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._prevOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._newOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnerUpdateEventResponse> ownerUpdateEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnerUpdate", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnerUpdateEventResponse>() {
            @Override
            public OwnerUpdateEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                OwnerUpdateEventResponse typedResponse = new OwnerUpdateEventResponse();
                typedResponse.log = log;
                typedResponse._prevOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._newOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> acceptOwnership() {
        final Function function = new Function(
                "acceptOwnership", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addPackingBox(List<String> _blist, String _authAddress) {
        final Function function = new Function(
                "addPackingBox", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_blist, Address.class)),
                new Address(_authAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> appendEntry(BigInteger _peroid, String _detailInfo) {
        final Function function = new Function(
                "appendEntry", 
                Arrays.<Type>asList(new Uint256(_peroid),
                new Utf8String(_detailInfo)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String _newOwner) {
        final Function function = new Function(
                "transferOwnership", 
                Arrays.<Type>asList(new Address(_newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<BigPackingBox> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, List<String> _blist, String _authAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_blist, Address.class)),
                new Address(_authAddress)));
        return deployRemoteCall(BigPackingBox.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<BigPackingBox> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, List<String> _blist, String _authAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_blist, Address.class)),
                new Address(_authAddress)));
        return deployRemoteCall(BigPackingBox.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public RemoteCall<String> authAddress() {
        final Function function = new Function("authAddress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> authIdentify() {
        final Function function = new Function("authIdentify", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> boxList() {
        final Function function = new Function("boxList", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> newOwner() {
        final Function function = new Function("newOwner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> once() {
        final Function function = new Function("once", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> packAuthAddress() {
        final Function function = new Function("packAuthAddress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> packAuthIdentify() {
        final Function function = new Function("packAuthIdentify", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static BigPackingBox load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BigPackingBox(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static BigPackingBox load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BigPackingBox(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ResponseEventResponse {
        public Log log;

        public String from;

        public byte[] errmsg;

        public BigInteger errno;
    }

    public static class OwnerUpdateEventResponse {
        public Log log;

        public String _prevOwner;

        public String _newOwner;
    }
}
