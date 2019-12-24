package bd.com.supply.web3.contract;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class Archives extends Contract {
    private static final String BINARY = "606060405234156200000d57fe5b604051620026b4380380620026b4833981016040528080518201919060200180518201919050505b5b33600060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b81600d9080519060200190620000909291906200039c565b50600c8054806001018281620000a7919062000423565b916000526020600020900160005b33909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505062000116816200011f640100000000026200098a176401000000009004565b5b50506200047a565b600060006000600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156200017f57fe5b600092505b8351831015620002dd5760009150600090505b600c805490508110156200024757600c81815481101515620001b557fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1684848151811015156200020757fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff16141562000238576001915062000247565b5b808060010191505062000197565b811515620002ce57600c805480600101828162000265919062000423565b916000526020600020900160005b86868151811015156200028257fe5b90602001906020020151909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505b5b828060010193505062000184565b60405180807f417263686976657320736574417574686f72697a65644c69737420737563636581526020017f73732e0000000000000000000000000000000000000000000000000000000000815250602301905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab84600160018111156200037f57fe5b6040518082815260200191505060405180910390a35b5b50505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620003df57805160ff191683800117855562000410565b8280016001018555821562000410579182015b828111156200040f578251825591602001919060010190620003f2565b5b5090506200041f919062000452565b5090565b8154818355818115116200044d578183600052602060002091820191016200044c919062000452565b5b505050565b6200047791905b808211156200047357600081600090555060010162000459565b5090565b90565b61222a806200048a6000396000f3006060604052361561011b576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde031461013657806315279260146101cf5780632c01c92214610226578063310ff31b146102505780633927f6af146102e95780633fe963891461030b5780634bde38c8146103a4578063521987411461043d57806373f49c35146104d657806379ba50971461053b5780638da5cb5b1461054d578063a28e67b51461059f578063b1c2997b146105c9578063c33fb87714610662578063d1ac3838146106fb578063d4ee1d9014610725578063dc08d2ab14610777578063e34580da146107c9578063e78624db146107f3578063ee0a39511461081d578063f2fde38b146108b6575b341561012357fe5b6101345b6000151561013157fe5b5b565b005b341561013e57fe5b6101466108ec565b6040518080602001828103825283818151815260200191508051906020019080838360008314610195575b80518252602083111561019557602082019150602081019050602083039250610171565b505050905090810190601f1680156101c15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101d757fe5b61022460048080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509190505061098a565b005b341561022e57fe5b610236610bf9565b604051808215151515815260200191505060405180910390f35b341561025857fe5b610260610c0c565b60405180806020018281038252838181518152602001915080519060200190808383600083146102af575b8051825260208311156102af5760208201915060208101905060208303925061028b565b505050905090810190601f1680156102db5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156102f157fe5b61030960048080351515906020019091905050610caa565b005b341561031357fe5b61031b610e49565b604051808060200182810382528381815181526020019150805190602001908083836000831461036a575b80518252602083111561036a57602082019150602081019050602083039250610346565b505050905090810190601f1680156103965780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156103ac57fe5b6103b4611058565b6040518080602001828103825283818151815260200191508051906020019080838360008314610403575b805182526020831115610403576020820191506020810190506020830392506103df565b505050905090810190601f16801561042f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561044557fe5b61044d6110f6565b604051808060200182810382528381815181526020019150805190602001908083836000831461049c575b80518252602083111561049c57602082019150602081019050602083039250610478565b505050905090810190601f1680156104c85780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156104de57fe5b610539600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091908035151590602001909190505061124d565b005b341561054357fe5b61054b61142e565b005b341561055557fe5b61055d61160e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156105a757fe5b6105af611634565b604051808215151515815260200191505060405180910390f35b34156105d157fe5b6105d9611647565b6040518080602001828103825283818151815260200191508051906020019080838360008314610628575b80518252602083111561062857602082019150602081019050602083039250610604565b505050905090810190601f1680156106545780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561066a57fe5b6106726116e5565b60405180806020018281038252838181518152602001915080519060200190808383600083146106c1575b8051825260208311156106c15760208201915060208101905060208303925061069d565b505050905090810190601f1680156106ed5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561070357fe5b61070b611783565b604051808215151515815260200191505060405180910390f35b341561072d57fe5b610735611796565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561077f57fe5b6107876117bc565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156107d157fe5b6107d96117e2565b604051808215151515815260200191505060405180910390f35b34156107fb57fe5b6108036117f5565b604051808215151515815260200191505060405180910390f35b341561082557fe5b61082d611808565b604051808060200182810382528381815181526020019150805190602001908083836000831461087c575b80518252602083111561087c57602082019150602081019050602083039250610858565b505050905090810190601f1680156108a85780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156108be57fe5b6108ea600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506118a6565b005b600d8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109825780601f1061095757610100808354040283529160200191610982565b820191906000526020600020905b81548152906001019060200180831161096557829003601f168201915b505050505081565b600060006000600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156109e957fe5b600092505b8351831015610b3b5760009150600090505b600c80549050811015610aaa57600c81815481101515610a1c57fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168484815181101515610a6d57fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff161415610a9c5760019150610aaa565b5b8080600101915050610a00565b811515610b2d57600c8054806001018281610ac59190612105565b916000526020600020900160005b8686815181101515610ae157fe5b90602001906020020151909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505b5b82806001019350506109ee565b60405180807f417263686976657320736574417574686f72697a65644c69737420737563636581526020017f73732e0000000000000000000000000000000000000000000000000000000000815250602301905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab8460016001811115610bdc57fe5b6040518082815260200191505060405180910390a35b5b50505050565b600360009054906101000a900460ff1681565b600a8054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610ca25780601f10610c7757610100808354040283529160200191610ca2565b820191906000526020600020905b815481529060010190602001808311610c8557829003601f168201915b505050505081565b6000600060009150600090505b600c80549050811015610d4a573373ffffffffffffffffffffffffffffffffffffffff16600c82815481101515610cea57fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415610d3c5760019150610d4a565b5b8080600101915050610cb7565b811515610d575760006000fd5b82600360006101000a81548160ff02191690831515021790555033600b60016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060405180807f736574466c616720737563636573732e00000000000000000000000000000000815250601001905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab8460016001811115610e2d57fe5b6040518082815260200191505060405180910390a35b5b505050565b610e51612131565b610e59612131565b604060405190810160405280600581526020017f66616c73650000000000000000000000000000000000000000000000000000008152509050600360009054906101000a900460ff1615610ee157604060405190810160405280600481526020017f747275650000000000000000000000000000000000000000000000000000000081525090505b610f7382610f26600b60019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166119a3565b604060405190810160405280600181526020017f24000000000000000000000000000000000000000000000000000000000000008152506020604051908101604052806000815250611bb1565b91506110518260028054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561100f5780601f10610fe45761010080835404028352916020019161100f565b820191906000526020600020905b815481529060010190602001808311610ff257829003601f168201915b5050505050604060405190810160405280600181526020017f260000000000000000000000000000000000000000000000000000000000000081525084611bb1565b91505b5090565b60068054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110ee5780601f106110c3576101008083540402835291602001916110ee565b820191906000526020600020905b8154815290600101906020018083116110d157829003601f168201915b505050505081565b6110fe612131565b6000600090505b600c80549050811015611248576001600c805490500381141561118c5761118582611180600c8481548110151561113857fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166119a3565b611f24565b915061123a565b611237826111ea600c848154811015156111a257fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166119a3565b6020604051908101604052806000815250604060405190810160405280600181526020017f2400000000000000000000000000000000000000000000000000000000000000815250611bb1565b91505b5b8080600101915050611105565b5b5090565b6000600060009150600090505b600c805490508110156112ed573373ffffffffffffffffffffffffffffffffffffffff16600c8281548110151561128d57fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614156112df57600191506112ed565b5b808060010191505061125a565b8115156112fa5760006000fd5b6002845111801561131e575060001515600360009054906101000a900460ff161515145b15611355578360029080519060200190611339929190612145565b5082600360006101000a81548160ff0219169083151502179055505b33600b60016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060405180807f7570646174654261736963417263686976657320737563636573732e00000000815250601c01905060405180910390203373ffffffffffffffffffffffffffffffffffffffff167f718cff2f30737c5a051976934736d5025bef9cea6b95e5c682a3d15353e3ab846001600181111561141157fe5b6040518082815260200191505060405180910390a35b5b50505050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561148b5760006000fd5b7f343765429aea5a34b3ff6a3785a98a5abb2597aca87bfbb58632c173d585373a600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a1600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b565b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600960009054906101000a900460ff1681565b60048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156116dd5780601f106116b2576101008083540402835291602001916116dd565b820191906000526020600020905b8154815290600101906020018083116116c057829003601f168201915b505050505081565b60088054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561177b5780601f106117505761010080835404028352916020019161177b565b820191906000526020600020905b81548152906001019060200180831161175e57829003601f168201915b505050505081565b600560009054906101000a900460ff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600b60019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600b60009054906101000a900460ff1681565b600760009054906101000a900460ff1681565b60028054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561189e5780601f106118735761010080835404028352916020019161189e565b820191906000526020600020905b81548152906001019060200180831161188157829003601f168201915b505050505081565b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156118ff57fe5b600060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415151561195d5760006000fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b5b50565b6119ab612131565b60006000602a6040518059106119be5750595b908082528060200260200182016040525b5092507f3000000000000000000000000000000000000000000000000000000000000000836000815181101515611a0257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053507f7800000000000000000000000000000000000000000000000000000000000000836001815181101515611a6257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350602991505b60028260ff16101515611ba957600f84169050601084811515611ab657fe5b049350600a8160ff161015611b3157603081017f010000000000000000000000000000000000000000000000000000000000000002838360ff16815181101515611afc57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350611b9c565b6061600a8203017f010000000000000000000000000000000000000000000000000000000000000002838360ff16815181101515611b6b57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b5b81600190039150611a97565b5b5050919050565b611bb9612131565b611bc16121c5565b611bc96121c5565b611bd16121c5565b611bd96121c5565b611be1612131565b611be96121c5565b600060008c97508b96508a95508994508451865188518a51010101604051805910611c115750595b908082528060200260200182016040525b50935083925060009150600090505b8751811015611ce5578781815181101515611c4857fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383806001019450815181101515611ca757fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611c31565b600090505b8651811015611d9e578681815181101515611d0157fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383806001019450815181101515611d6057fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611cea565b600090505b8551811015611e57578581815181101515611dba57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383806001019450815181101515611e1957fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611da3565b600090505b8451811015611f10578481815181101515611e7357fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383806001019450815181101515611ed257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611e5c565b8398505b5050505050505050949350505050565b611f2c612131565b611f346121c5565b611f3c6121c5565b611f44612131565b611f4c6121c5565b600060008895508794508451865101604051805910611f685750595b908082528060200260200182016040525b50935083925060009150600090505b855181101561203c578581815181101515611f9f57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383806001019450815181101515611ffe57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050611f88565b600090505b84518110156120f557848181518110151561205857fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838060010194508151811015156120b757fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053505b8080600101915050612041565b8396505b50505050505092915050565b81548183558181151161212c5781836000526020600020918201910161212b91906121d9565b5b505050565b602060405190810160405280600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061218657805160ff19168380011785556121b4565b828001600101855582156121b4579182015b828111156121b3578251825591602001919060010190612198565b5b5090506121c191906121d9565b5090565b602060405190810160405280600081525090565b6121fb91905b808211156121f75760008160009055506001016121df565b5090565b905600a165627a7a7230582025ea99097156eb1e9fd21753631ebc5fd2ac6a734ce242b13c74195da39670c50029";

    protected Archives(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Archives(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
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

    public RemoteCall<String> name() {
        final Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setAuthorizedList(List<String> _list) {
        final Function function = new Function(
                "setAuthorizedList", 
                Arrays.<Type>asList(new DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_list, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> basicFlag() {
        final Function function = new Function("basicFlag", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> buss() {
        final Function function = new Function("buss", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setFlag(Boolean _basicFlag) {
        final Function function = new Function(
                "setFlag", 
                Arrays.<Type>asList(new Bool(_basicFlag)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getArchives() {
        final Function function = new Function("getArchives", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> platform() {
        final Function function = new Function("platform", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> authorizedAddresses() {
        final Function function = new Function("authorizedAddresses", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> updateArchives(String _basicInfo, Boolean _basicFlag) {
        final Function function = new Function(
                "updateArchives", 
                Arrays.<Type>asList(new Utf8String(_basicInfo),
                new Bool(_basicFlag)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> acceptOwnership() {
        final Function function = new Function(
                "acceptOwnership", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> processFlag() {
        final Function function = new Function("processFlag", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> packagedoc() {
        final Function function = new Function("packagedoc", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> process() {
        final Function function = new Function("process", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> packageFlag() {
        final Function function = new Function("packageFlag", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> newOwner() {
        final Function function = new Function("newOwner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> lastUpdateAddr() {
        final Function function = new Function("lastUpdateAddr", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> bussFlag() {
        final Function function = new Function("bussFlag", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> platformFlag() {
        final Function function = new Function("platformFlag", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> basicInfo() {
        final Function function = new Function("basicInfo", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String _newOwner) {
        final Function function = new Function(
                "transferOwnership", 
                Arrays.<Type>asList(new Address(_newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Archives> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name, List<String> _list) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_name),
                new DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_list, Address.class))));
        return deployRemoteCall(Archives.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Archives> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _name, List<String> _list) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_name),
                new DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_list, Address.class))));
        return deployRemoteCall(Archives.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Archives load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Archives(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Archives load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Archives(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
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
