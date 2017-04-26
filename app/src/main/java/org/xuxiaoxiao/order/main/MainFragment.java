package org.xuxiaoxiao.order.main;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.dish.DishActivity;
import org.xuxiaoxiao.order.infrastructure.RecycleViewDivider;
import org.xuxiaoxiao.order.infrastructure.RecyclerViewClickListener2;
import org.xuxiaoxiao.order.infrastructure.RestaurantReadyEvent;
import org.xuxiaoxiao.order.model.Restaurant;

import java.util.ArrayList;

import static org.xuxiaoxiao.order.R.layout.restaurant;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class MainFragment extends Fragment {
    RecyclerView restaurantRecyclerView;
    LinearLayoutManager linearLayoutManager;
    RestaurantAdapter restaurantAdapter;
    ArrayList<Restaurant> restaurants = new ArrayList<>();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);

        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RestaurantReadyEvent event) {
        Log.d("WQWQ", "我执行了");
        restaurants.add(event.getRestaurant());
        restaurantAdapter.notifyDataSetChanged();
    }

    public void setModel(ArrayList<Restaurant> model) {
        this.restaurants = model;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 当 Activity 从操作系统接收到它的 onCreateOptionsMenu 回调函数时
        // FragmentManager 负责调用 Fragment.onCreateOptionsMenu
        // 所以要显式的告诉 FragmentManager ，Fragment 也应该接收到一个回调函数
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        restaurantRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_restaurant);
        restaurantRecyclerView.setHasFixedSize(true);
        Drawable divider = getResources().getDrawable(R.drawable.item_divider);
        // 添加自定义分割线：可自定义分割线drawable
        restaurantRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 20, Color.WHITE)); // 设置分割线
//        restaurantRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));
        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置线性布局为横向（默认为纵向）
        restaurantRecyclerView.setLayoutManager(linearLayoutManager);
        restaurantAdapter = new RestaurantAdapter(restaurants);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
//        restaurantRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(restaurantRecyclerView) {
//            @Override
//            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
//
//            }
//
//            @Override
//            public void onItemLOngClick(RecyclerView.ViewHolder viewHolder) {
////                viewHolder.getItemId();
//                Toast.makeText(getActivity(),String.valueOf(viewHolder.getItemId()),Toast.LENGTH_SHORT).show();
//
//            }
//        });
        restaurantRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener2(getActivity(), restaurantRecyclerView,
                new RecyclerViewClickListener2.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "Click " + restaurants.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = DishActivity.newIntent(getActivity(), restaurants.get(position).getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getActivity(), "Long Click " + restaurants.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }
                }));
        return view;
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
            this.restaurants = restaurants;
//            Log.d("WQWQ", String.valueOf(restaurants.size()));
        }

        @Override
        public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(restaurant, parent, false);
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
//            EventBus.getDefault().post
//            EventBus.getDefault().post(new SendRstaurantNameEvent(restaurant.getName()));
//            Intent intent = DishActivity.newIntent(getActivity(), restaurant.getName());
//            startActivity(intent);
        }

        public void bind(Restaurant restaurant) {
            this.restaurant = restaurant;
            restaurantName.setText(restaurant.getName());
            restaurantRate.setText(String.valueOf(restaurant.getRate()));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        // 根据指示器来决定要不要显示 Subtitle
//        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
//        if (mSubtitleVisible) {
//            subtitleItem.setTitle(R.string.hide_subtitle);
//        } else {
//            subtitleItem.setTitle(R.string.show_subtitle);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Intent intent = new Intent(getContext(),AddNewRestaurantActivity.class);
                startActivity(intent);
                return true;
//            case R.id.show_subtitle:
//                // 显示 Subtitle
//                mSubtitleVisible = !mSubtitleVisible;
//                getActivity().invalidateOptionsMenu();
//                updateSubtitle();
//                // 返回 true 来指示不需要更进一步的处理了
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
