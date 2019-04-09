package .base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 残梦
 * Created by dell on 2018/09/10
 */

public abstract class BaseFragment<P> extends Fragment {

    protected P mPresenter;
    protected View mRoot;
    protected Unbinder mRootUnBinder;
    // 标示是否第一次初始化数据
    protected boolean mIsFirstInitData = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化参�?
        initArgs(getArguments());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的跟布�?，但是不在创建时就添加到container里边
            View root = inflater.inflate(layId, container, false);
            initView(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                // 把当前Root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            // 触发�?次以后就不会触发
            mIsFirstInitData = false;
            // 触发
            onFirstInit();
        }

        // 当View创建完成后初始化数据
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRootUnBinder.unbind();
        doOnFinish();
    }

    /**
     * 实例化P�?
     * @return Presenter
     */
    protected abstract P createPresenter();

    /**
     * 初始化相关参�?
     */
    protected void initArgs(Bundle bundle) {
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 初始化控�?
     */
    protected void initView(View root) {
        mRootUnBinder = ButterKnife.bind(this, root);
        mPresenter = createPresenter();
    }

    /**
     * 初始化数�?
     */
    protected void initData() {

    }

    /**
     * 当首次初始化数据的时候会调用的方�?
     */
    protected void onFirstInit() {

    }

    /**
     * 当销毁的时�?�使�?
     */
    protected void doOnFinish() {

    }

    public void showLoading(){

    }

    public void hideLoading(){

    }

   public void showToast(String msg) {

    }

    /**
     * 创建fragment的静态方法，方便传�?�参�?
     *
     * @param args 传�?�的参数
     * @return Fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static <T extends Fragment> T newInstance(Class clazz, Bundle args) {
        T mFragment = null;
        try {
            mFragment = (T) clazz.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert mFragment != null;
        mFragment.setArguments(args);
        return mFragment;
    }
}
