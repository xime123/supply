package bd.com.supply.module.common;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by 徐敏 on 2017/8/15.
 */

public class TextWatcherProxy implements TextWatcher {
    private OnEdit onEdit;

    public TextWatcherProxy(OnEdit onEdit) {
        this.onEdit = onEdit;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(onEdit!=null){
            onEdit.afterTextChanged(s);
        }
    }

    public interface OnEdit{
        void afterTextChanged(Editable s);
    }
}
