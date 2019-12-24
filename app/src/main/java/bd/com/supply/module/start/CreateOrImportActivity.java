package bd.com.supply.module.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import bd.com.appcore.ui.activity.BaseCoreActivity;
import bd.com.supply.R;
import bd.com.supply.module.wallet.ui.CreateWalletActivity;
import bd.com.supply.module.wallet.ui.ImportWalletActivity;

public class CreateOrImportActivity extends BaseCoreActivity {
    private TextView createTv;
    private TextView importTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immerse();
        setContentView(R.layout.activity_create_activity);
        initView();
    }

    private void initView() {
        createTv = (TextView) findViewById(R.id.create_tv);
        importTv = findViewById(R.id.import_tv);
        createTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrImportActivity.this, CreateWalletActivity.class);
                startActivity(intent);
            }
        });

        importTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrImportActivity.this, ImportWalletActivity.class);
                startActivity(intent);
            }
        });
    }
}
