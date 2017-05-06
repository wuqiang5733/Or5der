package org.xuxiaoxiao.order.dish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.login.LoginActivity;
import org.xuxiaoxiao.order.model.Dish;

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

    private static final String RESTAURANT_NAME_AND_URL = "org.xuxiaoxiao.order.dish.DishesFragment.restaurant_name_url";
    LinearLayout orderedDishBottomBarLinearLayout;
    TextView orderedDishesDetail;
    Button orderedDishesCheckButton;
    RecyclerView recyclerView;
    //    LinearLayoutManager linearLayoutManager;
    DishesAdapter dishesAdapter;
    GridLayoutManager gridLayoutManager;

    private List<Dish> dishes =
            Collections.synchronizedList(new ArrayList<Dish>());
    // 决定是否是在点菜模式下
    private boolean isOrderMode = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    // 应该用一个布尔数组也能实现
    boolean [] checkArray;
    // 为了及时的显示点了几道菜而做的变量
//    private int orderedDishshSum = 0;
    // 要传送的点了的菜品，第一个元素是饭店的名字
    private ArrayList<String> orderedDishesArrayList = new ArrayList<>();
    private FloatingActionButton fab;
    // 扫描二维码用的一个变量
    private String toast;
    private String[] nameAndUrl;
    private ProgressBar progressBar;
    CoordinatorLayout dishesFragmentContainer;
//    Toolbar secondToolBar;
    TSnackbar snackbar;

    @Override
    public void onResume() {
        super.onResume();
        orderedDishesArrayList.clear();
        orderedDishesArrayList.add(nameAndUrl[0]);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
//        savedInstanceState.putString(STATE_RESTAURANT_NAME_IN_DISHES_FRAGMENT, restaurantName);
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
        nameAndUrl = getArguments().getStringArray(RESTAURANT_NAME_AND_URL);

        Log.d("WQWQ----",nameAndUrl[0]);
        // 要传送的点了的菜品，第一个元素是饭店的名字
        orderedDishesArrayList.add(nameAndUrl[0]);
        dishesAdapter = new DishesAdapter();
        if (savedInstanceState != null){
//            restaurantName = savedInstanceState.getString(STATE_RESTAURANT_NAME_IN_DISHES_FRAGMENT);
        }

    }

    public static DishesFragment newInstance(String[] nameAndUrl) {
        // 这个方法接收来自 Activity 的数据
        Bundle args = new Bundle();
        args.putStringArray(RESTAURANT_NAME_AND_URL,nameAndUrl);

        DishesFragment fragment = new DishesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dishes_container, container, false);
        dishesFragmentContainer = (CoordinatorLayout)view.findViewById(R.id.dishes_fragment_container);
        orderedDishBottomBarLinearLayout = (LinearLayout)view.findViewById(R.id.order_dish_bottom_bar_linear_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_dishes);
        orderedDishesDetail = (TextView)view.findViewById(R.id.ordered_dishes_detail_text_view);
        orderedDishesCheckButton = (Button)view.findViewById(R.id.ordered_dishes_check_button);
//        secondToolBar = (Toolbar)view.findViewById(R.id.second_toolbar);
//        secondToolBar.setSubtitle("text");
        fab = (FloatingActionButton) view.findViewById(R.id.refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });

        orderedDishesCheckButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 不为什么，这个按钮点击事件会触发两次
             * @param v
             */
            @Override
            public void onClick(View v) {

            }
        });
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setHasFixedSize(true);

        // 添加自定义分割线：可自定义分割线drawable
//        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,20, Color.GRAY));
        recyclerView.setLayoutManager(gridLayoutManager);
