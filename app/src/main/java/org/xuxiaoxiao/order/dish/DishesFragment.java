package org.xuxiaoxiao.order.dish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.infrastructure.DishReadyEvent;
import org.xuxiaoxiao.order.model.Dish;

import java.util.ArrayList;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class DishesFragment extends Fragment {
    RecyclerView recyclerView;
    //    LinearLayoutManager linearLayoutManager;
    DishesAdapter dishesAdapter;
    ArrayList<Dish> dishes = new ArrayList<>();
    String restaurantName;
    GridLayoutManager gridLayoutManager;
    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.restaurant_name";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        restaurantName = getArguments().getString(RESTAURANT_NAME);
    }

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
    public void onEventMainThread(DishReadyEvent event) {
//        Log.d("WQWQ","我执行了");
        dishes.add(event.getDish());
        dishesAdapter.notifyDataSetChanged();
    }

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
        recyclerView.setHasFixedSize(true);

        // 添加自定义分割线：可自定义分割线drawable
//        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,20, Color.GRAY));
        recyclerView.setLayoutManager(gridLayoutManager);
        dishesAdapter = new DishesAdapter(dishes);
        recyclerView.setAdapter(dishesAdapter);
        return view;
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
            holder.bind(dishes.get(position));

        }

        @Override
        public int getItemCount() {
            return dishes.size();
        }
    }

    private class DishesViewHolder extends RecyclerView.ViewHolder {
        TextView dishname;
        TextView dishprice;
        TextView dishdiscription;
        TextView disPhotoUrl;
        ImageView disImage;
        //        WebImageView imageView;
        Dish dish;


        public DishesViewHolder(View itemView) {
            super(itemView);
            dishname = (TextView) itemView.findViewById(R.id.dish_name_text_view);
            dishprice = (TextView) itemView.findViewById(R.id.dish_price_text_view);
            dishdiscription = (TextView) itemView.findViewById(R.id.dish_discription_text_view);
            disPhotoUrl = (TextView) itemView.findViewById(R.id.dish_photourl_text_view);
            disImage = (ImageView) itemView.findViewById(R.id.dish_image_view);
//            imageView =(WebImageView) itemView.findViewById(R.id.dish_web_image_view);

        }

        public void bind(Dish dish) {
            this.dish = dish;
            dishname.setText(dish.getName());
            dishprice.setText(String.valueOf(dish.getPrice()));
            dishdiscription.setText(dish.getDiscription());
//            imageView.setPlaceholderImage(R.drawable.error);
//            imageView.setImageUrl("http://bmob-cdn-10939.b0.upaiyun.com/2017/04/26/3230720540baa93e80edd3c101765d66.png");
            Picasso.with(getActivity()).load(dish.getPhotoUrl().getFileUrl())
                    .fit().centerCrop()
//                    .placeholder(R.drawable.error)
                    .error(R.drawable.error).into(disImage);
//            Glide
//                    .with(getActivity())
//                    .load("http://bmob-cdn-10939.b0.upaiyun.com/2017/04/26/3230720540baa93e80edd3c101765d66.png")
//                    .centerCrop()
////                    .placeholder(R.drawable.error)
//                    .crossFade()
//                    .into(disImage);
//            disPhotoUrl.setText(dish.getPhotoUrl().getFileUrl());
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
                Intent intent = NewDishActivity.newIntent(getActivity(), restaurantName);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
