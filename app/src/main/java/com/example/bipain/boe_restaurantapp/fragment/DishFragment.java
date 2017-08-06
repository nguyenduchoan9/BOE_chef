package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.bipain.boe_restaurantapp.DishInOrder;
import com.example.bipain.boe_restaurantapp.DishInQueue;
import com.example.bipain.boe_restaurantapp.DishQueueAdapter;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishFragment extends Fragment {

    private ListView lvDishInQueue;
    private DishQueueAdapter dishQueueAdapter;
    private LinkedList<DishInQueue> queueDish = new LinkedList<>();
    private ArrayList<DishInOrder> dishInOrders;
    private Button btDone;
    private RelativeLayout rlProcessing;

    StringBuilder listCompletedDish = new StringBuilder("");
    StringBuilder listCompletedDishLong = new StringBuilder("");

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queueDish = new LinkedList<>();
        dishInOrders = new ArrayList<>();
        setDishInOrders();
        dishQueueAdapter = new DishQueueAdapter(getActivity(), dishInOrders);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dish_fragment, container, false);
        lvDishInQueue = (ListView) view.findViewById(R.id.lvQueueDish);
        lvDishInQueue.setAdapter(dishQueueAdapter);
        btDone = (Button) view.findViewById(R.id.btDone);

        btDone.setOnClickListener(v -> {
            if (listCompletedDish.toString().trim().length() > 0) {
                showProcessing();
//                if (RetrofitUtils.checkNetworkAndToast(getContext())) {
                Services services = getService();
                String listDishParam = listCompletedDish.deleteCharAt(listCompletedDish.length() - 1).toString();
                listDishParam = listDishParam.startsWith(";") ? listDishParam.substring(1) : listDishParam;
                listCompletedDish = new StringBuilder(listDishParam);
                services.markListDishDone(listDishParam)
                        .enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if (response.isSuccessful()) {
                                    if (null != response.body()) {
                                        String[] listDish = listCompletedDish.toString().split(";");
                                        for (String cookedDish : listDish) {
                                            String[] id = cookedDish.split("_");
                                            int dishId = Integer.parseInt(id[1]);
                                            removeDishIsCooked(dishId);
                                        }
                                        listCompletedDish = new StringBuilder("");
                                    }
                                } else {
                                    if (response.code() == 500) {
                                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                                    } else {
                                        ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                                    }
                                }
                                hideProcessing();
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                hideProcessing();
                                ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                            }
                        });
            } else {
                hideProcessing();
//                    btDone.performClick();
            }
