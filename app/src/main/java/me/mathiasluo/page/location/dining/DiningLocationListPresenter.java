package me.mathiasluo.page.location.dining;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import javax.inject.Inject;

import me.mathiasluo.DaggerModule;
import me.mathiasluo.model.dining.DiningLocation;
import me.mathiasluo.model.dining.DiningLocations;
import me.mathiasluo.service.DiningService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DiningLocationListPresenter extends MvpBasePresenter<MvpLceView<List<DiningLocation>>> {

    @Inject
    DiningService service;

    private boolean isFirst = true;

    public DiningLocationListPresenter() {
        DaggerModule.getObjectGraph().inject(this);
    }

    public void loadData(final boolean pullToRefresh) {
        if (isViewAttached() && isFirst) {
            getView().showLoading(pullToRefresh);
            isFirst = false;
        }
        service.getDiningLocations(new Callback<DiningLocations>() {
            @Override
            public void success(DiningLocations diningLocations, Response response) {
                if (isViewAttached()) {
                    getView().setData(diningLocations.getLocations());
                    getView().showContent();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (isViewAttached()) {
                    getView().showError(error, pullToRefresh);
                }
            }
        });
    }
}
