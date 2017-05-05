package org.xuxiaoxiao.order.dish;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.login.LoginActivity;
import org.xuxiaoxiao.order.model.Dish;
import org.xuxiaoxiao.order.ordereddishes.OrderedDishesActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class DishesFragment extends Fragment {

    private static final String STATE_RESTAURANT_NAME_IN_DISHES_FRAGMENT = "org.xuxiaoxiao.order.dishesfragment.state_restaurant_name";
    LinearLayout orderedDishBottomBarLinearLayout;
    TextView orderedDishesDetail;
    Button orderedDishesCheckButton;
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
    // 决定是否是在点菜模式下
    private boolean isOrderMode = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    // 为了及时的显示点了几道菜而做的变量
    private int orderedDishshSum = 0;
    // 要传送的点了的菜品，第一个元素是饭店的名字
    private ArrayList<String> orderedDishesArrayList = new ArrayList<>();
    private FloatingActionButton fab;
    // 扫描二维码用的一个变量
    private String toast;


    @Override
    public void onResume() {
        super.onResume();
        orderedDishesArrayList.clear();
        orderedDishesArrayList.add(restaurantName);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_RESTAURANT_NAME_IN_DISHES_FRAGMENT, restaurantName);
//        savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        // Always call the superclass so it can restore the view hierarchy
//        super.onRestoreInstanceState(savedInstanceState);
//
//
//        // Restore state members from saved instance
//        restaurantName = savedInstanceState.getString(STATE_RESTAURANT_NAME_IN_DISHES_FRAGMENT);
////        mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
//    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        restaurantName = getArguments().getString(RESTAURANT_NAME);
        // 要传送的点了的菜品，第一个元素是饭店的名字
        orderedDishesArrayList.add(restaurantName);
        dishesAdapter = new DishesAdapter();

        new RestaurantAsyncTask().execute();
        if (savedInstanceState != null){
            restaurantName = savedInstanceState.getString(STATE_RESTAURANT_NAME_IN_DISHES_FRAGMENT);
        }
//        Log.d("WQWQ",getClass().getSimpleName());
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

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
        View view = inflater.inflate(R.layout.fragment_dishes_container, container, false);
        orderedDishBottomBarLinearLayout = (LinearLayout)view.findViewById(R.id.order_dish_bottom_bar_linear_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_dishes);
//        Drawable divider = getResources().getDrawable(R.drawable.item_divider);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);
        orderedDishesDetail = (TextView)view.findViewById(R.id.ordered_dishes_detail_text_view);
        orderedDishesCheckButton = (Button)view.findViewById(R.id.ordered_dishes_check_button);
        fab = (FloatingActionButton) view.findViewById(R.id.refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });
        if (orderedDishshSum < 1){
            orderedDishesCheckButton.setEnabled(false);
        }else {
            orderedDishesCheckButton.setEnabled(true);
        }
        orderedDishesCheckButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 不为什么，这个按钮点击事件会触发两次
             * @param v
             */
            @Override
            public void onClick(View v) {
                for (int i = 0; i < map.size(); i++) {
                    if (map.get(i)) {
                        Log.d("TAG", "你选了第：" + i + "项");
                        orderedDishshSum ++;
                        orderedDishesDetail.setText("已经点了" + orderedDishshSum + "道菜");
                        orderedDishesArrayList.add(dishes.get(i).getName());
                        Intent intent = OrderedDishesActivity.newOredredDishesIntent(getActivity(), orderedDishesArrayList);
                        getActivity().startActivityForResult(intent,11);
                        Log.d("WQWQ","DishesFragment-orderedDishesCheckButton");
                    }
                }
            }
        });
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
            if (isOrderMode) {
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
                            orderedDishshSum ++;
                            orderedDishesDetail.setText("已经点了" + orderedDishshSum + "道菜");
                        }
                    }
                    if (orderedDishshSum < 1){
                        orderedDishesCheckButton.setEnabled(false);
                    }else {
                        orderedDishesCheckButton.setEnabled(true);
                    }
//                    orderedDishshSum = 0;
                }
            });
            // 设置CheckBox的状态
            if (map.get(position) == null) {
                map.put(position, false);
            }
            holder.orderDishCheckBox.setChecked(map.get(position));
            orderedDishshSum = 0;

        }

        @Override
        public int getItemCount() {
            return dishes.size();
        }



        //设置是否显示CheckBox
        public void setShowBox() {
            //取反
            isOrderMode = !isOrderMode;
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
            case R.id.order_dish:
                orderedDishBottomBarLinearLayout.setVisibility(isOrderMode ? View.GONE : View.VISIBLE);
                fab.setVisibility(isOrderMode ? View.VISIBLE : View.GONE);
                dishesAdapter.setShowBox();
                dishesAdapter.initMap();
                dishesAdapter.notifyDataSetChanged();
//                if(isOrderMode){ // 如果不在点菜模式下了
//                    orderedDishshSum = 0;
//                }
                orderedDishshSum = 0;

                return true;
            case R.id.new_dish:
                Intent intent = NewDishActivity.newIntent(getActivity(), restaurantName);
                startActivity(intent);
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
    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "二维码代表：" + result.getContents();
            }
// zxing-android-embedded : https://github.com/journeyapps/zxing-android-embedded
            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }
    /**
     * 因此，在需要更新进度值时，AsyncTask的基本生命周期过程为：
     * onPreExecute() --> doInBackground() --> publishProgress() -->onProgressUpdate()--> onPostExecute()。
     可以看到，AsyncTask的优秀之处在于几个回调方法的设置上，
     只有donInBackground()是运行在子线程的，
     其他三个回调方法都是在主线程中运行，
     因此，只要在AsyncTask中，就可以实现文件的后台下载、UI的更新操作。
     http://www.2cto.com/kf/201606/518662.html
     Checkable Views : https://chris.banes.me/2013/03/22/checkable-views/
     */
}
