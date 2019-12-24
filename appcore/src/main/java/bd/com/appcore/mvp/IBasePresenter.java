package bd.com.appcore.mvp;



/**
 * Created by guotingzhu@qq.cn on 2017/7/26.
 */

public interface IBasePresenter<V extends IView> {

    void onAttachView(V view);

    void onDetachView();
}
