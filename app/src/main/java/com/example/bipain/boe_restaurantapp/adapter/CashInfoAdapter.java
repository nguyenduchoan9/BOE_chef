package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.CashModel;
import com.example.bipain.boe_restaurantapp.utils.Util;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 03/08/2017.
 */

public class CashInfoAdapter extends RecyclerView.Adapter<CashInfoAdapter.CashViewHolder> {

    List<CashModel> list;
    LayoutInflater layoutInflater;

    public CashInfoAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        list = new ArrayList<>();
    }

    public void setData(List<CashModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void removeByPos(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addData(CashModel cashModel) {
        list.add(cashModel);
        notifyItemInserted(list.size() - 1);
    }

    private CashModel getItemByPos(int pos) {
        return list.get(pos);
    }

    @Override
    public CashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.layout_item_cash, parent, false);
        return new CashViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CashViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CashViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvTable)
        TextView tvTable;
        @BindView(R.id.tvHandle)
        TextView tvHandle;

        public CashViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            CashModel cashModel = getItemByPos(getAdapterPosition());
            tvMoney.setText(Util.formatVNDecimal(cashModel.getTotal()));
            tvTable.setText("Table " + cashModel.getTableNumber());
            tvHandle.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onHanldeClick(cashModel.getOrderId(), getAdapterPosition());
                }
            });
        }
    }

    public interface CashItemListener {
        void onHanldeClick(int orderId, int pos);
    }

    private CashItemListener listener;

    public void setListener(CashItemListener listener) {
        this.listener = listener;
    }
}
