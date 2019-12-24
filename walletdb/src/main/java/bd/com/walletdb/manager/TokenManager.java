package bd.com.walletdb.manager;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import bd.com.walletdb.action.TokenAction;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.greendao.TokenEntityDao;


public class TokenManager {
    private static TokenManager manager = new TokenManager();

    private TokenManager() {
    }

    public static TokenManager getManager() {
        return manager;
    }

    /**
     * 根据合约地址获取token实体
     *
     * @param address 合约地址
     * @return token实体
     */
    public TokenEntity getTokenByAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            return null;
        }
        TokenAction action = new TokenAction();
        List<TokenEntity> tokenEntities = action.eq(TokenEntityDao.Properties.Address, address).queryAnd();
        if (tokenEntities != null && tokenEntities.size() > 0) {
            return tokenEntities.get(0);
        }
        return null;
    }

    /**
     * 根据合约地址获取token实体
     *
     * @param walletaddress 钱包地址
     * @return token实体
     */
    public TokenEntity getTokenByWalletAddress(String walletaddress) {
        if (TextUtils.isEmpty(walletaddress)) {
            return null;
        }
        TokenAction action = new TokenAction();
        List<TokenEntity> tokenEntities = action.eq(TokenEntityDao.Properties.WalletAddress, walletaddress).queryAnd();
        if (tokenEntities != null && tokenEntities.size() > 0) {
            return tokenEntities.get(0);
        }
        return null;
    }

    /**
     * 插入一个token
     *
     * @param tokenEntity
     */
    public void insertToken(TokenEntity tokenEntity) {
        if (tokenEntity == null) {
            return;
        }
        TokenAction action = new TokenAction();
        action.insertOrReplace(tokenEntity);
    }

    /**
     * 插入一批token
     *
     * @param entityList
     */
    public void insertTokenList(List<TokenEntity> entityList) {
        if (entityList == null || entityList.size() == 0) {
            return;
        }
        TokenAction action = new TokenAction();
        action.insertOrReplaceInTx(entityList);
    }

    /**
     * 根据address 删除token
     *
     * @param address
     */
    public void deleteByAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            return;
        }
        TokenAction action = new TokenAction();
        action.deleteByKey(address);
    }

    /**
     * 更新token
     *
     * @param entity
     */
    public void updateToken(TokenEntity entity) {
        if (entity == null) {
            return;
        }
        TokenAction action = new TokenAction();
        action.update(entity);
    }

    public List<TokenEntity> getAll() {
        TokenAction action = new TokenAction();
        return action.loadAll();
    }

    public List<TokenEntity> getTokenListByWalletAddr(String walletAddr) {
        TokenAction action = new TokenAction();
        List<TokenEntity> tokenEntityList = action.eq(TokenEntityDao.Properties.WalletAddress, walletAddr).queryAnd();
        return tokenEntityList;
    }

    public List<TokenEntity> getCheckedTokens(String walletAddr, String chainId) {
        TokenAction action = new TokenAction();
        List<TokenEntity> tokenEntityList = action.eq(TokenEntityDao.Properties.WalletAddress, walletAddr).eq(TokenEntityDao.Properties.ChainId, chainId).queryAnd();
        List<TokenEntity> checkedList = new ArrayList<>();
        if (tokenEntityList != null) {
            for (TokenEntity entity : tokenEntityList) {
                if (entity.getChecked()) {
                    checkedList.add(entity);
                }
            }
        }
        return checkedList;
    }

    public List<TokenEntity> getCurrentTokenList(String walletAddr, String chainId) {
        TokenAction action = new TokenAction();
        List<TokenEntity> tokenEntityList = action.eq(TokenEntityDao.Properties.WalletAddress, walletAddr).eq(TokenEntityDao.Properties.ChainId, chainId).queryAnd();

        return tokenEntityList;
    }

//    public String getContractAddressBySymbol(String symbol) {
//        if (TextUtils.isEmpty(symbol)) {
//            return "";
//        }
//        TokenAction action = new TokenAction();
//        List<TokenEntity> entityList = action.eq(TokenEntityDao.Properties.Symbol, symbol).queryAnd();
//        if (entityList != null && entityList.size() > 0) {
//            return entityList.get(0).getAddress();
//        }
//        return "";
//    }

    public String getContractAddressByAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            return "";
        }
        TokenAction action = new TokenAction();
        List<TokenEntity> entityList = action.eq(TokenEntityDao.Properties.Address, address).queryAnd();
        if (entityList != null && entityList.size() > 0) {
            return entityList.get(0).getAddress();
        }
        return "";
    }

    public void reset() {
        TokenAction action = new TokenAction();
        action.deleteAll();
    }
}
