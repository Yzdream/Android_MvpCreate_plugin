package ;

import .base.BaseActivity;
import android.content.Context;
import $rpackagename.R;

public class TestActivity extends BaseActivity<TestPresenter> implements TestContract.View{

       @Override
       protected TestPresenter createPresenter() {
           return new TestPresenter(this);
       }

       @Override
       protected int getContentLayoutId() {
            return R.layout.activity_test;
       }

       @Override
       public Context getContext() {
           return this;
       }

       @Override
       protected void doOnDestroy() {
           super.doOnDestroy();
           mPresenter.onDestroy();
        }

}