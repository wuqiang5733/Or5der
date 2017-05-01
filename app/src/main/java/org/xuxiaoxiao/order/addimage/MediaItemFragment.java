package org.xuxiaoxiao.order.addimage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xuxiaoxiao.order.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by WuQiang on 2017/4/30.
 */

public class MediaItemFragment extends Fragment {
    private static final String IMAGE_PATH = "org.xuxiaoxiao.mediaitemfragment.image.name";
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    MediaItemAdapter mediaItemAdapter;
    Button sendItemButton;
    String mediaFolderName;
    int lastSelectItem = -1;
    private List<String> mediaItems =
            Collections.synchronizedList(new ArrayList<String>());
    //    ArrayList<String> mediaItems;
//    boolean[] thumbnailsselection;
    private static final String MEDIA_FOLDER_NAME =
            "org.xuxiaoxiao.MediaFolderName";

    public static MediaItemFragment newInstance(String mediaFolderName) {
        // 这个方法接收来自 Activity 的数据
        Bundle args = new Bundle();
        args.putString(MEDIA_FOLDER_NAME, mediaFolderName);

        MediaItemFragment fragment = new MediaItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaFolderName = getArguments().getString(MEDIA_FOLDER_NAME);
//        Log.d("WQWQ",mediaFolderName);
        mediaItemAdapter = new MediaItemAdapter();
        new MediaAsyncTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
//        thumbnailsselection = new boolean[mediaItems.size()];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_item, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.media_item_recycler_view);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(mediaItemAdapter);

        sendItemButton = (Button) view.findViewById(R.id.send_item_button);
        sendItemButton.setText(mediaFolderName);
        // 没有选择图片就不让点击
        sendItemButton.setEnabled(false);
        sendItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = NewDishActivity.newImagePathIntent(getActivity(),mediaItems.get(lastSelectItem));
//                getActivity().startActivity(intent);
//                getActivity().finish();
//                Intent intent = NewDishActivity.newBundleImagePathIntent(getActivity(),mediaItems.get(lastSelectItem));
//                getActivity().startActivity(intent);
//                getActivity().finish();

                Intent i = new Intent();
                i.putExtra("back",mediaItems.get(lastSelectItem));
                getActivity().setResult(3, i);
                getActivity().finish();
            }
        });
//        fetchMediaItems();
        return view;
    }

    public class MediaAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fetchMediaItems();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            thumbnailsselection = new boolean[mediaItems.size()];
        }
    }

    public Void fetchMediaItems() {

//        ArrayList<String> mediaItems = new ArrayList<>();
        Uri uri;
        Cursor cursor;
        int column_index_data;

        String absolutePathOfImage = null;
        String tempMediaFolderName = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//        String[] mProjection = {MediaStore.Images.Thumbnails.DATA};
        // Defines a string to contain the selection clause
        String mSelectionClause = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        if (mediaFolderName.equals("latest")) {
            mSelectionClause = null;
        }

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {mediaFolderName};
        if (mediaFolderName.equals("latest")) {
            mSelectionArgs = null;
        }

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        cursor = getActivity().getContentResolver().query(uri, projection, mSelectionClause, mSelectionArgs, orderBy + " DESC");
        int totalItemNum = cursor.getCount();
        // 预防比如新手机上的照片不够25张的情况
        int latestIndex = totalItemNum > 25 ? 25 : totalItemNum;
        if (mediaFolderName.equals("latest")) {
            for (int i = 0; i < latestIndex; i++) {
                cursor.moveToPosition(i);
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                absolutePathOfImage = cursor.getString(column_index_data);  // 路径
//            Log.d("WQWQ", absolutePathOfImage);
                mediaItems.add(absolutePathOfImage);
                mediaItemAdapter.notifyDataSetChanged();
            }
        } else {
            while (cursor.moveToNext()) {
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                absolutePathOfImage = cursor.getString(column_index_data);  // 路径
//            Log.d("WQWQ", absolutePathOfImage);
                mediaItems.add(absolutePathOfImage);
                mediaItemAdapter.notifyDataSetChanged();
            }
        }

        cursor.close();
        return null;
    }

    /**
     * Created by WuQiang on 2017/4/30.
     */

    public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder> {
//        Context context;
//        ArrayList<String> mediaItems   = new ArrayList<>();
//        boolean[] thumbnailsselection;
        //      this.thumbnailsselection = new boolean[this.count];  // CheckBox

        public MediaItemAdapter() {
//            this.context = context;
//            this.mediaItems = mediaItems;
//            thumbnailsselection = new boolean[mediaItems.size()];
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
            String itemPath = mediaItems.get(position);
            Glide.with(getActivity()).load("file://" + itemPath)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageItem);
            holder.bind(itemPath);

            holder.itemCheckBox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (cb.isChecked()) {       // 如果以前已经选中过
                        cb.setChecked(true);            // 去掉钩
//                        Log.d("WQWQ", "if");
//                        Log.d("WQWQ_if_id", String.valueOf(id));

                        CheckBox tempButton = (CheckBox) getActivity().findViewById(lastSelectItem);
                        if (tempButton != null) {
                            tempButton.setChecked(false);
                        }
                        lastSelectItem = id;

                    } else {
//                        Log.d("WQWQ", "else");
                        cb.setChecked(false);      // 打上钩
                        if (lastSelectItem == id) {
                            lastSelectItem = -1;
                        }
//                        Log.d("WQWQ_else_id", String.valueOf(id));
                    }
//                    boolean booleanTemp = lastSelectItem == -1 ? false : true;
                    sendItemButton.setEnabled(lastSelectItem == -1 ? false : true);
//                    Log.d("WQWQ_lastSelectItem", String.valueOf(lastSelectItem));

                }
            });
            // 下面这一句单选的时候用不上
//            holder.itemCheckBox.setChecked(thumbnailsselection[position]);

        }

        @Override
        public int getItemCount() {
            return mediaItems.size();
        }

        public class MediaItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView imageItem;
            CheckBox itemCheckBox;
            String itemPath;

            //        TextView imagePath;
            public MediaItemViewHolder(View itemView) {
                super(itemView);
                imageItem = (ImageView) itemView.findViewById(R.id.image_item);
                itemCheckBox = (CheckBox) itemView.findViewById(R.id.image_item_check_box);
                //            imagePath = (TextView)itemView.findViewById(R.id.item_path_text_view);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + itemPath), "image/*");
                getActivity().startActivity(intent);
            }

            public void bind(String itemPath) {
                this.itemPath = itemPath;
            }
        }
    }
}
// http://blog.csdn.net/liuyifirsttime/article/details/38875287