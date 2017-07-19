package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import com.example.bipain.boe_restaurantapp.utils.Constant;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 10/06/2017.
 */

public class DishServeAdatper extends RecyclerView.Adapter<DishServeAdatper.ViewHolder> {
    List<WaiterNotification> waiterNotifications;
    private Context mContext;

    public DishServeAdatper() {
        waiterNotifications = new ArrayList<>();
    }

    public void setData(List<WaiterNotification> list) {
        this.waiterNotifications = list;
        notifyDataSetChanged();
    }

    public void addData(WaiterNotification activity) {
        waiterNotifications.add(activity);
        notifyItemInserted(waiterNotifications.size());
    }

    public void removeData(WaiterNotification item) {
        int pos = findPos(item);
        if (-1 != pos) {
            waiterNotifications.remove(pos);
            notifyItemRemoved(pos);
        }

    }

    private int findPos(WaiterNotification item) {
        for (int i = 0; i < waiterNotifications.size(); i++) {
            WaiterNotification needItem = waiterNotifications.get(i);
            if (item.getUid() == needItem.getUid()
                    && item.getOrderDetailId() == needItem.getOrderDetailId()) {
                return i;
            }
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

    private int findPostByOrderDetailId(int orderDetailId) {
        for (int i = 0; i < waiterNotifications.size(); i++) {
            WaiterNotification needItem = waiterNotifications.get(i);
            if (orderDetailId == needItem.getOrderDetailId()) {
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
        WaiterNotification item = waiterNotifications.get(position);
        holder.tvDishNam.setText(item.getDish().getDishName());
        holder.tvTableNumber.setText("Table No." + String.valueOf(item.getTableNumber()));
        if (Constant.OVER_TIME == item.getType()) {
            holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWarning));
            item.setNotified();
        } else {
            holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }
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
        void onServeClick(int id, WaiterNotification notification);
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
