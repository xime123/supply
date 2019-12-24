package bd.com.supply.module.transaction.presenter;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.supply.module.transaction.model.domian.SoSoBean;
import bd.com.supply.module.transaction.model.domian.SoSoInnerItemBean;

public class ProducePresenter extends BasePresenterImpl<ProducePresenter.View> {
    public interface View extends IListView<SoSoInnerItemBean> {

    }
}
