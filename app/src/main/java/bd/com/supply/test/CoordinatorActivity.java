package bd.com.supply.test;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;

public class CoordinatorActivity extends AppCompatActivity {
    private View toolBar1, toolBar2;
    private AppBarLayout appBarLayout;
    private RecyclerView rv;
    private CoordinatorLayout coordinatorLayout;
    private CommonAdapter<String> adapter;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        initView();
        initData();
    }

    private void initView() {
        toolBar1 = findViewById(R.id.toolbar1);
        toolBar2 = findViewById(R.id.toolbar2);
        appBarLayout = findViewById(R.id.adl_bar);
        rv = findViewById(R.id.rv);
        coordinatorLayout = findViewById(R.id.activity_coor);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int TotalScrollRange = appBarLayout.getTotalScrollRange();
                Log.i("CoordinatorActivity", "verticalOffset=" + verticalOffset + "   TotalScrollRange=" + TotalScrollRange);
                if (verticalOffset == 0) {//完全展开
                    toolBar1.setVisibility(View.VISIBLE);
                    toolBar2.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) == TotalScrollRange) {
                    //完全展开
                    toolBar1.setVisibility(View.GONE);
                    toolBar2.setVisibility(View.VISIBLE);
                } else {//上滑下滑
                    if (toolBar1.getVisibility() == View.VISIBLE) {
                        int alpha = 300 - 155 - Math.abs(verticalOffset);
                        Log.i("CoordinatorActivity", "alpha=" + alpha);
                        setToolBar1Alpha(alpha);
                    } else if (Math.abs(verticalOffset) > 0 && Math.abs(verticalOffset) < TotalScrollRange) {
                        toolBar1.setVisibility(View.VISIBLE);
                        toolBar2.setVisibility(View.GONE);
                        setToolBar1Alpha(255);
                    }

                    int alpha = (int) (255 * (Math.abs(verticalOffset) / 100f));
                    setToolBar2Alpha(alpha);
                }
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            datas.add("hello world");
        }
        adapter = new CommonAdapter<String>(this, R.layout.coordina_test, datas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.test_tv, s);
            }
        };
        rv.setAdapter(adapter);

    }

    private void setToolBar1Alpha(int alpha) {
        toolBar1.setAlpha(alpha);
    }

    private void setToolBar2Alpha(int alpha) {
        toolBar2.setAlpha(alpha);
    }
}