//        dishesAdapter = new DishesAdapter();
        recyclerView.setAdapter(dishesAdapter);
        fetchDishData();
        try {
            Glide.with(getActivity()).load(nameAndUrl[1]).into((ImageView) view.findViewById(R.id.backdrop));
//            Glide.with(getActivity()).load(restaurantPhotoUrl).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private class DishesAdapter extends RecyclerView.Adapter<DishesViewHolder> {

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
//            holder.root.setTag(position);
            holder.orderDishCheckBox.setTag(position);
            //
//            if (isOrderMode) {
//                holder.orderDishCheckBox.setVisibility(View.VISIBLE);
//            } else {
//                holder.orderDishCheckBox.setVisibility(View.INVISIBLE);
//            }
            holder.orderDishCheckBox.setVisibility(isOrderMode? View.VISIBLE:View.GONE);

            //设置checkBox改变监听
//            holder.orderDishCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                }
//            });

//            holder.orderDishCheckBox.setChecked(map.get(position));
            if (isOrderMode){
                // 如果没有一个判断，会出现： Attempt to read from null array 异常
                holder.orderDishCheckBox.setChecked(checkArray[position]);

            }
        }

        @Override
        public int getItemCount() {
            return dishes.size();
        }

    }

    private class DishesViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView dishname;
        TextView dishprice;
        TextView dishdiscription;
        TextView disPhotoUrl;
        ImageView disImage;
        CheckBox orderDishCheckBox;
        //        WebImageView imageView;
        Dish dish;


        public DishesViewHolder(final View itemView) {
            super(itemView);
            this.root = itemView;
            dishname = (TextView) itemView.findViewById(R.id.dish_name_text_view);
            dishprice = (TextView) itemView.findViewById(R.id.dish_price_text_view);
            dishdiscription = (TextView) itemView.findViewById(R.id.dish_discription_text_view);
            disPhotoUrl = (TextView) itemView.findViewById(R.id.dish_photourl_text_view);
            disImage = (ImageView) itemView.findViewById(R.id.dish_image_view);
            orderDishCheckBox = (CheckBox) itemView.findViewById(R.id.order_dish_menu);
//            orderDishCheckBox.setVisibility(isOrderMode? View.VISIBLE:View.GONE);
//            imageView =(WebImageView) itemView.findViewById(R.id.dish_web_image_view);
//            orderDishCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int orderedSum = 0 ;
//                    // 注意这个事件在上下拖动RecyclerView的时候会不停的触发，所以做计算的时候，要特别小心
//                    int i = (int)root.getTag();
////                    snackbar.setText(String.valueOf(i));
////                    CheckBox checkBox = (CheckBox)itemView.findViewById(i);
//                    checkArray[i] = isChecked;
//                    for (int j=0;j<dishes.size();j++){
////                        Log.d("WQWQ",j+1 + ": " + String.valueOf(checkArray[j]));
//                        if (checkArray[i] == true){
//                            orderedSum++;
//                        }
//                        snackbar.setText("你已经点了" + orderedSum + "道菜");
//                    }
////                    Log.d("WQWQ","=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+");
//                }
//            });
            orderDishCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int)v.getTag();
                    Log.d("WQWQ",String.valueOf(i));
                }
            });
        }

        public void bind(Dish dish) {
            this.dish = dish;
            dishname.setText(dish.getName());
            dishprice.setText(String.valueOf(dish.getPrice()));
            dishdiscription.setText(dish.getDiscription());
            Glide
                    .with(getActivity())
                    .load(dish.getPhoto().getUrl())
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
                isOrderMode = !isOrderMode;
                if (isOrderMode){
                    initOrder();
                    snackbar.show();
                }else {
                    snackbar.dismiss();
                }
                // 其实 notifyDataSetChanged 有重绘的功能
                dishesAdapter.notifyDataSetChanged();


                return true;
            case R.id.new_dish:
                Intent intent = NewDishActivity.newIntent(getActivity(), nameAndUrl[0]);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initOrder() {
        // Snackbar.make(dishesFragmentContainer, "Your Snackbar", Snackbar.LENGTH_INDEFINITE).show();

        TSnackbar.make(dishesFragmentContainer,"Hello from TSnackBar.", Snackbar.LENGTH_INDEFINITE).show();
//      TSnackbar.make(dishesFragmentContainer,"Hello from TSnackBar.",TSnackbar.LENGTH_LONG).show();

        snackbar = TSnackbar.make(dishesFragmentContainer, String.valueOf(isOrderMode), TSnackbar.LENGTH_INDEFINITE);
//        TSnackbar snackbar = TSnackbar.make(dishesFragmentContainer, String.valueOf(isOrderMode), TSnackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
//        snackbar.show();

        checkArray = new boolean[dishes.size()];
        for (int i=0; i<dishes.size(); i++){
            checkArray[i] = false;
        }
        // 其实 notifyDataSetChanged 有重绘的功能
        dishesAdapter.notifyDataSetChanged();
    }

    private void fetchDishData() {
        // Fetch data from website
        BmobQuery<Dish> query = new BmobQuery<Dish>();
        query.setLimit(20);
        // 获得来自 Activity 的数据
//        final String restaurantName = getArguments().getString(RESTAURANT_NAME);
        query.addWhereEqualTo("restaurantName", nameAndUrl[0]);

        query.findObjects(new FindListener<Dish>() {
            @Override
            public void done(List<Dish> object, BmobException e) {
                if (e == null) {
                    // Success
                    for (Dish dish : object) {
//                        Log.d("WQWQ",dish.getPhoto().getFileUrl());
                        dishes.add(dish);
                        dishesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    // Fail
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    Snackbar.make(dishesFragmentContainer,e.getMessage(),Snackbar.LENGTH_LONG).show();
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
