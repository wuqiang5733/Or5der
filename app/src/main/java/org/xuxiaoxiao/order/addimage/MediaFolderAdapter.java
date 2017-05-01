package org.xuxiaoxiao.order.addimage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xuxiaoxiao.order.R;

import java.util.ArrayList;

/**
 * Created by WuQiang on 2017/4/30.
 */

public class MediaFolderAdapter extends RecyclerView.Adapter<MediaFolderAdapter.MediaViewHolder> {
    ArrayList<MediaFolderModel> mediaFolderModels = new ArrayList<>();

    Context context;

    public MediaFolderAdapter(Context context, ArrayList<MediaFolderModel> mediaFolderModels) {
        this.mediaFolderModels = mediaFolderModels;
        this.context = context;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_floder, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        String mediaFolderName = mediaFolderModels.get(position).getFolderName();
        holder.folderName.setText(mediaFolderName);
        holder.itemSum.setText(String.valueOf(mediaFolderModels.get(position).getMediaItemSum()));

        Glide.with(context).load("file://" + mediaFolderModels.get(position).getFirstItemImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.folderImage);
            holder.bind(mediaFolderName);
    }

    @Override
    public int getItemCount() {
        return mediaFolderModels.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView folderImage;
        TextView folderName;
        TextView itemSum;
        String mediaFolderName;

        public MediaViewHolder(View itemView) {
            super(itemView);
            folderImage = (ImageView) itemView.findViewById(R.id.media_floder_image_view);
            folderName = (TextView) itemView.findViewById(R.id.media_folder_name_text_view);
            itemSum = (TextView)itemView.findViewById(R.id.media_item_sum_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = MediaItemActiviey.newIntent(context,mediaFolderName);
//            context.startActivity(intent);
        }


        public void bind(String mediaFolderName) {
            this.mediaFolderName = mediaFolderName;
//            Log.d("WQWQ",mediaFolderName);
        }
    }
}
