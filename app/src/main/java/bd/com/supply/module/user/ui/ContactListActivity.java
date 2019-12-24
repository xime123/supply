package bd.com.supply.module.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;
import bd.com.supply.main.OnMenuClickedEvent;
import bd.com.supply.module.test.TestActivity;
import bd.com.supply.module.user.bus.AddContactEvent;
import bd.com.walletdb.entity.ContactEntity;
import bd.com.supply.module.user.presenter.ContactListPresenter;
import de.greenrobot.event.EventBus;

public class ContactListActivity extends BaseListActivity<ContactListPresenter, ContactListPresenter.View, ContactEntity> implements ContactListPresenter.View {
    private int intentType;//默认0，item点击事件为编辑。1，为选择联系人

    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        mPresenter.getContactList();
    }

    @Override
    protected void initData() {
        super.initData();
        intentType = getIntent().getIntExtra(IntentKey.CONTACT_LIST_INTENT_TYPE, 0);
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("地址本");
        actionBar.setMenu2Resource(R.mipmap.add_gray);
        actionBar.setOnMenu2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
                //Intent intent = new Intent(ContactListActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected CommonAdapter<ContactEntity> createAdapter() {
        return new CommonAdapter<ContactEntity>(this, R.layout.item_contact_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, ContactEntity contactEntity, int position) {
                holder.setImageResource(R.id.contact_item_iv, R.mipmap.vec_icon);
                holder.setText(R.id.contact_name, contactEntity.getName());
                holder.setText(R.id.contact_address_tv, contactEntity.getAddress());
                holder.setText(R.id.contact_remark_tv, TextUtils.isEmpty(contactEntity.getRemark()) ? "" : contactEntity.getRemark());
            }
        };
    }

    @Override
    protected ContactListPresenter initPresenter() {
        return new ContactListPresenter();
    }

    @Override
    protected ContactListPresenter.View initView() {
        return this;
    }


    @Override
    protected void onItemViewClick(RecyclerView.ViewHolder vh, ContactEntity entity) {
        super.onItemViewClick(vh, entity);
        if (intentType == 1) {
            Intent intent=new Intent();
            intent.putExtra(IntentKey.CONTACT_ENTITY,entity);
            setResult(100,intent);
            finish();
        } else {
            gotoEditContact(entity);
        }
    }

    private void gotoEditContact(ContactEntity entity) {
        Intent intent = new Intent(this, EditContactActivity.class);
        intent.putExtra(IntentKey.CONTACT_ENTITY, entity);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 100) {
            ContactEntity entity=data.getParcelableExtra(IntentKey.CONTACT_ENTITY);
            if(entity!=null){
                datas.remove(entity);
                mAdapter.notifyDataSetChanged();
                if(datas.size()==0){
                    showEmptyView();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(AddContactEvent event) {
        mRecyclerView.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
