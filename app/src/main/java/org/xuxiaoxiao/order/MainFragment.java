package org.xuxiaoxiao.order;

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
        linearLayoutManager = new LinearLayoutManager(getActivity());
        restaurantRecyclerView.setLayoutManager(linearLayoutManager);
        restaurantAdapter = new RestaurantAdapter(restaurants);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BmobQuery<Restaurant> query = new BmobQuery<Restaurant>();
        query.setLimit(10);
        query.findObjects(new FindListener<Restaurant>() {
            @Override
            public void done(List<Restaurant> object, BmobException e) {
                if (e == null) {
                    for (Restaurant gameScore : object) {
                        gameScore.getName();
//                        Log.i("bmob", "成功：" + gameScore.getName());
//                        Log.i("bmob", "成功：" + String.valueOf(gameScore.getRate()));

                        gameScore.getObjectId();
                        gameScore.getCreatedAt();
                        restaurants.add(new Restaurant(gameScore.getName(), gameScore.getRate()));
                        restaurantAdapter.notifyDataSetChanged();

//                        Log.d("WQWQ", String.valueOf(restaurants.size()));

                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
//        Log.d("WQWQ--", String.valueOf(restaurants.size()));

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
            holder.restaurantName.setText(restaurants.get(position).getName());
            holder.restaurantRate.setText(String.valueOf(restaurants.get(position).getRate()));

        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }
    }

    private class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        }
    }
}
