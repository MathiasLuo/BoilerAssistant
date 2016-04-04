package me.mathiasluo.page.location.dining;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.hannesdorfmann.fragmentargs.annotation.FragmentArgsInherited;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.activity.DiningSettingActivity;
import me.mathiasluo.base.BaseListAdapter;
import me.mathiasluo.base.MainLceFragment;
import me.mathiasluo.model.dining.DiningLocation;
import me.mathiasluo.page.location.LocationListAdapter;

@FragmentArgsInherited
public class DiningLocationListFragment extends MainLceFragment<RecyclerView, List<DiningLocation>, MvpLceView<List<DiningLocation>>, DiningLocationListPresenter> implements BaseListAdapter.OnClickListener<DiningLocation>, View.OnClickListener {

    LocationListAdapter<DiningLocation> adapter;

    @InjectView(R.id.fab)
    public FloatingActionButton fab;
    @InjectView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new LocationListAdapter<>(new ArrayList<DiningLocation>(), this);
        contentView.setAdapter(adapter);
        contentView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loadData(false);
    }

    @Override
    public DiningLocationListPresenter createPresenter() {
        return new DiningLocationListPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_location_list;
    }

    @Override
    public void setData(List<DiningLocation> data) {
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        adapter.setDataSet(data);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setOnClickListener(this);
        refreshLayout.setColorSchemeColors(R.color.open);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });


    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadData(pullToRefresh);
    }

    @Override
    public void onClick(DiningLocation location) {
        Context ctx = getActivity();
        startActivity(new DiningMenuActivityIntentBuilder(location).build(ctx));
    }

    @Override
    public LceViewState<List<DiningLocation>, MvpLceView<List<DiningLocation>>> createViewState() {
        return new CastedArrayListLceViewState<>();
    }

    @Override
    public List<DiningLocation> getData() {
        return adapter.getDataSet();
    }

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        switch (viewID) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), DiningSettingActivity.class));
                break;
        }
    }
}

