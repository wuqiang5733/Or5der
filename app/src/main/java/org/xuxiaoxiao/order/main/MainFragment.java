package org.xuxiaoxiao.order.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.dish.DishActivity;
import org.xuxiaoxiao.order.infrastructure.NetWorkUtils;
import org.xuxiaoxiao.order.infrastructure.RecycleViewDivider;
import org.xuxiaoxiao.order.infrastructure.RecyclerViewClickListener2;
import org.xuxiaoxiao.order.login.LoginActivity;
import org.xuxiaoxiao.order.model.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static org.xuxiaoxiao.order.R.layout.restaurant_item;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class MainFragment extends Fragment {
    RecyclerView restaurantRecyclerView;
    LinearLayoutManager linearLayoutManager;
    RestaurantAdapter restaurantAdapter;
    ProgressBar progressBar;
//    ProgressBar mainPrograssBar;

    private List<Restaurant> restaurants =
            Collections.synchronizedList(new ArrayList<Restaurant>());
    //    private View _avatarProgressFrame; // 头像上转的那个圈
    // 用一个数组把 饭店图片跟饭店名字传送给 DishesFragment;
    String[] strArray = new String[2];
    CoordinatorLayout mainFragmentContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            // 允许用户使用应用
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
//        BmobUser user = new BmobUser();
        // 当 Activity 从操作系统接收到它的 onCreateOptionsMenu 回调函数时
        // FragmentManager 负责调用 Fragment.onCreateOptionsMenu
        // 所以要显式的告诉 FragmentManager ，Fragment 也应该接收到一个回调函数
        setHasOptionsMenu(true);
//        new LoadWordsThread().start();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_container, container, false);
        mainFragmentContainer = (CoordinatorLayout)view.findViewById(R.id.main_fragment_container);
        restaurantRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_restaurant);
        restaurantRecyclerView.setHasFixedSize(true);
        Drawable divider = getResources().getDrawable(R.drawable.item_divider);
        // 添加自定义分割线：可自定义分割线drawable
        restaurantRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 20, Color.WHITE)); // 设置分割线
//        restaurantRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));
        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置线性布局为横向（默认为纵向）
        restaurantRecyclerView.setLayoutManager(linearLayoutManager);
        restaurantAdapter = new RestaurantAdapter();
        restaurantRecyclerView.setAdapter(restaurantAdapter);
        progressBar = (ProgressBar) view.findViewById(R.id.main_fragment_progres_bar);
        if (NetWorkUtils.isNetworkConnected(getActivity())) {
            new RestaurantAsyncTask(progressBar).execute();
        } else {
            Toast.makeText(getActivity(), "请先检查网络联接", Toast.LENGTH_LONG).show();
        }

        restaurantRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener2(getActivity(), restaurantRecyclerView,
                new RecyclerViewClickListener2.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Toast.makeText(getActivity(), "Click " + restaurants.get(position).getName(), Toast.LENGTH_SHORT).show();
                        strArray[0] = restaurants.get(position).getName();
                        strArray[1] = restaurants.get(position).getPhoto().getFileUrl();

                        Intent intent = DishActivity.newIntent(getActivity(), strArray);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
//                        Toast.makeText(getActivity(), "Long Click " + restaurants.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }
                }));
        try {
            Glide.with(getActivity()).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
//        ArrayList<Restaurant> restaurants = new ArrayList<>();

        public RestaurantAdapter() {
//            this.restaurants = restaurants;
//            Log.d("WQWQ", String.valueOf(restaurants.size()));
//            notifyDataSetChanged();
        }

        @Override
        public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(restaurant_item, parent, false);
            return new RestaurantViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantViewHolder holder, int position) {
            holder.root.setTag(position);
            holder.bind(restaurants.get(position));

        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }
    }

    private class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private View root;
        Restaurant restaurant;
        TextView restaurantName;
        TextView restaurantRate;
        TextView restaurantAddress;
        ImageView restaurantImage;


        public RestaurantViewHolder(View itemView) {
            super(itemView);
            this.root = itemView;
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name_text_view);
            restaurantRate = (TextView) itemView.findViewById(R.id.restaurant_rate_text_view);
            restaurantAddress = (TextView) itemView.findViewById(R.id.restaurant_address_text_view);
            restaurantImage = (ImageView) itemView.findViewById(R.id.restaurant_image_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            int temp = (int) v.getTag();
//            Log.d("WQWQ", "你*长按*了第" + temp + "个元素");
            // 返回 true 的时候，不会在长按事件之后 产生 点击事件
            return true;
        }

        public void bind(Restaurant restaurant) {
            this.restaurant = restaurant;
            restaurantName.setText(restaurant.getName());
            restaurantRate.setText(String.valueOf(restaurant.getRate()));
            restaurantAddress.setText(restaurant.getAddress());
            Glide
                    .with(getActivity())
                    .load(restaurant.getPhoto().getUrl())
                    .centerCrop()
//                    .placeholder(R.drawable.error)
                    .crossFade()
                    .into(restaurantImage);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_restaurant, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_restaurant:
                Intent intent = new Intent(getContext(), NewRestaurantActivity.class);
                startActivity(intent);
                return true;
            case R.id.qr_bitmap:
                Intent qRCodeBitmapintent = new Intent(getContext(), CreateQRCodeBitmapActivity.class);
                startActivity(qRCodeBitmapintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class RestaurantAsyncTask extends AsyncTask<Void, ArrayList<Restaurant>, Void> {
        ProgressBar innerProgressBar;

        public RestaurantAsyncTask(ProgressBar innerProgressBar) {
            this.innerProgressBar = innerProgressBar;
        }

        @Override
        protected void onPreExecute() {
            innerProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            final ArrayList<Restaurant> innerRestaurants = new ArrayList<>();

            BmobQuery<Restaurant> query = new BmobQuery<Restaurant>();
            query.setLimit(10);
            query.findObjects(new FindListener<Restaurant>() {
                @Override
                public void done(List<Restaurant> object, BmobException e) {
                    if (e == null) {
                        // Success
                        for (Restaurant restaurant : object) {
                            innerRestaurants.add(restaurant);
                        }
                    } else {
                        // Fail
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        Snackbar.make(mainFragmentContainer, "查询出错了，请检查网络联接", Snackbar.LENGTH_LONG).show();
                    }
//                    if (object == null) {
//                        Log.i("bmob", "出错了 。。查询");
//                        getActivity().finish();
//                    }
                    if ((object != null)&&(object.size() == innerRestaurants.size())) {
                        onProgressUpdate(innerRestaurants);
                    }
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<Restaurant>... values) {
            super.onProgressUpdate(values);
            restaurants = values[0];
            restaurantAdapter.notifyDataSetChanged();
            innerProgressBar.setVisibility(View.GONE);
        }
    }

    private void fetchRestaurantData() {

    }
}
// http://blog.csdn.net/liuhe688/article/details/6532519