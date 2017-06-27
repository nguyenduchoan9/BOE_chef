package com.example.bipain.boe_restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiPain on 6/9/2017.
 */

public class DishQueueAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private ArrayList<DishInOrder> data;
    private ArrayList<DishInOrder> originalData;
    private static LayoutInflater layoutInflater = null;

    public DishQueueAdapter(Activity activity, ArrayList<DishInOrder> data) {
        this.activity = activity;
        this.data = data;
        originalData = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<DishInOrder> data) {
        this.data = data;
        originalData = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewlHolder = null;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.dish_queue_row, parent, false);
            viewlHolder = new ViewHolder(convertView);
            convertView.setTag(viewlHolder);
        } else {
            viewlHolder = (ViewHolder) convertView.getTag();
        }

        DishInOrder dish = new DishInOrder();
        dish = data.get(position);

        viewlHolder.txtDishName.setText(dish.getDish().getName());
        viewlHolder.txtQuantity.setText(String.valueOf(dish.getQuantity()));
        viewlHolder.txtDishId.setText(String.valueOf(dish.getDish().getDishId()));
        viewlHolder.chkDone.setChecked(false);
        DishInOrder finalDish = dish;
       // viewlHolder.chkDone.setOnClickListener(v ->
       //         listener.onDoneClick(finalDish.getDish().getDishId()));

        return convertView;
    }

    class ViewHolder {
        public TextView txtDishName;
        public TextView txtQuantity;
        public TextView txtDishId;
        public CheckBox chkDone;

        public ViewHolder(View v) {
            initView(v);
        }

        private void initView(View view) {
            txtDishName = (TextView) view.findViewById(R.id.txtDishName);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            txtDishId = (TextView) view.findViewById(R.id.txtDishId);
            chkDone = (CheckBox) view.findViewById(R.id.chkCookedDish);
        }
    }

    public interface DishQueueAdapterListener {
        void onDoneClick(int dishId);
    }

    private DishQueueAdapterListener listener;

    public void setListener(DishQueueAdapterListener listener) {
        this.listener = listener;
    }

    private DishFilterable filter;

    @Override
    public Filter getFilter() {
        if (null == filter) {
            filter = new DishFilterable();
        }
        return filter;
    }

    private class DishFilterable extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence term) {
            FilterResults filterResults = new FilterResults();
            if (null == term || term.length() == 0) {
                filterResults.values = originalData;
                filterResults.count = originalData.size();
            } else {
                List<DishInOrder> filterDish = new ArrayList<>();
                for (DishInOrder dish : originalData) {
                    String lowerName = Normalizer.normalize(dish.getDish().getName(), Normalizer.Form.NFC).toLowerCase();
                    String lowerTerm = Normalizer.normalize(term, Normalizer.Form.NFC).toLowerCase();
                    if (lowerName.contains(lowerTerm)) {
                        filterDish.add(dish);
                    }
                    filterResults.values = filterDish;
                    filterResults.count = filterDish.size();
                }
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<DishInOrder>) results.values;
            notifyDataSetChanged();
        }
    }
}
