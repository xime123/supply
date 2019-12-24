package bd.com.appcore.ui.fragment;

import android.app.Activity;

import java.util.concurrent.TimeUnit;

import bd.com.appcore.R;
import bd.com.appcore.ui.view.LoadingDialog;
import bd.com.appcore.ui.view.ToastDialog;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public abstract class BaseDialogFragment extends BaseFragment{
    //handler code
    public static final int DISMISS_DIALOG = 1;
    public static final int DIALOG_SHOW_TIME = 2 * 1000;

    private ToastDialog customerDialog;
    /**
     * 正确
     * @param content    内容
     * @param cancelable 是否可取消
     */
    public  void showSuccessToast(final String content, final String contentDes, final boolean cancelable) {
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        startToast(content,contentDes,cancelable);
                    }
                });
    }

    /**
     * 弹出框
     * @param content
     * @param contentDes
     * @param cancelable
     */
    private void startToast(final String content, final String contentDes, final boolean cancelable){
        showDialog(getActivity(),content,contentDes,R.mipmap.complete,false,cancelable);
    }
    /**
     * 错误
     * @param content    内容
     * @param cancelable 是否可取消
     */
    public  void showErrorToast( String content, String contentDes, boolean cancelable) {
        showDialog(getActivity(),content,contentDes, R.mipmap.error,false,cancelable);
    }


    /**
     * 警告
     * @param content    内容
     * @param cancelable 是否可取消
     */
    public  void showWarningToast( String content, String contentDes, boolean cancelable) {
        dismissLoading();
        showDialog(getActivity(),content,contentDes,R.mipmap.hint,false,cancelable);
    }

    /**
     * 加载loading
     * @param content    内容
     * @param cancelable 是否可取消
     */
    public void showLoading(String content, String contentDes, boolean cancelable) {
        showDialog(getActivity(),content,contentDes,R.mipmap.hint,true,cancelable);
    }


    protected   void  dismissDialog() {
        if (customerDialog != null) {
            if (customerDialog.isShowing()) {
                customerDialog.dismiss();
            }
            customerDialog=null;
        }
    }


    private   void showDialog(Activity activity, String parameter, String des, int resID, boolean isLoading, boolean cancelable) {
        dismissDialog();
        if (activity != null) {
            customerDialog = new ToastDialog(activity, R.style.DialogTranslucentNoTitle);
            customerDialog.showLoadingDialog(parameter, des,resID, isLoading,cancelable);
        } else {
            if(customerDialog.isShowing()){
                customerDialog.dismiss();
            }
            customerDialog.show();
        }
    }

    public void dismissLoading() {
        dismissDialog();
        LoadingDialog.cancleDialog();
    }

    /**
     * 加载loading
     */
    public void showDefaultLoading() {
        LoadingDialog.loadingDialog(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dismissDialog();
    }

}
