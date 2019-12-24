package bd.com.walletdb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class TxHistory implements Parcelable {

    @Id
    private String pkHash;
    private String blockHash;
    private int blockNumber;
    private String blockTimesStr;
    private String blockGasLimit;
    private String transactionIndex;
    private String transactionFrom;
    private String transactionTo;
    private String value;
    private String gas;
    private String gasPrice;
    private String cumulativeGas;
    private int type;
    private String tokenTransferTo;//代币转账接收者
    private String tokenTransfer;//代币转账数量
    private String address;//tokenAddr;
    private String walletAddr;
    private int lastBlockNumber;
    private String chainId;
    private int state;//0已确认状态 ,1,确认中状态,2 pengding状态


    protected TxHistory(Parcel in) {
        pkHash = in.readString();
        blockHash = in.readString();
        blockNumber = in.readInt();
        blockTimesStr = in.readString();
        blockGasLimit = in.readString();
        transactionIndex = in.readString();
        transactionFrom = in.readString();
        transactionTo = in.readString();
        value = in.readString();
        gas = in.readString();
        gasPrice = in.readString();
        cumulativeGas = in.readString();
        type = in.readInt();
        tokenTransferTo = in.readString();
        tokenTransfer = in.readString();
        address = in.readString();
        walletAddr = in.readString();
        lastBlockNumber = in.readInt();
        chainId = in.readString();
        state = in.readInt();
    }

    @Generated(hash = 1200338274)
    public TxHistory(String pkHash, String blockHash, int blockNumber,
            String blockTimesStr, String blockGasLimit, String transactionIndex,
            String transactionFrom, String transactionTo, String value, String gas,
            String gasPrice, String cumulativeGas, int type, String tokenTransferTo,
            String tokenTransfer, String address, String walletAddr,
            int lastBlockNumber, String chainId, int state) {
        this.pkHash = pkHash;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.blockTimesStr = blockTimesStr;
        this.blockGasLimit = blockGasLimit;
        this.transactionIndex = transactionIndex;
        this.transactionFrom = transactionFrom;
        this.transactionTo = transactionTo;
        this.value = value;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.cumulativeGas = cumulativeGas;
        this.type = type;
        this.tokenTransferTo = tokenTransferTo;
        this.tokenTransfer = tokenTransfer;
        this.address = address;
        this.walletAddr = walletAddr;
        this.lastBlockNumber = lastBlockNumber;
        this.chainId = chainId;
        this.state = state;
    }

    @Generated(hash = 1772483653)
    public TxHistory() {
    }

    public static final Creator<TxHistory> CREATOR = new Creator<TxHistory>() {
        @Override
        public TxHistory createFromParcel(Parcel in) {
            return new TxHistory(in);
        }

        @Override
        public TxHistory[] newArray(int size) {
            return new TxHistory[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TxHistory) {
            return ((TxHistory) obj).pkHash.equals(this.pkHash);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.pkHash.hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pkHash);
        parcel.writeString(blockHash);
        parcel.writeInt(blockNumber);
        parcel.writeString(blockTimesStr);
        parcel.writeString(blockGasLimit);
        parcel.writeString(transactionIndex);
        parcel.writeString(transactionFrom);
        parcel.writeString(transactionTo);
        parcel.writeString(value);
        parcel.writeString(gas);
        parcel.writeString(gasPrice);
        parcel.writeString(cumulativeGas);
        parcel.writeInt(type);
        parcel.writeString(tokenTransferTo);
        parcel.writeString(tokenTransfer);
        parcel.writeString(address);
        parcel.writeString(walletAddr);
        parcel.writeInt(lastBlockNumber);
        parcel.writeString(chainId);
        parcel.writeInt(state);
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

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockTimesStr() {
        return blockTimesStr;
    }

    public void setBlockTimesStr(String blockTimesStr) {
        this.blockTimesStr = blockTimesStr;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getCumulativeGas() {
        return cumulativeGas;
    }

    public void setCumulativeGas(String cumulativeGas) {
        this.cumulativeGas = cumulativeGas;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWalletAddr() {
        return walletAddr;
    }

    public void setWalletAddr(String walletAddr) {
        this.walletAddr = walletAddr;
    }

    public int getLastBlockNumber() {
        return lastBlockNumber;
    }

    public void setLastBlockNumber(int lastBlockNumber) {
        this.lastBlockNumber = lastBlockNumber;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
