package me.mathiasluo.page.location;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.base.BaseListAdapter;
import me.mathiasluo.model.Location;

public class LocationListAdapter<ListItem extends Location> extends BaseListAdapter<ListItem> {

    private int pics[] = {
            R.mipmap.earhart,
            R.mipmap.ford,
            R.mipmap.hillenbrand,
            R.mipmap.wiley,
            R.mipmap.windsor
    };

    public LocationListAdapter(List<ListItem> dataSet, OnClickListener<ListItem> onClickListener) {
        super(dataSet, onClickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;

            Location loc = dataSet.get(position);

            vh.parentView.setId(loc.hashCode());

            // set card title
            vh.title.setText(loc.getFullName());

            // set card status
            boolean isTwentyFourHourFormat = DateFormat.is24HourFormat(vh.parentView.getContext());
            String status = loc.getTimings(isTwentyFourHourFormat);
            if (status != null && !loc.getTimings(isTwentyFourHourFormat).equals("Closed")) {
                vh.status.setText(Html.fromHtml(status));
                vh.status.setTextColor(vh.status.getResources().getColor(R.color.secondary_text));
            } else {
                vh.status.setText(R.string.closed);
                vh.status.setTextColor(vh.status.getResources().getColor(R.color.closed));
            }

            // set card image
          /*  Glide.with(vh.image.getContext())
                   *//* .load(DiningServiceHelper.getFileUrl(loc.getTileImage()))*//*
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(vh.image);*/
            vh.image.setImageResource(pics[position]);
        }
    }

    public static class ViewHolder extends BaseListAdapter.ViewHolder {
        @InjectView(R.id.title)
        public TextView title;

        @InjectView(R.id.status)
        public TextView status;

        @InjectView(R.id.image)
        public ImageView image;

        public ViewHolder(View parentView) {
            super(parentView);
        }
    }

}