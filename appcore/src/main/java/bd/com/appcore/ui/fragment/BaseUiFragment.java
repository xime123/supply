package bd.com.appcore.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bd.com.appcore.R;
import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.ui.view.CommonActionBar;
import bd.com.appcore.ui.view.LoadingDialog;


public abstract class BaseUiFragment<P extends IBasePresenter<V>,V extends IBaseView> extends BaseDialogFragment implements IBaseView {
    /** 标题栏的控件统一在这里初始化，并由上层自定义修改*/
    protected CommonActionBar actionBar;
    protected ViewGroup containerView;
    protected ViewGroup bottomContainer;
    protected ViewGroup topContaniner;
    //页面发生错误时，展示的统一错误页面
    protected View errorView;
    //页面数据为空时，展示的统一为空页面
    protected View emptyView;
    protected P mPresenter;

    protected V view;

    protected abstract P initPresenter();

    protected abstract V initView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter =initPresenter();
        if (mPresenter != null && view != null) {
            mPresenter.onAttachView(view);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup baseRootView = (ViewGroup) inflater.inflate(R.layout.activity_base,null);
        View contentView =  inflater.inflate(getLayoutId(), null);
        containerView=(ViewGroup) baseRootView.findViewById(R.id.container);
        bottomContainer=baseRootView.findViewById(R.id.bottom_container);
        topContaniner=baseRootView.findViewById(R.id.top_container);
        containerView.addView(contentView);
        initTooBar(baseRootView);
        initContentView(baseRootView);
        //初始化布局
        view = initView();
        if(view!=null) {
            mPresenter.onAttachView(view);
        }
        return baseRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        setListener();
        initData();
    }

    protected void initContentView(ViewGroup contentView){

    }

    protected void initTooBar(ViewGroup rootView) {
        actionBar = (CommonActionBar) rootView.findViewById(R.id.action_bar);
        actionBar.setBackgroundResource(R.color.cyan_normal);
        setTitle("");
    }

    /**
     * 设置标题
     * @param text
     */
    protected void setTitle(String text) {
        actionBar.setTitle(text);
    }

    protected void setListener(){

    }

    @Override
    public void onDetach() {
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
        hideLoadingDialog();
        super.onDetach();
    }


    protected abstract int getLayoutId();

    @Override
    public void showLoadingDialog() {
        showLoading( true);
    }

    @Override
    public void hideLoadingDialog() {
        showLoading(false);
    }

    public void showLoading(boolean show) {
        if(show){
            showDefaultLoading();
        }else {
            LoadingDialog.cancleDialog();
            dismissDialog();
        }
    }

    /**
     * 显示加载失败页面
     */
    protected void showErrorView() {
        //先移除空view
       // removeExceptionView();
        if(errorView!=null){
            errorView.setVisibility(View.VISIBLE);
            return;
        }
        errorView = View.inflate(getActivity(), R.layout.error_layout, null);
        containerView.addView(errorView);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErroViewClicked();
            }
        });
    }

    /**
     * 移除加载异常页面
     */
    protected void removeExceptionView() {
        if(errorView!=null){
            errorView.setVisibility(View.GONE);
        }
        if(emptyView!=null){
            emptyView.setVisibility(View.GONE);
        }
//        containerView.removeView(errorView);
//        containerView.removeView(emptyView);
    }

    /**
     * 显示加载数据为空页面
     */
    protected void showEmptyView() {
        //先remove掉errorView
       // removeExceptionView();
        if(emptyView!=null){
            emptyView.setVisibility(View.VISIBLE);
            return;
        }
        emptyView = View.inflate(getActivity(), R.layout.empty_layout, null);
        containerView.addView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂时屏蔽，根据产品需求是否放开
                onErroViewClicked();
            }
        });
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }

    /**
     * 暴露给上层，errorView点击后 应该去重新加载页面逻辑
     */
    protected void onErroViewClicked(){

    }



    /**
     * 一些初始化操作在这里执行
     */
    protected void initData() {
    }

    @Override
    public void exit() {
        getActivity().finish();
    }
}
