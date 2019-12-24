package bd.com.appupdate.util;


import com.liulishuo.filedownloader.BaseDownloadTask;

public interface DownLoadCallBack {
    void pending(BaseDownloadTask task, long soFarBytes, long totalBytes);

    void progress(BaseDownloadTask task, long soFarBytes, long totalBytes);


    void paused(BaseDownloadTask task, long soFarBytes, long totalBytes);

    void completed(BaseDownloadTask task);

    void error(BaseDownloadTask task, Throwable e);

    void warn(BaseDownloadTask task);
}
