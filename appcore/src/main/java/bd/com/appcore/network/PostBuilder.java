package bd.com.appcore.network;

import com.yanzhenjie.nohttp.RequestMethod;

public abstract class PostBuilder<T> extends CommonBuilder<T> {
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
}
