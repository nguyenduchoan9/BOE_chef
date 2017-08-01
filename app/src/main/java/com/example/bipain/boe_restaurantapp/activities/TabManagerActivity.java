package com.example.bipain.boe_restaurantapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.bipain.boe_restaurantapp.Category;
import com.example.bipain.boe_restaurantapp.DishInOrder;
import com.example.bipain.boe_restaurantapp.DishInQueue;
import com.example.bipain.boe_restaurantapp.QueueOrder;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.adapter.PagerFragmentAdapter;
import com.example.bipain.boe_restaurantapp.fragment.DishFragment;
import com.example.bipain.boe_restaurantapp.fragment.LanguageDialog;
import com.example.bipain.boe_restaurantapp.fragment.OrderFragment;
import com.example.bipain.boe_restaurantapp.gcm.GCMIntentService;
import com.example.bipain.boe_restaurantapp.gcm.GCMRegistrationIntentService;
import com.example.bipain.boe_restaurantapp.model.Dish;
import com.example.bipain.boe_restaurantapp.model.User;
import com.example.bipain.boe_restaurantapp.request.NotificationResponse;
import com.example.bipain.boe_restaurantapp.request.SessionDeleteResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;
import com.example.bipain.boe_restaurantapp.utils.EndpointManager;
import com.example.bipain.boe_restaurantapp.utils.PreferencesManager;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import com.example.bipain.boe_restaurantapp.utils.Util;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TabManagerActivity extends AppCompatActivity {
    private BroadcastReceiver mBroadcastReceiver;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;
    private LinkedList<QueueOrder> orders;
    private LinkedList<DishInQueue> queueDish;
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Dish> dishes = new ArrayList<>();
    private Gson gson = new Gson();
    private int fragmentPos;
    PagerFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.handleSelectLanguage(this, Util.getLanguage(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_manager);

        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);

        setBroadcastReceiver();

//        services.getUserProfile("abc", "id").enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        User user = response.body();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
        queueDish = new LinkedList<>();
        orders = new LinkedList<>();
        categories = new ArrayList<>();
        dishes = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_toolbar_title_chef);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new PagerFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentPos = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
