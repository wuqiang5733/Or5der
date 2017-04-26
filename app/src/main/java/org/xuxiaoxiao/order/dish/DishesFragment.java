package org.xuxiaoxiao.order.dish;

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
import org.xuxiaoxiao.order.model.Dish;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class DishesFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DishesAdapter dishesAdapter;
    ArrayList<Dish> dishes = new ArrayList<>();
    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.restaurant_name";

    public static DishesFragment newInstance(String restaurantName) {
        // 这个方法接收来自 Activity 的数据
        Bundle args = new Bundle();
        args.putString(RESTAURANT_NAME, restaurantName);

        DishesFragment fragment = new DishesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dishsh, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_dishes);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        dishesAdapter = new DishesAdapter(dishes);
        recyclerView.setAdapter(dishesAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获得来自 Activity 的数据
       final String restaurantName = getArguments().getString(RESTAURANT_NAME);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Fetch data from website
                BmobQuery<Dish> query = new BmobQuery<Dish>();
                query.setLimit(10);
                query.addWhereEqualTo("restaurantName", restaurantName);
                query.findObjects(new FindListener<Dish>() {
                    @Override
                    public void done(List<Dish> object, BmobException e) {
                        if (e == null) {
                            // Success
                            for (Dish dish : object) {
//                                restaurant.getName();
//                                restaurant.getObjectId();
//                                restaurant.getCreatedAt();
                                dishes.add(new Dish(dish.getName(), dish.getPrice(), dish.getDiscription()));
                                dishesAdapter.notifyDataSetChanged();
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

    private class DishesAdapter extends RecyclerView.Adapter<DishesViewHolder> {
        ArrayList<Dish> dishes = new ArrayList<>();

        public DishesAdapter(ArrayList<Dish> dishes) {
            this.dishes = dishes;
        }

        @Override
        public DishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.dish, parent, false);
            return new DishesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DishesViewHolder holder, int position) {
            holder.dishname.setText(dishes.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return dishes.size();
        }
    }

    private class DishesViewHolder extends RecyclerView.ViewHolder {
        TextView dishname;

        public DishesViewHolder(View itemView) {
            super(itemView);
            dishname = (TextView) itemView.findViewById(R.id.dish_name_text_view);
        }
    }
}
