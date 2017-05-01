package org.xuxiaoxiao.order.addimage;

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

import org.xuxiaoxiao.order.R;

import java.util.ArrayList;

/**
 * Created by WuQiang on 2017/4/30.
 */

public class MediaItemFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    MediaItemAdapter mediaItemAdapter;
    Button sendItemButton;
    String mediaFolderName;
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


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_item, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.media_item_recycler_view);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        new MediaAsyncTask().execute();
        sendItemButton = (Button) view.findViewById(R.id.send_item_button);
        sendItemButton.setText(mediaFolderName);
        sendItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fetchMediaItems();
        return view;
    }

    public class MediaAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
//        ArrayList<MediaFolderModel> mediaFolderModels = new ArrayList<>();


        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return fetchMediaItems();
//            return mediaFolderModels;
        }

        @Override
        protected void onPostExecute(ArrayList<String> mediaItems) {
//            super.onPostExecute(mediaFolderModels);
            mediaItemAdapter = new MediaItemAdapter(getActivity(), mediaItems);
            recyclerView.setAdapter(mediaItemAdapter);
        }
    }

    public ArrayList<String> fetchMediaItems() {

        ArrayList<String> mediaItems = new ArrayList<>();
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
        if (mediaFolderName.equals("latest") ) {
            mSelectionClause = null;
        }

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {mediaFolderName};
        if (mediaFolderName.equals("latest") ) {
            mSelectionArgs = null;
        }

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        cursor = getActivity().getContentResolver().query(uri, projection, mSelectionClause, mSelectionArgs, orderBy + " DESC");
        int totalItemNum = cursor.getCount();
        // 预防比如新手机上的照片不够25张的情况
        int latestIndex = totalItemNum > 25 ? 25:totalItemNum;
        if (mediaFolderName.equals("latest") ) {
            for (int i = 0; i < latestIndex; i++) {
                cursor.moveToPosition(i);
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                absolutePathOfImage = cursor.getString(column_index_data);  // 路径
//            Log.d("WQWQ", absolutePathOfImage);
                mediaItems.add(absolutePathOfImage);
            }
        } else {
            while (cursor.moveToNext()) {
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                absolutePathOfImage = cursor.getString(column_index_data);  // 路径
//            Log.d("WQWQ", absolutePathOfImage);
                mediaItems.add(absolutePathOfImage);
            }
        }

        cursor.close();
        return mediaItems;
    }

}