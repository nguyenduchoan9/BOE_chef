package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.DishSeri;
import com.example.bipain.boe_restaurantapp.model.TableModel;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_table_item, parent, false);
        return new TableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableModel model = getModelByPos(position);
        holder.tvTable.setText("Table " + String.valueOf(model.getTableNumber()));
        StringBuilder string = new StringBuilder();
        for (DishSeri d : model.getDishSeris()) {
            string.append(d.getName())
                    .append(", ");
        }
        String dishName = string.replace(string.length() - 2, string.length() - 1, "").toString();
        holder.tvDish.setText(dishName);
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

        public TableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
