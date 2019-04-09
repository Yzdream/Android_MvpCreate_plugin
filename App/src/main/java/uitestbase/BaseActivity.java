package .base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 残梦
 * Created by dell on 2018/09/10
 */

public abstract class BaseActivity<P> extends AppCompatActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始�?
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面
            int layId = getContentLayoutId();
            setContentView(layId);
            initBefore();
            initView(savedInstanceState);
            initData();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doOnDestroy();
    }

    /**
     * 界面�?毁时可以调用
     */
    protected void doOnDestroy() {

    }

    /**
     * 初始化控件调用之�?
     */
    protected void initBefore() {
    }

    /**
     * init Presenter
     * @return Presenter
     */
    protected abstract P createPresenter();

    /**
     *init Widows
     */
    protected void initWidows() {

    }

    /**
     * 初始化相关参�?
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mPresenter = createPresenter();
    }

    protected void initData() {

    }

    public void showLoading(){

    }

    public void hideLoading(){

    }

   public void showToast(String msg) {

    }


}
