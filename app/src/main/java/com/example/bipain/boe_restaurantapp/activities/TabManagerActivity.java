package com.example.bipain.boe_restaurantapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.bipain.boe_restaurantapp.Category;
import com.example.bipain.boe_restaurantapp.DishInOrder;
import com.example.bipain.boe_restaurantapp.QueueDish;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.adapter.PagerFragmentAdapter;
import com.example.bipain.boe_restaurantapp.fragment.DishFragment;
import com.example.bipain.boe_restaurantapp.fragment.MenuFragment;
import com.example.bipain.boe_restaurantapp.fragment.OrderFragment;
import com.example.bipain.boe_restaurantapp.model.Dish;
import com.example.bipain.boe_restaurantapp.model.User;
import com.example.bipain.boe_restaurantapp.request.SessionDeleteResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.EndpointManager;
import com.example.bipain.boe_restaurantapp.utils.PreferencesManager;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TabManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;
    HashMap<Integer, ArrayList<DishInOrder>> orders;
    QueueDish queueDish;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Dish> dishes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_manager);

        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);

        services.getUserProfile("abc", "id").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        User user = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        queueDish = new QueueDish();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager()));

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setOrders();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onLogOut();
        return true;
    }

    private void onLogOut() {
        services.logout(preferencesManager.getUser().getId()).enqueue(new Callback<SessionDeleteResponse>() {
            @Override
            public void onResponse(Call<SessionDeleteResponse> call, Response<SessionDeleteResponse> response) {
                if (response.isSuccessful()) {
                    preferencesManager.logOut();
                    startActivity(LoginActivity.newInstance(TabManagerActivity.this));
                }
            }

            @Override
            public void onFailure(Call<SessionDeleteResponse> call, Throwable t) {

            }
        });
    }

    public HashMap<Integer, ArrayList<DishInOrder>> getOrders() {
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
        orderDetail1.add(dishInOrder2);
        orderDetail1.add(dishInOrder2);
        orderDetail1.add(dishInOrder2);
        orderDetail1.add(dishInOrder2);
        orderDetail1.add(dishInOrder2);
        orderDetail1.add(dishInOrder2);
        orderDetail1.add(dishInOrder2);

        ArrayList<DishInOrder> orderDetail2 = new ArrayList<>();
        orderDetail2.add(dishInOrder3);
        orderDetail2.add(dishInOrder4);
        orderDetail2.add(dishInOrder5);
        orderDetail2.add(dishInOrder5);
        orderDetail2.add(dishInOrder5);
        orderDetail2.add(dishInOrder5);
        orderDetail2.add(dishInOrder5);

        ArrayList<DishInOrder> orderDetail3 = new ArrayList<>();
        orderDetail3.add(dishInOrder6);

        ArrayList<DishInOrder> orderDetail4 = new ArrayList<>();
        orderDetail4.add(dishInOrder7);
        orderDetail4.add(dishInOrder8);
        orderDetail4.add(dishInOrder8);
        orderDetail4.add(dishInOrder8);
        orderDetail4.add(dishInOrder8);
        orderDetail4.add(dishInOrder9);

        orders = new HashMap<>();
        orders.put(1, orderDetail1);
        orders.put(2, orderDetail2);
        orders.put(3, orderDetail3);
        orders.put(4, orderDetail4);
        orders.put(5, orderDetail4);
        orders.put(6, orderDetail4);
        orders.put(7, orderDetail4);
        orders.put(8, orderDetail4);
        orders.put(9, orderDetail4);
        orders.put(10, orderDetail4);
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

    public QueueDish getQueueDish() {
        return queueDish;
    }

    public void setQueueDish(QueueDish queueDish) {
        this.queueDish = queueDish;
    }
}
