package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.Material;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 27/06/2017.
 */

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {
    List<Material> materials;
    LayoutInflater inflater;
    Context mContext;

    public MaterialAdapter(List<Material> materials, Context context) {
        this.materials = materials;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Material> materials) {
        this.materials = materials;
        notifyDataSetChanged();
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.layout_material_item, parent, false);
        return new MaterialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MaterialViewHolder holder, int position) {
        Material material = getByPosition(position);
        holder.name.setText(material.getName());
        holder.isChecked.setChecked(material.isAvailable());
        holder.isChecked.setOnClickListener(v -> {
            mListener.onCheckClick(holder.isChecked.isChecked(), material.getId());
            material.setAvailable(holder.isChecked.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    private Material getByPosition(int pos) {
        return materials.get(pos);
    }

    class MaterialViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView name;
        @BindView(R.id.cbAvailable)
        CheckBox isChecked;

        public MaterialViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface MaterialAapterListener {
        void onCheckClick(boolean status, int id);
    }

    private MaterialAapterListener mListener;

    public void setListener(MaterialAapterListener mListener) {
        this.mListener = mListener;
    }
}
