package com.mark.moveandswipervitem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mark.moveandswipervitem.image.ImageLoader;
import com.mark.moveandswipervitem.bean.ImageModel;
import com.mark.moveandswipervitem.R;

import java.util.List;

/**
 * Created by mark on 18-8-11.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    static final String TAG = "ItemAdapter";
    Context mContext;
    List<ImageModel> mModels;
    int width = 340;
    public ItemAdapter(){}

    public ItemAdapter(Context context, List<ImageModel> models,int columns){
        this.mContext = context;
        this.mModels = models;
        //width = (context.getResources().getDisplayMetrics().widthPixels - 20*columns ) / columns;
        width = context.getResources().getDisplayMetrics().widthPixels / columns;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_image_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageModel model = mModels.get(position);

        int imageWidth = model.getImageWidth();
        int imageHeight = model.getImageHeight();
        int height = width * imageHeight / imageWidth;
        Log.e(TAG,"WH : " + width + " : " + height);
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        holder.imageView.setLayoutParams(layoutParams);

        ImageLoader.displayRoundImage(model.getImagePath(),holder.imageView,width,height);

        Log.e(TAG,"PATH : " + model.getImagePath());
    }

    @Override
    public int getItemCount() {
        return mModels != null ? mModels.size() : 0;
    }

    static class ViewHolder  extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumb);
        }
    }
}
