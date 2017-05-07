package org.xuxiaoxiao.order.ordereddishes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.model.Dish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by WuQiang on 2017/5/2.
 */

public class OrderedDishesFragment extends Fragment {
    @BindView(R.id.confirm_button) Button confirmButton;
    @BindView(R.id.continue_button) Button continueButton;
    @BindView(R.id.cancel_button) Button cancelButton;

    private static final String ORDERED_DISHES = "org.xuxiaoxiao.ordereddishesfragment.ordered_dishes";
    // 传送过来的点了的菜品，第一个元素是饭店的名字
    private String[] orderedDishesArray;
    ProgressBar progressBar;
    // 用于存放下载完成之后的 Dishes
    ArrayList<Dish> orderDishes = new ArrayList<>();
    private loadOrderedDishes asyncTask;

    RecyclerView recyclerView;
    OrderDishesAdapter orderDishesAdapter;
    Utility myUtility;

    public static Fragment newInstance(String[] orderedDishesArray) {
        Bundle args = new Bundle();
        args.putStringArray(ORDERED_DISHES, orderedDishesArray);
        OrderedDishesFragment orderedDishesFragment = new OrderedDishesFragment();
        orderedDishesFragment.setArguments(args);
        return orderedDishesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderedDishesArray = getArguments().getStringArray(ORDERED_DISHES);
//        for (String s : orderedDishesArrayList) {
//            Log.d("WQWQ", s);
//        }
//        Log.d("WQWQ", getClass().getSimpleName());
//        myUtility = new Utility();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordered_dishes, container, false);
        ButterKnife.bind(this,view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        // 异步加载必须放到这儿，要不 ProgressBar 还是 Null
        asyncTask = new loadOrderedDishes(progressBar);
        asyncTask.execute(orderedDishesArray);//将图片url作为参数传入到doInBackground()中
        recyclerView = (RecyclerView) view.findViewById(R.id.ordered_dishes_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        orderDishesAdapter = new OrderDishesAdapter();
        recyclerView.setAdapter(orderDishesAdapter);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("back3","confirm");
                getActivity().setResult(31, i);
                getActivity().finish();
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("back3","continue");
                getActivity().setResult(32, i);
                getActivity().finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("back3","continue");
                getActivity().setResult(33, i);
                getActivity().finish();
            }
        });
        return view;
    }

    public class loadOrderedDishes extends AsyncTask<String[], ArrayList<Dish>,Void> {
        private ProgressBar progressBar;//进度条

        public loadOrderedDishes(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
//            Log.d("WQWQ", "onPreExecuteonPreExecuteonPreExecuteonPreExecute");

        }

        @Override
        protected Void doInBackground(String[]... params) {
            final ArrayList<Dish> asyncOrderDishes = new ArrayList<>();
            // Fetch data from website
            // BmobQuery<Dish> 就限定了查询的表的名字
            BmobQuery<Dish> query = new BmobQuery<Dish>();
            query.setLimit(20);
            // 添加限定条件 restaurantNme(饭店的名字)与name(菜名)：菜名这个限定条件是一个字符串数组
            query.addWhereEqualTo("restaurantName", params[0][0]);
            // 查询条件必须是数组，所以需要转换一下，从ArrayList<String>当中生成一个数组
            String[] names = new String[params[0].length - 1];
            for (int i = 0; i < params[0].length - 1; i++) {
                names[i] = params[0][i + 1];
            }
            query.addWhereContainedIn("name", Arrays.asList(names));
            query.findObjects(new FindListener<Dish>() {
                @Override
                public void done(List<Dish> object, BmobException e) {
                    if (e == null) {
                        // Success
                        for (Dish dish : object) {
                            if (isCancelled())
                                break;

                            Log.d("WQWQ", "查询出来的结果object的长度" + object.size());
//                            asyncOrderDishes.add(new Dish(dish.getName(), dish.getPrice(), dish.getDiscription(), dish.getPhotoUrl(), dish.getRestaurantName()));
                            asyncOrderDishes.add(dish);
//
                            //调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                            // 我是是在应该更新进度条的时候，传送数据的，做一个判断，是为了只传送一次
                            if (asyncOrderDishes.size() == object.size()) {
                                publishProgress(asyncOrderDishes);
//                                myUtility.receiveDataFromAsynck(asyncOrderDishes);
                            }
                        }
                    } else {
                        // Fail
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate( ArrayList<Dish>... values) {
            // 把下载完成的数据传给全局变量，并通知 Adapter
            if (!isCancelled()) {
                orderDishes = values[0];
                orderDishesAdapter.notifyDataSetChanged();
                values[0] = null;
                progressBar.setVisibility(View.GONE);
            }
        }

    }

    private class OrderDishesAdapter extends RecyclerView.Adapter<OrderedDishesViewHolder> {

        @Override
        public OrderedDishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.ordered_dishes_item,parent,false);
            return new OrderedDishesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderedDishesViewHolder holder, int position) {
            holder.bind(orderDishes.get(position));

        }

        @Override
        public int getItemCount() {
            return orderDishes.size();
        }
    }

    private class OrderedDishesViewHolder extends RecyclerView.ViewHolder {
        ImageView oredredDishesImageView;
        Dish dish;

        public OrderedDishesViewHolder(View itemView) {
            super(itemView);
            oredredDishesImageView = (ImageView)itemView.findViewById(R.id.ordered_dishes_image_view);
        }

        public void bind(Dish dish) {
            this.dish = dish;
            Glide
                    .with(getActivity())
                    .load(dish.getPhoto().getFileUrl())
                    .centerCrop()
//                    .placeholder(R.drawable.error)
                    .crossFade()
                    .into(oredredDishesImageView);
        }
    }

    public class Utility {
        public Utility() {
        }

        public void receiveDataFromAsynck(ArrayList<Dish> values) {
            progressBar.setVisibility(View.GONE);
            for (Dish dish : values) {
                Log.d("WQWQ", "下载完成了：" + dish.getDiscription());
            }
        }
    }
}
/**
 * 当然，你可以在你的查询操作中添加多个约束条件，来查询符合要求的数据。
 * <p>
 * query.addWhereNotEqualTo("playerName", "Barbie");     //名字不等于Barbie
 * query.addWhereGreaterThan("score", 60);                //条件：分数大于60岁
 * <p>
 * <p>
 * 子查询
 * <p>
 * 如果你想查询匹配几个不同值的数据，如：要查询“Barbie”,“Joe”,“Julia”三个人的成绩时，你可以使用addWhereContainedIn方法来实现。
 * <p>
 * String[] names = {"Barbie", "Joe", "Julia"};
 * query.addWhereContainedIn("playerName", Arrays.asList(names));
 * <p>
 * 相反，如果你想查询排除“Barbie”,“Joe”,“Julia”这三个人的其他同学的信息，你可以使用addWhereNotContainedIn方法来实现。
 * <p>
 * <p>
 * String[] names = {"Barbie", "Joe", "Julia"};
 * query.addWhereNotContainedIn("playerName", Arrays.asList(names));
 */