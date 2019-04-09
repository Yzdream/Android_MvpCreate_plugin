package .ui.test;

/**
 * 残梦
 * Created by dell on 2018/09/10
 */
public class TestPresenter implements TestContract.Presenter{

    private TestContract.View mView;

    public TestPresenter(TestContract.View view) {
        this.mView = view;
    }

    @Override
    public void onDestroy() {

    }

}
