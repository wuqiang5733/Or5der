package org.xuxiaoxiao.order.dish;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.model.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class DishesFragment extends Fragment {
    RecyclerView recyclerView;
    //    LinearLayoutManager linearLayoutManager;
    DishesAdapter dishesAdapter;
    //    ArrayList<Dish> dishes = new ArrayList<>();
    String restaurantName;
    GridLayoutManager gridLayoutManager;

    private List<Dish> dishes =
            Collections.synchronizedList(new ArrayList<Dish>());

    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.restaurant_name";
    // 决定是否显示 复选框
    private boolean isshowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        restaurantName = getArguments().getString(RESTAURANT_NAME);
        dishesAdapter = new DishesAdapter();

        new RestaurantAsyncTask().execute();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onDetach() {
//        EventBus.getDefault().unregister(this);
//
//        super.onDetach();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(DishReadyEvent event) {
////        Log.d("WQWQ","我执行了");
//        dishes.add(event.getDish());
//        dishesAdapter.notifyDataSetChanged();
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(SendRstaurantNameEvent event) {
//        restaurantName = event.getRestaurantName();
//    }

    public void setModel(ArrayList<Dish> model) {
        this.dishes = model;
    }

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
//        Drawable divider = getResources().getDrawable(R.drawable.item_divider);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setHasFixedSize(true);

        // 添加自定义分割线：可自定义分割线drawable
//        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,20, Color.GRAY));
        recyclerView.setLayoutManager(gridLayoutManager);
//        dishesAdapter = new DishesAdapter();
        recyclerView.setAdapter(dishesAdapter);
        return view;
    }

    private class DishesAdapter extends RecyclerView.Adapter<DishesViewHolder> {
//        ArrayList<Dish> dishes = new ArrayList<>();

        public DishesAdapter() {
//            this.dishes = dishes;
        }

        @Override
        public DishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.dish, parent, false);
            return new DishesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DishesViewHolder holder, final int position) {
            holder.bind(dishes.get(position));
            //
            if (isshowBox) {
                holder.orderDishCheckBox.setVisibility(View.VISIBLE);
            } else {
                holder.orderDishCheckBox.setVisibility(View.INVISIBLE);
            }

            //设置checkBox改变监听
            holder.orderDishCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //用map集合保存
                    map.put(position, isChecked);

                    for (int i = 0; i < map.size(); i++) {
                        if (map.get(i)) {
                            Log.d("TAG", "你选了第：" + i + "项");
                        }
                    }
                }
            });
            // 设置CheckBox的状态
            if (map.get(position) == null) {
                map.put(position, false);
            }
            holder.orderDishCheckBox.setChecked(map.get(position));

        }

        @Override
        public int getItemCount() {
            return dishes.size();
        }



        //设置是否显示CheckBox
        public void setShowBox() {
            //取反
            isshowBox = !isshowBox;
        }

        //初始化map集合,默认为不选中
        public void initMap() {
            for (int i = 0; i < dishes.size(); i++) {
                map.put(i, false);
            }
        }
    }

    private class DishesViewHolder extends RecyclerView.ViewHolder {
        TextView dishname;
        TextView dishprice;
        TextView dishdiscription;
        TextView disPhotoUrl;
        ImageView disImage;
        CheckBox orderDishCheckBox;
        //        WebImageView imageView;
        Dish dish;


        public DishesViewHolder(View itemView) {
            super(itemView);
            dishname = (TextView) itemView.findViewById(R.id.dish_name_text_view);
            dishprice = (TextView) itemView.findViewById(R.id.dish_price_text_view);
            dishdiscription = (TextView) itemView.findViewById(R.id.dish_discription_text_view);
            disPhotoUrl = (TextView) itemView.findViewById(R.id.dish_photourl_text_view);
            disImage = (ImageView) itemView.findViewById(R.id.dish_image_view);
            orderDishCheckBox = (CheckBox) itemView.findViewById(R.id.order_dish_menu);
//            imageView =(WebImageView) itemView.findViewById(R.id.dish_web_image_view);

        }

        public void bind(Dish dish) {
            this.dish = dish;
            dishname.setText(dish.getName());
            dishprice.setText(String.valueOf(dish.getPrice()));
            dishdiscription.setText(dish.getDiscription());
//            imageView.setPlaceholderImage(R.drawable.error);
//            imageView.setImageUrl("http://bmob-cdn-10939.b0.upaiyun.com/2017/04/26/3230720540baa93e80edd3c101765d66.png");
//            Picasso.with(getActivity()).load(dish.getPhotoUrl().getFileUrl())
//                    .fit().centerCrop()
////                    .placeholder(R.drawable.error)
//                    .error(R.drawable.error).into(disImage);
            Glide
                    .with(getActivity())
                    .load(dish.getPhotoUrl().getFileUrl())
                    .centerCrop()
//                    .placeholder(R.drawable.error)
                    .crossFade()
                    .into(disImage);
//            disPhotoUrl.setText(dish.getPhotoUrl().getFileUrl());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_dish, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_restaurant:
                Intent intent = NewDishActivity.newIntent(getActivity(), restaurantName);
                startActivity(intent);
                return true;
            case R.id.order_dish:
                dishesAdapter.setShowBox();
                dishesAdapter.notifyDataSetChanged();
                dishesAdapter.initMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class RestaurantAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fetchDishData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void fetchDishData() {
        // Fetch data from website
        BmobQuery<Dish> query = new BmobQuery<Dish>();
        query.setLimit(10);
        // 获得来自 Activity 的数据
        final String restaurantName = getArguments().getString(RESTAURANT_NAME);
//            EventBus.getDefault().post(new SendRstaurantNameEvent(restaurantName));
        query.addWhereEqualTo("restaurantName", restaurantName);
//            Log.d("WQWQ",restaurantName);

        query.findObjects(new FindListener<Dish>() {
            @Override
            public void done(List<Dish> object, BmobException e) {
                if (e == null) {
                    // Success
                    for (Dish dish : object) {
//                                Log.d("WQWQ",restaurant.getName());
                        dishes.add(new Dish(dish.getName(), dish.getPrice(), dish.getDiscription(), dish.getPhotoUrl(), dish.getRestaurantName()));
//                            EventBus.getDefault().post(new DishReadyEvent(dish));
                        dishesAdapter.notifyDataSetChanged();

                    }
                } else {
                    // Fail
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