//        viewPager.getH
        setOrders();
        setQueueDish();
        setCategories();
        setDishes();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onLogOut();
        return true;
    }

    private void onLogOut() {
        if (RetrofitUtils.checkNetworkAndToast(TabManagerActivity.this)) {
            services.logout(preferencesManager.getUser().getId()).enqueue(new Callback<SessionDeleteResponse>() {
                @Override
                public void onResponse(Call<SessionDeleteResponse> call, Response<SessionDeleteResponse> response) {
                    if (response.isSuccessful()) {
                        preferencesManager.logOut();
                        startActivity(LoginActivity.newInstance(TabManagerActivity.this));
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(TabManagerActivity.this, getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(TabManagerActivity.this, getString(R.string.text_response_error_msg));
                        }
                    }
                }

                @Override
                public void onFailure(Call<SessionDeleteResponse> call, Throwable t) {
                    ToastUtils.toastLongMassage(TabManagerActivity.this, getString(R.string.text_response_error_connection));
                }
            });
        }
    }

    public LinkedList<QueueOrder> getOrders() {
        return orders;
    }

    public void setOrders() {
        Dish dish1 = new Dish(1, "Vit Quay Bac Kinh", 1);
        Dish dish2 = new Dish(2, "Ngheu xao sa ot", 1);
        Dish dish3 = new Dish(3, "Oc quan xao dua", 1);
        Dish dish4 = new Dish(4, "Be Thui Cau Mong", 1);
        Dish dish5 = new Dish(5, "Banh trang cuon thit heo 2 dau da", 1);
        Dish dish6 = new Dish(6, "De Nuong Nam Dinh", 1);
        Dish dish7 = new Dish(7, "Ga deo le gion cay", 1);
        Dish dish8 = new Dish(8, "Muc ham ruou sa ke", 1);
        Dish dish9 = new Dish(9, "Vit Tiem thuoc bac", 1);

        DishInOrder dishInOrder1 = new DishInOrder(1, dish1);
        DishInOrder dishInOrder2 = new DishInOrder(1, dish2);
        DishInOrder dishInOrder3 = new DishInOrder(3, dish4);
        DishInOrder dishInOrder4 = new DishInOrder(2, dish3);
        DishInOrder dishInOrder5 = new DishInOrder(1, dish5);
        DishInOrder dishInOrder6 = new DishInOrder(3, dish9);
        DishInOrder dishInOrder7 = new DishInOrder(1, dish8);
        DishInOrder dishInOrder8 = new DishInOrder(4, dish6);
        DishInOrder dishInOrder9 = new DishInOrder(5, dish7);

        ArrayList<DishInOrder> orderDetail1 = new ArrayList<>();
        orderDetail1.add(dishInOrder1);
        orderDetail1.add(dishInOrder2);

        ArrayList<DishInOrder> orderDetail2 = new ArrayList<>();
        orderDetail2.add(dishInOrder3);
        orderDetail2.add(dishInOrder4);
        orderDetail2.add(dishInOrder5);

        ArrayList<DishInOrder> orderDetail3 = new ArrayList<>();
        orderDetail3.add(dishInOrder6);

        ArrayList<DishInOrder> orderDetail4 = new ArrayList<>();
        orderDetail4.add(dishInOrder7);
        orderDetail4.add(dishInOrder8);
        orderDetail4.add(dishInOrder9);

        orders = new LinkedList<>();
        QueueOrder queueOrder1 = new QueueOrder(1, 2, 2, orderDetail1);
        QueueOrder queueOrder2 = new QueueOrder(2, 3, 6, orderDetail2);
        QueueOrder queueOrder3 = new QueueOrder(3, 1, 3, orderDetail3);
        QueueOrder queueOrder4 = new QueueOrder(4, 2, 5, orderDetail4);
        orders.add(queueOrder1);
        orders.add(queueOrder2);
        orders.add(queueOrder3);
        orders.add(queueOrder4);
    }

    public void setQueueDish() {
        Dish dish1 = new Dish(1, "Vit Quay Bac Kinh", 1);
        Dish dish2 = new Dish(2, "Ngheu xao sa ot", 1);
        Dish dish3 = new Dish(3, "Oc quan xao dua", 1);
        Dish dish4 = new Dish(4, "Be Thui Cau Mong", 1);
        Dish dish5 = new Dish(5, "Banh trang cuon thit heo 2 dau da", 1);
        Dish dish6 = new Dish(6, "De Nuong Nam Dinh", 1);
        Dish dish7 = new Dish(7, "Ga deo le gion cay", 1);
        Dish dish8 = new Dish(8, "Muc ham ruou sa ke", 1);
        Dish dish9 = new Dish(9, "Vit Tiem thuoc bac", 1);

        DishInQueue dishInQueue = new DishInQueue(dish1, 1);
        DishInQueue dishInQueue1 = new DishInQueue(dish2, 1);
        DishInQueue dishInQueue2 = new DishInQueue(dish3, 1);
        DishInQueue dishInQueue3 = new DishInQueue(dish3, 2);
        DishInQueue dishInQueue4 = new DishInQueue(dish4, 2);
        DishInQueue dishInQueue5 = new DishInQueue(dish4, 2);
        DishInQueue dishInQueue6 = new DishInQueue(dish7, 3);
        DishInQueue dishInQueue7 = new DishInQueue(dish3, 3);

        queueDish.add(dishInQueue);
        queueDish.add(dishInQueue1);
        queueDish.add(dishInQueue2);
        queueDish.add(dishInQueue3);
        queueDish.add(dishInQueue4);
        queueDish.add(dishInQueue5);
        queueDish.add(dishInQueue6);
        queueDish.add(dishInQueue7);
    }

    public void setCategories() {
        Category category = new Category(1, "Mon Xao");
        Category category1 = new Category(2, "Mon Canh");
        Category category2 = new Category(3, "Mon Khai Vi");
        Category category3 = new Category(4, "Mon Rau");
        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
    }

    public void setDishes() {
        Dish dish1 = new Dish(1, "Vit Quay Bac Kinh", 1);
        Dish dish2 = new Dish(2, "Ngheu xao sa ot", 1);
        Dish dish3 = new Dish(3, "Oc quan xao dua", 1);
        Dish dish4 = new Dish(4, "Be Thui Cau Mong", 2);
        Dish dish5 = new Dish(5, "Banh trang cuon thit heo 2 dau da", 2);
        Dish dish6 = new Dish(6, "De Nuong Nam Dinh", 3);
        Dish dish7 = new Dish(7, "Ga deo le gion cay", 3);
        Dish dish8 = new Dish(8, "Muc ham ruou sa ke", 3);
        Dish dish9 = new Dish(9, "Vit Tiem thuoc bac", 4);

        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);
        dishes.add(dish4);
        dishes.add(dish5);
        dishes.add(dish6);
        dishes.add(dish7);
        dishes.add(dish8);
        dishes.add(dish9);
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, TabManagerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public LinkedList<DishInQueue> getQueueDish() {
        return queueDish;
    }

    public void setQueueDish(LinkedList<DishInQueue> queueDish) {
        this.queueDish = queueDish;
    }

    private final static String GCM_TOKEN = "GCM_TOKEN";

    protected void setBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    services.registerRegToken(token).enqueue(new Callback<NotificationResponse>() {
                        @Override
                        public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                            if (response.isSuccessful()) {
                                if (null != response.body()) {
                                    Log.v("GCM-register", "Success");
                                    ToastUtils.toastLongMassage(TabManagerActivity.this,
                                            "reg token is sucess");
                                }
                            } else {
                                Log.v("GCM-register", "fail");
                                ToastUtils.toastLongMassage(TabManagerActivity.this,
                                        "reg token is fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationResponse> call, Throwable t) {

                        }
                    });
                    Log.v(GCM_TOKEN, token);
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(TabManagerActivity.this,
                            "GCM registration error");
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_CHEF_ORDER)) {
                    String body = intent.getStringExtra("body");
                    QueueOrder order = gson.fromJson(body, QueueOrder.class);
                    order.setNumberDish(order.getOrderDetail().size());
                    order.setTotal(getTotal(order.getOrderDetail()));
//                    if (null != adapter) {
//                        OrderFragment fragment = (OrderFragment) adapter.getItem(1);
//                        fragment.addOrder(order);
//                    }
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_CHEF_DISH)) {
                    String body = intent.getStringExtra("body");
                    DishInQueue queue = gson.fromJson(body, DishInQueue.class);
                    if (null != adapter) {
                        DishFragment fragment = (DishFragment) adapter.getItem(0);
                        fragment.addNewQueue(queue);
                    }
                } else {
                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
                }
            }
        };

        Intent intent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_CHEF_DISH));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_CHEF_ORDER));
        tabLayout.getTabAt(0).select();
        fragmentPos = 0;
    }

    public int getTotal(ArrayList<DishInOrder> dishInOrders) {
        int total = 0;
        for (DishInOrder dish : dishInOrders) {
            total += dish.getQuantity();
        }
        return total;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ZZZZZZZ", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    public Services getServices() {
        return services;
    }

    public void setFragmentPos(int pos) {
        fragmentPos = pos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        MenuItem language = menu.findItem(R.id.action_language);
        openSelectLanguageDialog(language);
        setUpSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void openSelectLanguageDialog(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(item -> {
            FragmentManager fm = getSupportFragmentManager();
            LanguageDialog dialog = LanguageDialog.newInstance(preferencesManager.getLanguage());
            dialog.setmListener(new LanguageDialog.LanguageListener() {
                                    @Override
                                    public void onEnglishSelect() {
                                        preferencesManager.setLanguage(Constant.EN_LANGUAGE_STRING);
                                        refreshViewAfterChangeLanguage();
                                    }

                                    @Override
                                    public void onVNSelect() {
                                        preferencesManager.setLanguage(Constant.VI_LANGUAGE_STRING);
                                        refreshViewAfterChangeLanguage();
                                    }
                                }
            );
            dialog.show(fm, "putang");
            return true;
        });
    }

    private void refreshViewAfterChangeLanguage() {
        finish();
        startActivity(new Intent(this, TabManagerActivity.class));
    }

    private SearchView mSearchView;

    private void setUpSearchView(MenuItem menuItem) {
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.text_searching));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (0 == fragmentPos) {
                    DishFragment f = (DishFragment) adapter.getItem(fragmentPos);
                    f.onKeySearchChange(newText);
                }
                return true;
            }
        });
    }

    public void notifyDishNotAvailable(List<Integer> integerList) {
        if (null != adapter) {
            DishFragment fragment = (DishFragment) adapter.getItem(0);
            fragment.updateDishNotAvailable(integerList);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Util.getLanguage(newBase)));
    }
}
