package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.ServingDishGroup;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import com.example.bipain.boe_restaurantapp.utils.Util;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 10/06/2017.
 */

public class DishServeAdatper extends RecyclerView.Adapter<DishServeAdatper.ViewHolder> {
    List<ServingDishGroup> waiterNotifications;
    private Context mContext;

    public DishServeAdatper() {
        waiterNotifications = new ArrayList<>();
    }

    public void setData(List<ServingDishGroup> list) {
        this.waiterNotifications = list;
        notifyDataSetChanged();
    }

    public void addData(ServingDishGroup activity) {
        waiterNotifications.add(activity);
        notifyItemInserted(waiterNotifications.size());
    }

    public void removeData(WaiterNotification item) {
        final boolean[] isChange = {false};
        if (waiterNotifications != null) {
            waiterNotifications.stream().map(group -> {
                if (group.isContainerOrderDetailId(item.getOrderDetailId())) {
                    group.removeByOrderDetail(item.getOrderDetailId());
                    isChange[0] = true;
                }
                return group;
            });
        }
        if(isChange[0]){
            notifyDataSetChanged();
        }
//        int pos = findPos(item);
//        if (-1 != pos) {
//            waiterNotifications.remove(pos);
//            notifyItemRemoved(pos);
//        }

    }

    private int findPos(ServingDishGroup item) {
        for (int i = 0; i < waiterNotifications.size(); i++) {
            ServingDishGroup needItem = waiterNotifications.get(i);
//            if (item.getUid() == needItem.getUid()
//                    && item.getOrderDetailId() == needItem.getOrderDetailId()) {
//                return i;
//            }
        }
        return -1;
    }

    public void removeInDerictFromTable(List<TableGroupServe> listOD) {
        if (null != listOD) {
            if (listOD.size() > 0) {
                Log.d("dishremove", "list size " + listOD.size());
                for (int i = 0; i < listOD.size(); i++) {
                    int timeLoop = listOD.get(i).quantityCountInFragment;
                    boolean flagContinue = true;
                    while (false != flagContinue) {
                        int od = listOD.get(i).orderDetailId;
                        int postInAdapter = findPostByOrderDetailId(od);
                        Log.d("dishremove", "No. " + i
                                + "--" + "OD " + od + "--" + "posAd " + postInAdapter);
                        if (-1 != postInAdapter) {
                            Log.d("dishremove", "found");
                            listOD.get(i).quantityCountInFragment -= 1;
                            waiterNotifications.remove(postInAdapter);
                            notifyItemRemoved(postInAdapter);
                        } else {
                            timeLoop -= 1;
                            if (timeLoop == 0)
                                flagContinue = false;
                        }
                    }
                }
            }
        }
    }

    private int findPostByOrderDetailId(int dishId) {
        for (int i = 0; i < waiterNotifications.size(); i++) {
            ServingDishGroup needItem = waiterNotifications.get(i);
            if (dishId == needItem.getDishId()) {
                return i;
            }
        }
        return -1;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_dish_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServingDishGroup item = waiterNotifications.get(position);
        holder.tvDishNam.setText(item.getDishName());
        holder.tvTableNumber.setText(Util.setupTableGroupName(item.getGroupDishByTableList(), mContext));
//        holder.tvTableNumber.setText("Table No." + String.valueOf(item.ge()));
//        if (Constant.OVER_TIME == item.getType()) {
//            holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWarning));
//            item.setNotified();
//        } else {
//            holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//        }
    }

    @Override
    public int getItemCount() {
        return waiterNotifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDishName)
        TextView tvDishNam;
        @BindView(R.id.tvTableNumber)
        TextView tvTableNumber;
        @BindView(R.id.tvServe)
        TextView tvServe;
        @BindView(R.id.container)
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initView();
        }

        private void initView() {
            tvServe.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listner.onServeClick(pos, waiterNotifications.get(pos));
            });
        }
    }

    public void notifyLongTime(WaiterNotification notification) {
        int pos = waiterNotifications.indexOf(notification);
        notification.setOverTime();
        notifyItemChanged(pos);
    }

    public interface WaiterListner {
        void onServeClick(int id, ServingDishGroup notification);
    }

    private WaiterListner listner;

    public void setListner(WaiterListner listner) {
        this.listner = listner;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (null != recyclerView) {
            mContext = recyclerView.getContext();
        }
        super.onAttachedToRecyclerView(recyclerView);
    }
}
