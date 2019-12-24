package bd.com.supply.web3;


public class TransactionManager {
    private static TransactionManager transactionManager=new TransactionManager();
    private TransactionManager(){};
    public static TransactionManager getTransactionManager(){
        return transactionManager;
    }

}
