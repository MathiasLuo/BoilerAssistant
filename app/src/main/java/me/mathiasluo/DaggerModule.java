package me.mathiasluo;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import me.mathiasluo.activity.DiningSettingActivity;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.page.location.dining.DiningItemActivity;
import me.mathiasluo.page.location.dining.DiningItemPresenter;
import me.mathiasluo.page.location.dining.DiningLocationListPresenter;
import me.mathiasluo.page.location.dining.DiningMenuActivity;
import me.mathiasluo.page.location.dining.DiningMenuPresenter;
import me.mathiasluo.page.location.retail.RetailInfoActivity;
import me.mathiasluo.page.location.retail.RetailLocationListPresenter;
import me.mathiasluo.service.DiningService;
import me.mathiasluo.service.DiningServiceHelper;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                MainActivity.class,
                DiningMenuActivity.class,
                DiningItemActivity.class,
                RetailInfoActivity.class,
                DiningSettingActivity.class,
                DiningLocationListPresenter.class,
                RetailLocationListPresenter.class,
                DiningMenuPresenter.class,
                DiningItemPresenter.class,
        }
)
public class DaggerModule {

    private static ObjectGraph graph;

    public static ObjectGraph getObjectGraph() {
        if (graph == null) {
            graph = ObjectGraph.create(new DaggerModule());
        }
        return graph;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }

    @Provides
    @Singleton
    Converter provideConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Converter converter) {
        return new RestAdapter.Builder()
                .setEndpoint(DiningServiceHelper.getEndpoint())
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setConverter(converter)
                .build();
    }

    @Provides
    @Singleton
    DiningService provideDiningService(RestAdapter adapter) {
        return adapter.create(DiningService.class);
    }

    @Provides
    Handler provideHandler() {
        return new Handler();
    }
}