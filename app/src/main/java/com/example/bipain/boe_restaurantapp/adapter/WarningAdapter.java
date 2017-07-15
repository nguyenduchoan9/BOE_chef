package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
 * Created by hoang on 29/06/2017.
 */

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder> {
    List<WaiterNotification> waiterNotifications;
    private Context mContext;

    public WarningAdapter() {
        waiterNotifications = new ArrayList<>();
    }

    public void setData(List<WaiterNotification> list) {
        this.waiterNotifications = list;
        notifyDataSetChanged();
    }

    public void addData(WaiterNotification activity) {
//        int pos = waiterNotifications.size();
        waiterNotifications.add(activity);
        notifyItemInserted(waiterNotifications.size());
//        notifyItemRangeChanged(pos, waiterNotifications.size());
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
            if (item.getTiming() == needItem.getTiming()
                    && item.getOrderDetailId() == item.getOrderDetailId()) {
                return i;
            }
        }
        return -1;
    }

    public void removeInDerictFromTable(List<TableGroupServe> listOD) {
        if (null != listOD) {
            if (listOD.size() > 0) {
                for (int i = 0; i < listOD.size(); i++) {
                    int timeLoop = listOD.get(i).quantityCountInFragment;
                    boolean flagContinue = true;
                    while (false != flagContinue) {
                        int od = listOD.get(i).orderDetailId;
                        int postInAdapter = findPostByOrderDetailId(od);
                        if (-1 != postInAdapter) {
                            listOD.get(i).quantityCountInFragment -= 1;
                            waiterNotifications.remove(postInAdapter);
                            notifyItemRemoved(postInAdapter);
                        } else {
                            flagContinue = false;
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
    public WarningAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_dish_item, parent, false);
        return new WarningAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WarningAdapter.ViewHolder holder, int position) {
        WaiterNotification item = waiterNotifications.get(position);
        holder.tvDishNam.setText(item.getDish().getDishName());
        holder.tvTableNumber.setText("Table No." + String.valueOf(item.getTableNumber()));
        if (Constant.OVER_TIME == item.getType() && !item.isNotify()) {
            holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWarning));
            item.setNotified();
        } else if (item.isNotify()) {
            holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWarning));
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

    private WarningAdapter.WaiterListner listner;

    public void setListner(WarningAdapter.WaiterListner listner) {
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
