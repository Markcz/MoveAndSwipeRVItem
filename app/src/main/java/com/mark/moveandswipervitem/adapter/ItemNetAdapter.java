package com.mark.moveandswipervitem.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mark.moveandswipervitem.R;
import com.mark.moveandswipervitem.bean.ImageModel;
import com.mark.moveandswipervitem.image.BitmapUtil;
import com.mark.moveandswipervitem.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 18-8-11.
 */
public class ItemNetAdapter extends RecyclerView.Adapter<ItemNetAdapter.ViewHolder> {

    static final String TAG = "ItemAdapter";
    Context mContext;
    List<String> mUrls;
    int width = 340;
    List<Integer> mHeights = new ArrayList<>();
    public ItemNetAdapter(){}

    public ItemNetAdapter(Context context, List<String> models, int columns){
        this.mContext = context;
        this.mUrls = models;
        //width = (context.getResources().getDisplayMetrics().widthPixels - 20*columns ) / columns;
        width = context.getResources().getDisplayMetrics().widthPixels / columns;
        initHeights();
    }

    private void initHeights() {
        for (int i = 0; i < mUrls.size(); i++){
            mHeights.add((int) (Math.random()*100 + 400));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_image_net_item,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String url = mUrls.get(position);
        final int height = mHeights.get(position);
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        holder.imageView.setLayoutParams(layoutParams);
        ImageLoader.displayRoundImage(url, holder.imageView, true);

    }

    @Override
    public int getItemCount() {
        return mUrls != null ? mUrls.size() : 0;
    }

    static class ViewHolder  extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumb);
        }
    }
}
