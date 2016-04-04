package me.mathiasluo.page.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

import me.mathiasluo.R;
import me.mathiasluo.activity.WebActivity;
import me.mathiasluo.page.news.newsutil.RssItem;

/**
 * Created by mathiasluo on 16-3-26.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private List<RssItem> mDatas;
    private Context context;

    public NewsAdapter(List<RssItem> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RssItem mRssItem = mDatas.get(position);
        holder.textView_title.setText(mRssItem.getTitle());
        holder.textView_content.setText("    " + mRssItem.getDescription());
        holder.textView_date.setText(mRssItem.getDate());
        holder.rippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WEB_ACTIVITY_KEY, mRssItem.getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title, textView_date, textView_content;
        private MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_title = (TextView) itemView.findViewById(R.id.item_news_title);
            textView_date = (TextView) itemView.findViewById(R.id.item_news_time);
            textView_content = (TextView) itemView.findViewById(R.id.item_news_content);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
        }
    }
}
