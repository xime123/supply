package bd.com.supply.module.wallet.model.domain;

public class SugGasResp {
    public SugGas data;

    public class SugGas {
        private String sugGasPrice;

        public String getSugGasPrice() {
            return sugGasPrice;
        }

        public void setSugGasPrice(String sugGasPrice) {
            this.sugGasPrice = sugGasPrice;
        }
    }

}
