package org.xuxiaoxiao.order.addimage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xuxiaoxiao.order.R;

import java.util.ArrayList;

/**
 * Created by WuQiang on 2017/4/30.
 */

public class MediaItemAdapter extends RecyclerView.Adapter <MediaItemAdapter.MediaItemViewHolder>{
    Context context;
    ArrayList<String> mediaItems = new ArrayList<>();
    boolean[] thumbnailsselection;
//      this.thumbnailsselection = new boolean[this.count];  // CheckBox

    public MediaItemAdapter(Context context, ArrayList<String> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
        thumbnailsselection = new boolean[mediaItems.size()];
    }

    @Override
    public MediaItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);

        return new MediaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaItemViewHolder holder, int position) {
//        holder.imagePath.setText(mediaItems.get(position));
        holder.itemCheckBox.setId(position);
        String itemPath =  mediaItems.get(position);
        Glide.with(context).load("file://" + itemPath)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imageItem);
        holder.bind(itemPath);

        holder.itemCheckBox.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox cb = (CheckBox) v;
                int id = cb.getId();
                if (thumbnailsselection[id]) {
                    cb.setChecked(false);
                    thumbnailsselection[id] = false;
                } else {
                    cb.setChecked(true);
                    thumbnailsselection[id] = true;
                }
            }
        });
        holder.itemCheckBox.setChecked(thumbnailsselection[position]);

    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public class MediaItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        ImageView imageItem;
        CheckBox itemCheckBox;
        String itemPath;
        //        TextView imagePath;
        public MediaItemViewHolder(View itemView) {
            super(itemView);
            imageItem = (ImageView)itemView.findViewById(R.id.image_item);
            itemCheckBox = (CheckBox)itemView.findViewById(R.id.image_item_check_box);
//            imagePath = (TextView)itemView.findViewById(R.id.item_path_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + itemPath), "image/*");
            context.startActivity(intent);
        }

        public void bind(String itemPath) {
            this.itemPath = itemPath;
        }
    }
}