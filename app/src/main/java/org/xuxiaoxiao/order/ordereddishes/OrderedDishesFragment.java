package org.xuxiaoxiao.order.ordereddishes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class OrderedDishesFragment extends Fragment{

    private static final String ORDERED_DISHES = "org.xuxiaoxiao.ordereddishesfragment.ordered_dishes";
    // 传送过来的点了的菜品，第一个元素是饭店的名字
    private ArrayList<String> orderedDishesArrayList = new ArrayList<>();
    ProgressBar progressBar;
    // 用于存放下载完成之后的 Dishes
    ArrayList<Dish> orderDishes = new ArrayList<>();
    private loadOrderedDishes asyncTask;

    public static Fragment newInstance(ArrayList<String> orderedDishesArrayList) {
        Bundle args = new Bundle();
        args.putStringArrayList(ORDERED_DISHES,orderedDishesArrayList);
        OrderedDishesFragment orderedDishesFragment = new OrderedDishesFragment();
        orderedDishesFragment.setArguments(args);
        return orderedDishesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderedDishesArrayList = getArguments().getStringArrayList(ORDERED_DISHES);
        for (String s : orderedDishesArrayList){
            Log.d("WQWQ",s);
        }
        Log.d("WQWQ",getClass().getSimpleName());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordered_dishes,container,false);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        // 异步加载必须放到这儿，要不 ProgressBar 还是 Null
        asyncTask = new loadOrderedDishes(progressBar);
        asyncTask.execute(orderedDishesArrayList);//将图片url作为参数传入到doInBackground()中
        return view;
    }
    public class loadOrderedDishes extends AsyncTask<ArrayList<String>,Integer,ArrayList<Dish>>{
        private ProgressBar progressBar;//进度条

        public loadOrderedDishes(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Dish> doInBackground(ArrayList<String>... params) {
            for (String s :params[0]){
                Log.d("WQWQ_doInBackground",s);
            }

            // Fetch data from website
            // BmobQuery<Dish> 就限定了查询的表的名字
            BmobQuery<Dish> query = new BmobQuery<Dish>();
            query.setLimit(20);
            // 添加限定条件 restaurantNme(饭店的名字)与name(菜名)：菜名这个限定条件是一个字符串数组
            query.addWhereEqualTo("restaurantName", params[0].get(0));
            // 查询条件必须是数组，所以需要转换一下，从ArrayList<String>当中生成一个数组
            String[] names = new String[params[0].size()-1];
            for (int i =0;i<params[0].size()-1;i++){
                names[i] = params[0].get(i+1);
            }
//            String[] names = {"QingHeYuan", "清和元的菜"};
            query.addWhereContainedIn("name", Arrays.asList(names));
            query.findObjects(new FindListener<Dish>() {
                @Override
                public void done(List<Dish> object, BmobException e) {
                    if (e == null) {
                        // Success
                        for (Dish dish : object) {
                                Log.d("WQWQ","查询出来的结果：" + dish.getPrice());


//                            dishes.add(new Dish(dish.getName(), dish.getPrice(), dish.getDiscription(), dish.getPhotoUrl(), dish.getRestaurantName()));
////                            EventBus.getDefault().post(new DishReadyEvent(dish));
//                            dishesAdapter.notifyDataSetChanged();

                        }
                    } else {
                        // Fail
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });

            return null;
        }
    }
}
/**
 * 当然，你可以在你的查询操作中添加多个约束条件，来查询符合要求的数据。

 query.addWhereNotEqualTo("playerName", "Barbie");     //名字不等于Barbie
 query.addWhereGreaterThan("score", 60);                //条件：分数大于60岁


 子查询

 如果你想查询匹配几个不同值的数据，如：要查询“Barbie”,“Joe”,“Julia”三个人的成绩时，你可以使用addWhereContainedIn方法来实现。

 String[] names = {"Barbie", "Joe", "Julia"};
 query.addWhereContainedIn("playerName", Arrays.asList(names));

 相反，如果你想查询排除“Barbie”,“Joe”,“Julia”这三个人的其他同学的信息，你可以使用addWhereNotContainedIn方法来实现。


 String[] names = {"Barbie", "Joe", "Julia"};
 query.addWhereNotContainedIn("playerName", Arrays.asList(names));
 */