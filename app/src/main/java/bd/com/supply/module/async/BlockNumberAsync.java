package bd.com.supply.module.async;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bd.com.supply.app.ThreadManager;
import bd.com.supply.web3.Web3Proxy;

public class BlockNumberAsync {
    private Timer timer = new Timer(true);
    private TimerTask task;
    private List<OnBlockNumberListener> listeners = new ArrayList<>();

    private BlockNumberAsync() {
    }

    ;
    private static BlockNumberAsync async;

    public static BlockNumberAsync getAsync() {
        if (async == null) {
            synchronized (BlockNumberAsync.class) {
                if (async == null) {
                    async = new BlockNumberAsync();
                }
            }
        }
        return async;
    }

    public interface OnBlockNumberListener {
        void onBlockNumber(long blockNum);
    }

    public void addListener(OnBlockNumberListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(OnBlockNumberListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void init() {
        timer = new Timer(true);
        task = new GetBlockNumTask();
        timer.schedule(task, 0, 10000);
    }

    class GetBlockNumTask extends TimerTask {

        @Override
        public void run() {
            try {
                final int lastBlockNum = Web3Proxy.getWeb3Proxy().getEth_BlockNumber();
                Log.e("getBlockNum", "Timer getLastBlockNum =" + lastBlockNum);
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        notifyAllListener(lastBlockNum);

                    }
                });

            } catch (Exception e) {
                Log.e("getBlockNum", "Timer getLastBlockNum error e=" + e.getMessage());
            }
        }
    }

    private void notifyAllListener(int blockNum) {
        for (OnBlockNumberListener listener : listeners) {
            listener.onBlockNumber(blockNum);
        }
    }
}
