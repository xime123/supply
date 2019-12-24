package bd.com.walletdb.manager;

import java.util.List;

import bd.com.walletdb.action.ChainAction;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.greendao.ChainEntityDao;

public class ChainManager {
    private static ChainManager manager = new ChainManager();

    private ChainManager() {
    }

    public static ChainManager getManager() {
        return manager;
    }

    public void inputChainList(List<ChainEntity> chainEntityList) {
        ChainAction action = new ChainAction();
        action.insertOrReplaceInTx(chainEntityList);
    }

    public ChainEntity findChainById(String chainId) {
        ChainAction action = new ChainAction();
        List<ChainEntity> chainEntities = action.eq(ChainEntityDao.Properties.ChainId, chainId).queryAnd();
        if (chainEntities != null && chainEntities.size() > 0) {
            return chainEntities.get(0);
        }
        return null;
    }

    public List<ChainEntity> getChainList() {
        ChainAction action = new ChainAction();
        return action.loadAll();
    }
}
