package org.xuxiaoxiao.order.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.dish.DishActivity;
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

import static org.xuxiaoxiao.order.R.layout.restaurant;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class MainFragment extends Fragment {
    RecyclerView restaurantRecyclerView;
    LinearLayoutManager linearLayoutManager;
    RestaurantAdapter restaurantAdapter;
//    ProgressBar mainPrograssBar;

    private List<Restaurant> restaurants =
            Collections.synchronizedList(new ArrayList<Restaurant>());
    private View _avatarProgressFrame; // 头像上转的那个圈

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
        }else{
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
        restaurantAdapter = new RestaurantAdapter();

        new RestaurantAsyncTask(getActivity()).execute();


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
        restaurantRecyclerView.setAdapter(restaurantAdapter);
//        _avatarProgressFrame = view.findViewById(R.id.activity_profile_avatarProgressFrame); // 头像上转的那个圈
//        _avatarProgressFrame.setVisibility(View.VISIBLE);

//        mainPrograssBar = (ProgressBar)view.findViewById(R.id.main_progress_bar);

//        restaurantAdapter = new RestaurantAdapter(restaurants);
//        restaurantRecyclerView.setAdapter(restaurantAdapter);
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
//                        Toast.makeText(getActivity(), "Click " + restaurants.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = DishActivity.newIntent(getActivity(), restaurants.get(position).getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
//                        Toast.makeText(getActivity(), "Long Click " + restaurants.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }
                }));
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
            View view = getActivity().getLayoutInflater().inflate(restaurant, parent, false);
            return new RestaurantViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantViewHolder holder, int position) {
//            holder.restaurantName.setText(restaurants.get(position).getName());
//            holder.restaurantRate.setText(String.valueOf(restaurants.get(position).getRate()));
            holder.root.setTag(position);
            holder.bind(restaurants.get(position));

        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }
    }

    private class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{
        private View root;
        Restaurant restaurant;
        TextView restaurantName;
        TextView restaurantRate;


        public RestaurantViewHolder(View itemView) {
            super(itemView);
            this.root = itemView;
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name_text_view);
            restaurantRate = (TextView) itemView.findViewById(R.id.restaurant_rate_text_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // 在Fragment 当中启动 一个putExtra的Intent
//            EventBus.getDefault().post
//            EventBus.getDefault().post(new SendRstaurantNameEvent(restaurant.getName()));
//            Intent intent = DishActivity.newIntent(getActivity(), restaurant.getName());
//            startActivity(intent);
//            int temp =(int) v.getTag();
//            Log.d("WQWQ","你单击了第" + temp + "个元素");

        }
        @Override
        public boolean onLongClick(View v) {
            int temp =(int) v.getTag();
            Log.d("WQWQ","你*长按*了第" + temp + "个元素");
            // 返回 true 的时候，不会在长按事件之后 产生 点击事件
            return true;
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

    public class RestaurantAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pdialog;
        public RestaurantAsyncTask(Context context) {
//            pdialog = new ProgressDialog(context, 0);
//            pdialog.setButton("取消", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int i) {
//                    dialog.cancel();
//                }
//            });
//            pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                public void onCancel(DialogInterface dialog) {
//                    getActivity().finish();
//                }
//            });
//            pdialog.setCancelable(true);
//            pdialog.setMax(100);
//            pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pdialog.show();
//            ProgressBar progressBar = new ProgressBar(context);
//            progressBar
        }

        @Override
        protected void onPreExecute() {
            // 任务启动，可以在这里显示一个对话框，这里简单处理
//            message.setText(R.string.task_started);
        }
        @Override
        protected Void doInBackground(Void... params) {
//            fetchRestaurantData();

            //        final ArrayList<Restaurant> restaurants = new ArrayList<>();
            // Fetch data from website
            BmobQuery<Restaurant> query = new BmobQuery<Restaurant>();
            query.setLimit(10);
            query.findObjects(new FindListener<Restaurant>() {
                @Override
                public void done(List<Restaurant> object, BmobException e) {
//                    int temp = 0;
                    if (e == null) {
                        // Success
                        for (Restaurant restaurant : object) {
                            Log.d("WQWQ", restaurant.getName());
                            restaurants.add(new Restaurant(restaurant.getName(), restaurant.getRate()));
//                                EventBus.getDefault().post(new RestaurantReadyEvent(restaurant));
                            restaurantAdapter.notifyDataSetChanged();
//                            publishProgress(temp += 20);
//                            Thread.sleep(100);
                        }
                    } else {
                        // Fail
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
//                    pdialog.dismiss();

                }
            });

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度
//            System.out.println(""+values[0]);
//            message.setText(""+values[0]);
//            pdialog.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            _avatarProgressFrame.setVisibility(View.GONE);
//            mainPrograssBar.setVisibility(View.GONE);
        }
    }

    private void fetchRestaurantData() {
//        final ArrayList<Restaurant> restaurants = new ArrayList<>();
        // Fetch data from website
        BmobQuery<Restaurant> query = new BmobQuery<Restaurant>();
        query.setLimit(10);
        query.findObjects(new FindListener<Restaurant>() {
            @Override
            public void done(List<Restaurant> object, BmobException e) {
                if (e == null) {
                    // Success
                    for (Restaurant restaurant : object) {
                        Log.d("WQWQ", restaurant.getName());
                        restaurants.add(new Restaurant(restaurant.getName(), restaurant.getRate()));
//                                EventBus.getDefault().post(new RestaurantReadyEvent(restaurant));
                        restaurantAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Fail
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }

            }
        });
    }
}
// http://blog.csdn.net/liuhe688/article/details/6532519