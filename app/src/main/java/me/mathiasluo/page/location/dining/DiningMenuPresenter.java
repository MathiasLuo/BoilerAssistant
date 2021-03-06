package me.mathiasluo.page.location.dining;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.Calendar;

import javax.inject.Inject;

import me.mathiasluo.DaggerModule;
import me.mathiasluo.model.dining.DayMenu;
import me.mathiasluo.model.dining.DiningLocation;
import me.mathiasluo.page.location.LocationClosedError;
import me.mathiasluo.service.DiningService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DiningMenuPresenter extends MvpBasePresenter<MvpLceView<DayMenu>> {

    @Inject
    DiningService service;

    public DiningMenuPresenter() {
        DaggerModule.getObjectGraph().inject(this);
    }

    public void loadData(DiningLocation location, final boolean pullToRefresh) {
        if (isViewAttached())
            getView().showLoading(pullToRefresh);

        String date = String.format("%1$tm-%1$td-%1$tY", Calendar.getInstance());

        service.getDiningMenu(location.getName(), date, new Callback<DayMenu>() {
            @Override
            public void success(DayMenu dayMenu, Response response) {
                if (dayMenu.getOpenMeals().size() <= 0) {
                    failure(new LocationClosedError());
                } else if (isViewAttached()) {
                    getView().setData(dayMenu);
                    getView().showContent();
                }
            }

            private void failure(Throwable t) {
                if (isViewAttached()) {
                    getView().showError(t, pullToRefresh);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                failure((Throwable) error);
            }
        });
    }

}
