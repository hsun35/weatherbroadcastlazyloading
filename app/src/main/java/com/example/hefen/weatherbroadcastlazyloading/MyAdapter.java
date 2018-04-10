package com.example.hefen.weatherbroadcastlazyloading;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}
class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView textViewDate;
    TextView textViewDay;
    TextView textViewHeigh;
    TextView textViewLow;
    TextView textViewText;

    public ItemViewHolder(View itemView) {
        super(itemView);
        textViewDate = itemView.findViewById(R.id.textViewDate);
        textViewDay = itemView.findViewById(R.id.textViewDay);
        textViewHeigh = itemView.findViewById(R.id.textViewHeigh);
        textViewLow = itemView.findViewById(R.id.textViewLow);
        textViewText = itemView.findViewById(R.id.textViewText);
    }
}
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Weather> weathers;
    int visibleThreshold = 1;
    int lastVisibleItem, totalItemCount;

    public MyAdapter(RecyclerView recyclerView, Activity activity, List<Weather> weathers) {
        this.activity = activity;
        this.weathers = weathers;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //Log.i("mylog", "total " + totalItemCount + " last " + lastVisibleItem +" load more");
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
        return weathers.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }
    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v  = LayoutInflater.from(activity)
                    .inflate(R.layout.weather_item_layout, parent, false);
            return new ItemViewHolder(v);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v  = LayoutInflater.from(activity)
                    .inflate(R.layout.loading_item_layout, parent, false);
            return new LoadingViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Weather weather = weathers.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.textViewDate.setText(weather.getDate());
            itemViewHolder.textViewDay.setText(weather.getDay());
            itemViewHolder.textViewHeigh.setText(weather.getHeigh());
            itemViewHolder.textViewLow.setText(weather.getLow());
            itemViewHolder.textViewText.setText(weather.getText());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

}


