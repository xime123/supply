package bd.com.appcore.mvp;

import android.util.Log;





public class BasePresenterImpl<V extends IBaseView> extends BasePresenter<V> {
    public static final String TAG = "BasePresenterImpl";



    public void onAttachView(V view) {
        super.onAttachView(view);
    }


    public void onDetachView() {
        super.onDetachView();
        Log.e(TAG, " Activity onDestroy ,  执行onDetachView() mView = null ");
    }


    public void showLoading(){
        if(mView!=null){
            mView.showLoadingDialog();
        }
    }

    public void dismissLoading(){
        if(mView!=null){
            mView.hideLoadingDialog();
        }
    }
}
