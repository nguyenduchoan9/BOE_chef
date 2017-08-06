package com.example.bipain.boe_restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
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
        notifyDataSetChanged();
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

        DishInOrder dish = data.get(position);
        // refresh viewholder
        viewlHolder.chkDone.setOnCheckedChangeListener(null);
        viewlHolder.chkDone.setVisibility(View.GONE);
        viewlHolder.handleOverMaterial.setVisibility(View.GONE);
        viewlHolder.rlBg.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.white));

        if (!dish.isOverMaterial()) {
            viewlHolder.chkDone.setVisibility(View.VISIBLE);
            viewlHolder.chkDone.setChecked(dish.isChecked());
            viewlHolder.chkDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                dish.setChecked(isChecked);
                if (isChecked) {
                    Log.d("Hoang", "getView: true");
                    listener.onDoneClick(dish.getDish().getDishId(), dish.getIdentity());
                } else {
                    Log.d("Hoang", "getView: false");
                    listener.onNotDoneClick(dish.getDish().getDishId(), dish.getIdentity());
                }
            });
            convertView.setOnLongClickListener(v -> {
                if (null != listener) {
                    listener.onLongClick(dish.getDish().getDishId());
                }
                return true;
            });
        } else {
            viewlHolder.rlBg.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarningBlue));
            viewlHolder.handleOverMaterial.setVisibility(View.VISIBLE);
            viewlHolder.tvKeep.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onKeepDishClick(dish.getDish().getDishId());
                }
            });
            viewlHolder.tvCancel.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onCancelClick(dish.getDish().getDishId());
                }
            });
        }

        viewlHolder.txtDishName.setText(dish.getDish().getName());
        viewlHolder.txtQuantity.setText("x" + String.valueOf(dish.getQuantity()));
        viewlHolder.txtDishId.setText(String.valueOf(dish.getDish().getDishId()));

        return convertView;
    }

    class ViewHolder {
        public TextView txtDishName;
        public TextView txtQuantity;
        public TextView txtDishId, tvKeep, tvCancel;
        public CheckBox chkDone;
        public LinearLayout handleOverMaterial;
        public RelativeLayout rlBg;

        public ViewHolder(View v) {
            initView(v);
        }

        private void initView(View view) {
            txtDishName = (TextView) view.findViewById(R.id.txtDishName);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            txtDishId = (TextView) view.findViewById(R.id.txtDishId);
            chkDone = (CheckBox) view.findViewById(R.id.chkCookedDish);
            handleOverMaterial = (LinearLayout) view.findViewById(R.id.handleOverMaterial);
            tvKeep = (TextView) view.findViewById(R.id.tvKeep);
            tvCancel = (TextView) view.findViewById(R.id.tvCancel);
            rlBg = (RelativeLayout) view.findViewById(R.id.rlBg);
        }


    }

    public interface DishQueueAdapterListener {
        void onDoneClick(int dishId, Date identify);

        void onNotDoneClick(int dishId, Date identify);

        void onLongClick(int dishId);

        void onKeepDishClick(int dishId);

        void onCancelClick(int dishId);
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
                    String lowerTerm = Normalizer.normalize(term.toString().toLowerCase(), Normalizer.Form.NFD);
                    String lowerName = Normalizer.normalize(dish.getDish().getName().toLowerCase(), Normalizer.Form.NFD);

                    if (lowerName.contains(lowerTerm)) {
                        filterDish.add(dish);
                    }
                }
                filterResults.values = filterDish;
                filterResults.count = filterDish.size();
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
