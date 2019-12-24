package bd.com.supply.module.test;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.mvp.IListView;

public class TestPresenter extends BasePresenterImpl<TestPresenter.View> {
    public void getDatas() {
        List<TestData> dataList = new ArrayList<>();
        TestData title = new TestData();
        title.setViewType(1);
        title.setTitle("未添加路由器前，以下设备可以直接添加，但部分设备的功能将受限");
        dataList.add(title);
        for (int i = 0; i < 2; i++) {
            TestData testData = new TestData();
            testData.setViewType(2);
            testData.setTitle("tile");
            testData.setContent("测试数据" + i);
            dataList.add(testData);
        }
        TestData more = new TestData();
        more.setViewType(1);
        more.setTitle("部分设备需要添加路由器后才能支持，点击展开");
        dataList.add(more);
        mView.loadSuccess(dataList);
    }


    public List<TestData> getMore(){
        List<TestData> dataList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TestData testData = new TestData();
            testData.setViewType(2);
            testData.setContent("测试数据" + i);
            dataList.add(testData);
        }
        return dataList;
    }

    public interface View extends IListView<TestData> {

    }
}
