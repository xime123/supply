package bd.com.appcore.mvp;

import java.util.List;

/**
 * author:     labixiaoxin
 * date:       2018/5/26
 * email:      labixiaoxin2@qq.cn
 */
public interface IListView<T> extends IBaseView {
    void loadFailed(String msg);
    void loadSuccess(List<T> items);
    void loadMoreSuccess(List<T>moreDatas);
    void loadEmpty();
    void loadMoreFailed();
}
