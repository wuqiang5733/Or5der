package org.xuxiaoxiao.order.ordereddishes;

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
import android.widget.ProgressBar;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.model.Dish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by WuQiang on 2017/5/2.
 */

public class OrderedDishesFragment extends Fragment {

    private static final String ORDERED_DISHES = "org.xuxiaoxiao.ordereddishesfragment.ordered_dishes";
    // 传送过来的点了的菜品，第一个元素是饭店的名字
    private ArrayList<String> orderedDishesArrayList = new ArrayList<>();
    ProgressBar progressBar;
    // 用于存放下载完成之后的 Dishes
    ArrayList<Dish> orderDishes = new ArrayList<>();
    private loadOrderedDishes asyncTask;

    RecyclerView recyclerView;
    OrderDishesAdapter orderDishesAdapter;
    Unity myUnity;

    public static Fragment newInstance(ArrayList<String> orderedDishesArrayList) {
        Bundle args = new Bundle();
        args.putStringArrayList(ORDERED_DISHES, orderedDishesArrayList);
        OrderedDishesFragment orderedDishesFragment = new OrderedDishesFragment();
        orderedDishesFragment.setArguments(args);
        return orderedDishesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderedDishesArrayList = getArguments().getStringArrayList(ORDERED_DISHES);
        for (String s : orderedDishesArrayList) {
            Log.d("WQWQ", s);
        }
        Log.d("WQWQ", getClass().getSimpleName());
        myUnity = new Unity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordered_dishes, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        // 异步加载必须放到这儿，要不 ProgressBar 还是 Null
        asyncTask = new loadOrderedDishes(progressBar);
        asyncTask.execute(orderedDishesArrayList);//将图片url作为参数传入到doInBackground()中
        recyclerView = (RecyclerView) view.findViewById(R.id.ordered_dishes_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    public class loadOrderedDishes extends AsyncTask<ArrayList<String>, ArrayList<Dish>,Void> {
        private ProgressBar progressBar;//进度条

        public loadOrderedDishes(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Log.d("WQWQ", "onPreExecuteonPreExecuteonPreExecuteonPreExecute");

        }

        @Override
        protected Void doInBackground(ArrayList<String>... params) {
            final ArrayList<Dish> asyncOrderDishes = new ArrayList<>();
            // Fetch data from website
            // BmobQuery<Dish> 就限定了查询的表的名字
            BmobQuery<Dish> query = new BmobQuery<Dish>();
            query.setLimit(20);
            // 添加限定条件 restaurantNme(饭店的名字)与name(菜名)：菜名这个限定条件是一个字符串数组
            query.addWhereEqualTo("restaurantName", params[0].get(0));
            // 查询条件必须是数组，所以需要转换一下，从ArrayList<String>当中生成一个数组
            String[] names = new String[params[0].size() - 1];
            for (int i = 0; i < params[0].size() - 1; i++) {
                names[i] = params[0].get(i + 1);
            }
            query.addWhereContainedIn("name", Arrays.asList(names));
            query.findObjects(new FindListener<Dish>() {
                @Override
                public void done(List<Dish> object, BmobException e) {
                    if (e == null) {
                        // Success
                        for (Dish dish : object) {
                            Log.d("WQWQ", "查询出来的结果object的长度" + object.size());
                            asyncOrderDishes.add(new Dish(dish.getName(), dish.getPrice(), dish.getDiscription(), dish.getPhotoUrl(), dish.getRestaurantName()));
                            Log.d("WQWQ", "生成每一条查询结果的长度：" + asyncOrderDishes.size());
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            //调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                            if (asyncOrderDishes.size() == object.size()) {
                                publishProgress(asyncOrderDishes);
                            }
//                            myUnity.MyUpdateProgressBar((int) ((asyncOrderDishes.size() / (float) object.size()) * 100));
                        }
                    } else {
                        // Fail
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
            Log.d("WQWQ", "返回之前的长度：" + asyncOrderDishes.size());
            return null;
        }

        @Override
        protected void onProgressUpdate( ArrayList<Dish>... values) {
//            int vlaue = values[0];
            Log.d("WQWQ", "更新进度条---更新进度条--更新进度条");
            progressBar.setVisibility(View.GONE);
            for (Dish dish : values[0]) {
                Log.d("WQWQ", "下载完成了：" + dish.getDiscription());
            }
//            progressBar.setProgress(vlaue);
        }

        //onProgressUpdate方法用于更新进度信息
//        @Override
//        protected void onProgressUpdate(Integer... progresses) {
//            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
//            progressBar.setProgress(progresses[0]);
//            textView.setText("loading..." + progresses[0] + "%");
//        }

//        @Override
//        protected void onPostExecute(ArrayList<Dish> dishes) {
//            super.onPostExecute(dishes);
//            orderDishes = dishes;
//            for (Dish dish : dishes) {
//                Log.d("WQWQ", "下载完成了：" + dish.getDiscription());
//            }
//            Log.d("WQWQ", "onPostExecute后后后=+=+=+onPostExecute后后后=+=+=+onPostExecute后后后=+=+=+onPostExecute后后后=+=+=+");
////            progressBar.setVisibility(View.GONE);
//        }
    }

    private class OrderDishesAdapter extends RecyclerView.Adapter<OrderedDishesViewHolder> {

        @Override
        public OrderedDishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(OrderedDishesViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class OrderedDishesViewHolder extends RecyclerView.ViewHolder {

        public OrderedDishesViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class Unity {
        public Unity() {
        }

        public void MyUpdateProgressBar(int para) {
            progressBar.setProgress(para);

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