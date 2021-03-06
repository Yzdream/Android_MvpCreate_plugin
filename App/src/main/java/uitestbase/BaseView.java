package .base;

import android.content.Context;

public interface BaseView {

    Context getContext();

    void showToast(String msg);

    void showLoading();

    void hideLoading();
}