//            }

        });
        rlProcessing = (RelativeLayout) view.findViewById(R.id.rlProcessing);
        hideProcessing();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dishQueueAdapter.setListener(new DishQueueAdapter.DishQueueAdapterListener() {
            @Override
            public void onDoneClick(int dishId, Date identity) {
                listCompletedDish.append(String.valueOf(DishFragment.this.getOrderIdByDish(dishId)))
                        .append("_")
                        .append(String.valueOf(dishId))
                        .append(";");
                updateCheckInQueueDish(identity, true);

            }

            @Override
            public void onNotDoneClick(int dishId, Date identity) {
                String[] listDish = listCompletedDish.toString().split(";");
                if (listDish.length != 0 && !listCompletedDish.toString().equals("")) {
                    listCompletedDish = new StringBuilder("");
                    for (String cookedDish : listDish) {
                        String[] items = cookedDish.split("_");
                        String currentOrderID = String.valueOf(DishFragment.this.getOrderIdByDish(dishId));
                        if (!items[1].equals("" + dishId) && !currentOrderID.equals(items[0])) {
                            listCompletedDish.append(cookedDish)
                                    .append(";");
                        }
                    }
                }
                updateCheckInQueueDish(identity, false);
            }

            @Override
            public void onLongClick(int dishId) {
                handleServeAllDish(dishId);
            }

            @Override
            public void onKeepDishClick(int dishId) {
                doKeepOrderDetail(dishId);
            }

            @Override
            public void onCancelClick(int dishId) {
                doCancelOrderDetail(dishId);
            }
        });
    }

    private void doKeepOrderDetail(int dishId) {
        if (RetrofitUtils.checkNetworkAndToast(getContext())) {
            StringBuilder params = new StringBuilder();
            for (DishInQueue dishInQueue : queueDish) {
                if (dishId == dishInQueue.getDish().getDishId()) {
//                    if (dishInQueue.isOverMaterial()) {
                    params.append(dishInQueue.getDish()
                            .getOrderDetailId()).append("_");
//                    }
                }
            }
            String formatParams = params.deleteCharAt(params.length() - 1).toString();
            Services services = getService();
            services.postKeepOrderDetail(formatParams).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            Log.d("Hoang", "keep onResponse: successfully");
                            for (DishInQueue dishInQueue : queueDish) {
                                if (dishId == dishInQueue.getDish().getDishId()) {
                                    if (dishInQueue.isOverMaterial())
                                        dishInQueue.setOverMaterial(false);
                                }
                            }
                            refreshListViewDish();

                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        }
    }

    private void doCancelOrderDetail(int dishId) {
        LinkedList<DishInQueue> newQueue = new LinkedList<>();
        if (RetrofitUtils.checkNetworkAndToast(getContext())) {
            StringBuilder params = new StringBuilder();
            for (DishInQueue dishInQueue : queueDish) {
                if (dishId == dishInQueue.getDish().getDishId()) {
//                    if (dishInQueue.isOverMaterial()) {
                    params.append(dishInQueue.getDish()
                            .getOrderDetailId()).append("_");
//                    }
                } else {
                    newQueue.add(dishInQueue);
                }
            }
            String formatParams = params.deleteCharAt(params.length() - 1).toString();
            Services services = getService();
            services.postNotifyDishNotAvailable(formatParams).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            Log.d("Hoang", "cancel onResponse: successfully");
                            queueDish = newQueue;
                            refreshListViewDish();

                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        }

    }

    private void handleServeAllDish(int dishId) {
        prepareListIdParams(dishId);
        if (listCompletedDishLong.toString().trim().length() > 0) {
            showProcessing();
            Services services = getService();
            String listDishParam = listCompletedDishLong.deleteCharAt(listCompletedDishLong.length() - 1).toString();
            listDishParam = listDishParam.startsWith(";") ? listDishParam.substring(1) : listDishParam;
            listCompletedDishLong = new StringBuilder(listDishParam);
            services.markListDishDone(listDishParam)
                    .enqueue(new Callback<StatusResponse>() {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                            if (response.isSuccessful()) {
                                if (null != response.body()) {
                                    String[] listDish = listCompletedDishLong.toString().split(";");
                                    for (String cookedDish : listDish) {
                                        String[] id = cookedDish.split("_");
                                        int dishId = Integer.parseInt(id[1]);
                                        removeDishIsCooked(dishId);
                                    }
                                    listCompletedDishLong = new StringBuilder("");
                                }
                            } else {
                                if (response.code() == 500) {
                                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                                } else {
                                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                                }
                            }
                            hideProcessing();
                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                            hideProcessing();
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                        }
                    });
        } else {
            hideProcessing();
//                    btDone.performClick();
        }

    }

    private void prepareListIdParams(int dishId) {
        for (DishInQueue dishInQueue : queueDish) {
            if (dishInQueue.getDish().getDishId() == dishId) {
                listCompletedDishLong.append(String.valueOf(dishInQueue.getOrderId()))
                        .append("_")
                        .append(String.valueOf(dishId))
                        .append(";");
            }
        }
    }

    private void updateCheckInQueueDish(Date identify, boolean isChecked) {
        for (DishInQueue dishInQueue : queueDish) {
            if (dishInQueue.isMe(identify)) {
                Log.d("Hoang", "update iden succc");
                dishInQueue.setChecked(isChecked);
                break;
            }
        }
    }

    public void setDishInOrders() {
        if (null != queueDish && queueDish.size() > 0) {
            for (DishInQueue dishInQueue : queueDish) {
                int quantity = 0;
                DishInOrder dishInOrder = new DishInOrder();
                dishInOrder.setDish(dishInQueue.getDish());
                dishInOrder.setIdentity(dishInQueue.getIdentity());
                dishInOrder.setChecked(dishInQueue.isChecked());
                dishInOrder.setOverMaterial(dishInQueue.isOverMaterial());
                boolean isExited = false;
                for (DishInOrder dish : dishInOrders) {
                    if (dish.getDish().getDishId() == dishInOrder.getDish().getDishId()) {
                        isExited = true;
                        break;
                    }
                }
                if (!isExited) {
                    for (DishInQueue dish : queueDish) {
                        if (dish.getDish().getDishId() == dishInOrder.getDish().getDishId()) {
                            quantity += 1;
                            dishInOrder.addDescription(dish.getDescription());
                        }
                    }
                    Log.d("Hoang", "setDishInOrders: " + quantity + " - " + String.valueOf(dishInOrder.isChecked()));
                    dishInOrder.setQuantity(quantity);
                    dishInOrders.add(dishInOrder);
                }
            }
        }
    }

    public void refreshListViewDish() {
        dishInOrders.clear();
        setDishInOrders();
        dishQueueAdapter.setData(dishInOrders);
    }

    private int getOrderIdByDish(int dishId) {
        for (DishInQueue dishInQueue : queueDish) {
            if (dishInQueue.getDish().getDishId() == dishId) {
                return dishInQueue.getOrderId();
            }
        }
        return 0;
    }

    public void removeDishIsCooked(int dishId) {
        boolean isChange = false;
        LinkedList<DishInQueue> newQueueDish = new LinkedList<>();
        for (DishInQueue dishInQueue : queueDish) {
            if (dishInQueue.getDish().getDishId() != dishId) {
                newQueueDish.add(dishInQueue);
            } else {
                if (!isChange) {
                    isChange = true;
                } else {
                    newQueueDish.add(dishInQueue);
                }
            }
        }
        if (isChange) {
            queueDish = newQueueDish;
            refreshListViewDish();
        }
    }

    public void updateDishIsNotAvailable(List<Integer> integers) {
        boolean isChange = false;
        for (DishInQueue dishInQueue : queueDish) {
            if (integers.contains(dishInQueue.getDish().getOrderDetailId())) {
                dishInQueue.setOverMaterial(true);
                if (!isChange) isChange = true;
            }
        }
        if (isChange) {
            refreshListViewDish();
        }
    }

    public void addNewQueue(DishInQueue queue) {
        queue.setCurrentTime();
        queueDish.add(queue);
        refreshListViewDish();
    }

    public Services getService() {
        return ((TabManagerActivity) getActivity()).getServices();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constant.LOG_TAG, "Dish-onstart");
        setPos();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == queueDish) {
            queueDish = new LinkedList<>();
        }
        if (null == dishInOrders) {
            dishInOrders = new ArrayList<>();
        }
        Log.d(Constant.LOG_TAG, "Dish-onResume");
    }

    private void setPos() {
        ((TabManagerActivity) getActivity()).setFragmentPos(0);
    }

    public void onKeySearchChange(String term) {
        if (null != dishQueueAdapter) {
            dishQueueAdapter.getFilter().filter(term);
        }
    }

    public void updateDishNotAvailable(List<Integer> list) {
        if (null != list) {
            if (list.size() > 0) {
                updateDishIsNotAvailable(list);
//                notifyUpdateDishInChefComplete(items != null ? items : new ArrayList<Integer>());
            }
        }
    }

    private void notifyUpdateDishInChefComplete(List<Integer> list) {
        if (null != list) {
            if (list.size() > 0) {
                if (RetrofitUtils.checkNetworkAndToast(getContext())) {
                    StringBuilder params = new StringBuilder();
                    for (Integer id : list) {
                        params.append(String.valueOf(id))
                                .append("_");
                    }
                    String formatParams = params.deleteCharAt(params.length() - 1).toString();
                    Services services = getService();
                    services.postNotifyDishNotAvailable(formatParams).enqueue(new Callback<StatusResponse>() {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                            if (response.isSuccessful()) {
                                if (null != response.body()) {
                                    ToastUtils.toastShortMassage(DishFragment.this.getContext(), "Notify update Success");
                                }
                            } else {
                                if (response.code() == 500) {
                                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                                } else {
                                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                        }
                    });
                } else {
//            notifyUpdateDishInChefComplete(list);
                }
            }
        }
    }

    public void cancelDish(List<Integer> orderDetailIds) {
        List<DishInQueue> removeData = new ArrayList<>();
        for (DishInQueue dishInQueue : queueDish) {
            if (orderDetailIds.contains(dishInQueue.getDish().getOrderDetailId())) {
                removeData.add(dishInQueue);
            }
        }
        if (removeData.size() > 0) {
            queueDish.removeAll(removeData);
            refreshListViewDish();
        }
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }
}
