package me.mathiasluo.page.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.page.news.newsutil.RssFeedTask;
import me.mathiasluo.page.news.newsutil.RssItem;

/**
 * Created by mathiasluo on 16-3-26.
 */
public class SingleNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String BUNDLE_KEY = "NEWS_INDEX";
    private int mKey;


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @InjectView(R.id.loading_progress)
    ProgressBar progressBar;

    private NewsAdapter adapter;
    private List<RssItem> currenResults;

    public SingleNewsFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKey = getArguments().getInt(BUNDLE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_news, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        if (adapter == null) showProgressBar();
        refresh.setColorSchemeColors(R.color.open);
        refresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
    }

    public void loadData() {
        RssFeedTask task = new RssFeedTask(getContext(), new RssFeedTask.RssFeedResultListener() {
            @Override
            public void onRssResult(List<RssItem> results, Exception ex) {
                currenResults = results;
                if (adapter == null) {
                    colosProgressBar();
                    adapter = new NewsAdapter(currenResults, getParentFragment().getActivity());
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                refresh.setRefreshing(false);
            }
        });
        task.execute(NewsFragment.tabList.get(mKey).url());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        loadData();
    }


    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void colosProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


}
