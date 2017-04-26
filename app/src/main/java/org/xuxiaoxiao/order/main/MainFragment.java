package org.xuxiaoxiao.order.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.dish.DishActivity;
import org.xuxiaoxiao.order.infrastructure.RecycleViewDivider;
import org.xuxiaoxiao.order.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class MainFragment extends Fragment {
    RecyclerView restaurantRecyclerView;
    LinearLayoutManager linearLayoutManager;
    RestaurantAdapter restaurantAdapter;
    ArrayList<Restaurant> restaurants = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        restaurantRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_restaurant);
        restaurantRecyclerView.setHasFixedSize(true);
        Drawable divider = getResources().getDrawable(R.drawable.item_divider);
        // 添加自定义分割线：可自定义分割线drawable
        restaurantRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL,20, Color.WHITE)); // 设置分割线
//        restaurantRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));
        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置线性布局为横向（默认为纵向）
        restaurantRecyclerView.setLayoutManager(linearLayoutManager);
        restaurantAdapter = new RestaurantAdapter(restaurants);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Fetch data from website
                BmobQuery<Restaurant> query = new BmobQuery<Restaurant>();
                query.setLimit(10);
                query.findObjects(new FindListener<Restaurant>() {
                    @Override
                    public void done(List<Restaurant> object, BmobException e) {
                        if (e == null) {
                            // Success
                            for (Restaurant restaurant : object) {
//                                restaurant.getName();
//                                restaurant.getObjectId();
//                                restaurant.getCreatedAt();
                                restaurants.add(new Restaurant(restaurant.getName(), restaurant.getRate()));
                                restaurantAdapter.notifyDataSetChanged();
                            }
                        } else {
                            // Fail
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        }).start();

    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
            this.restaurants = restaurants;
            Log.d("WQWQ", String.valueOf(restaurants.size()));
        }

        @Override
        public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.restaurant, parent, false);
            return new RestaurantViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantViewHolder holder, int position) {
//            holder.restaurantName.setText(restaurants.get(position).getName());
//            holder.restaurantRate.setText(String.valueOf(restaurants.get(position).getRate()));
            holder.bind(restaurants.get(position));

        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }
    }

    private class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Restaurant restaurant;
        TextView restaurantName;
        TextView restaurantRate;


        public RestaurantViewHolder(View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name_text_view);
            restaurantRate = (TextView) itemView.findViewById(R.id.restaurant_rate_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // 在Fragment 当中启动 一个putExtra的Intent
            Intent intent = DishActivity.newIntent(getActivity(), restaurant.getName());
            startActivity(intent);
        }

        public void bind(Restaurant restaurant) {
            this.restaurant = restaurant;
            restaurantName.setText(restaurant.getName());
            restaurantRate.setText(String.valueOf(restaurant.getRate()));
        }
    }
}
