package bd.com.walletdb.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author:     xumin
 * date:       2018/11/4
 * email:      xumin2@evergrande.cn
 */
@Entity
public class TransactionDetail {
    @Id
    private String pkHash;
    private String blockHash;
    private long blockNumber;
    private String blockTimestamp;
    private String blockGasLimit;
    private String transactionIndex;
    private String transactionFrom;
    private String transactionTo;
    private long gas;
    private long gasUsed;
    private long gasPrice;
    private long cumulativeGas;
    private String randomId;
    private String inputText;
    private long lastBlockNumber;
    private String txCost;
    private String value;
    private String tokenTransferTo;
    private String tokenTransfer;
    private int type;

    @Generated(hash = 658078881)
    public TransactionDetail(String pkHash, String blockHash, long blockNumber,
            String blockTimestamp, String blockGasLimit, String transactionIndex,
            String transactionFrom, String transactionTo, long gas, long gasUsed,
            long gasPrice, long cumulativeGas, String randomId, String inputText,
            long lastBlockNumber, String txCost, String value,
            String tokenTransferTo, String tokenTransfer, int type) {
        this.pkHash = pkHash;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.blockGasLimit = blockGasLimit;
        this.transactionIndex = transactionIndex;
        this.transactionFrom = transactionFrom;
        this.transactionTo = transactionTo;
        this.gas = gas;
        this.gasUsed = gasUsed;
        this.gasPrice = gasPrice;
        this.cumulativeGas = cumulativeGas;
        this.randomId = randomId;
        this.inputText = inputText;
        this.lastBlockNumber = lastBlockNumber;
        this.txCost = txCost;
        this.value = value;
        this.tokenTransferTo = tokenTransferTo;
        this.tokenTransfer = tokenTransfer;
        this.type = type;
    }

    @Generated(hash = 487738323)
    public TransactionDetail() {
    }

    public String getPkHash() {
        return pkHash;
    }

    public void setPkHash(String pkHash) {
        this.pkHash = pkHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(String blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public String getBlockGasLimit() {
        return blockGasLimit;
    }

    public void setBlockGasLimit(String blockGasLimit) {
        this.blockGasLimit = blockGasLimit;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getTransactionFrom() {
        return transactionFrom;
    }

    public void setTransactionFrom(String transactionFrom) {
        this.transactionFrom = transactionFrom;
    }

    public String getTransactionTo() {
        return transactionTo;
    }

    public void setTransactionTo(String transactionTo) {
        this.transactionTo = transactionTo;
    }

    public long getGas() {
        return gas;
    }

    public void setGas(long gas) {
        this.gas = gas;
    }

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public long getCumulativeGas() {
        return cumulativeGas;
    }

    public void setCumulativeGas(long cumulativeGas) {
        this.cumulativeGas = cumulativeGas;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public long getLastBlockNumber() {
        return lastBlockNumber;
    }

    public void setLastBlockNumber(long lastBlockNumber) {
        this.lastBlockNumber = lastBlockNumber;
    }

    public String getTxCost() {
        return txCost;
    }

    public void setTxCost(String txCost) {
        this.txCost = txCost;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTokenTransferTo() {
        return tokenTransferTo;
    }

    public void setTokenTransferTo(String tokenTransferTo) {
        this.tokenTransferTo = tokenTransferTo;
    }

    public String getTokenTransfer() {
        return tokenTransfer;
    }

    public void setTokenTransfer(String tokenTransfer) {
        this.tokenTransfer = tokenTransfer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
