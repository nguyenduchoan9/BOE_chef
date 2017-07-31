package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.DishSeri;
import com.example.bipain.boe_restaurantapp.model.DishTableInfo;
import com.example.bipain.boe_restaurantapp.model.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 06/07/2017.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    List<TableModel> list;
    Context mContext;

    public TableAdapter(Context context) {
        list = new ArrayList<>();
        mContext = context;
    }

    public void setData(List<TableModel> data) {
        list = data;
        notifyDataSetChanged();
    }

    public void removeData(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    public List<TableModel> getList() {
        return list;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_table_item, parent, false);
        return new TableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableModel model = getModelByPos(position);
        holder.tvTable.setText(mContext.getString(R.string.text_table_title) + String.valueOf(model.getTableNumber()));
        StringBuilder string = new StringBuilder();
//        for (DishTableInfo d : model.getDishList()) {
//            string.append(d.getDish().getName())
//                    .append(", ");
//        }
//        String dishName = string.replace(string.length() - 2, string.length() - 1, "").toString();
        holder.tvDish.setText(getShowText(model.getDishList()));
    }

    private String getShowText(List<DishTableInfo> dishTable) {
        StringBuilder string = new StringBuilder();
        String abc = "";
        Map<DishTableInfo, Integer> container = new HashMap<>();
        for (int i = 0; i < dishTable.size(); i++) {
            DishTableInfo dishTableInfo = dishTable.get(i);
            DishTableInfo key = null;
            for (Map.Entry<DishTableInfo, Integer> item : container.entrySet()) {
                DishTableInfo keyValue = item.getKey();
                if (keyValue.getOrderDetailId() == dishTableInfo.getOrderDetailId()) {
                    key = keyValue;
                    break;
                }
            }
            if (null != key) {
                int currentQuantity = container.get(key);
                container.put(key, currentQuantity + dishTableInfo.getQuantiyNotServe());
            } else {
                container.put(dishTableInfo, dishTableInfo.getQuantiyNotServe());
            }
        }
        if (container.size() > 0) {
            for (Map.Entry<DishTableInfo, Integer> item : container.entrySet()) {
                DishTableInfo keyValue = item.getKey();
                string.append(keyValue.getDish().getName())
                        .append(" x ")
                        .append(String.valueOf(item.getValue()))
                        .append(" - ");
            }
            abc = string.replace(string.length() - 3, string.length() - 1, "").toString();
        } else {
            return "";
        }
        return abc;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private TableModel getModelByPos(int pos) {
        return list.get(pos);
    }

    class TableViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTableNumber)
        TextView tvTable;

        @BindView(R.id.tvDish)
        TextView tvDish;

        @BindView(R.id.tvServe)
        TextView tvServe;

        public TableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvServe.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                TableModel model = getModelByPos(pos);
                StringBuilder params = new StringBuilder("");
                for (DishTableInfo table : model.getDishList()) {
                    params.append(String.valueOf(table.getOrderDetailId()))
                            .append("_");
                }
                String formatParams = params.deleteCharAt(params.length() - 1).toString();
                if (null != listener) {
                    listener.onServeClick(getAdapterPosition(), formatParams, model);
                }
            });
        }
    }

    public interface onTableServeClickListener {
        void onServeClick(int pos, String orderDetailIdList, TableModel tableModel);
    }

    private onTableServeClickListener listener;

    public void setListener(onTableServeClickListener listener) {
        this.listener = listener;
    }
}
