package bd.com.appcore.mvp;

/**
 * Created by guotingzhu@qq.cn on 2017/7/26.
 */

public interface IBaseView extends IView {

    void showLoadingDialog();

    void hideLoadingDialog();

    void exit();

}
