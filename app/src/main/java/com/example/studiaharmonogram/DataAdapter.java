package com.example.studiaharmonogram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    List<DataModel> data_list;

    public DataAdapter(Context context, List<DataModel> data_list) {
        this.context = context;
        this.data_list = new ArrayList<>();  // Initialize empty list
        if (data_list != null) {
            this.data_list.addAll(data_list); // Add initial data if provided
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (data_list != null && data_list.size() > 0) {
            DataModel model = data_list.get(position);

            holder.przedmiot_tv.setText(model.getPrzedmiot());
            holder.sala_tv.setText(model.getSala());
            holder.godziny_tv.setText(model.getGodziny());
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public void updateData(List<DataModel> newDataList) {
        if (newDataList == null) return;

        this.data_list.clear();
        this.data_list.addAll(newDataList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView przedmiot_tv, sala_tv, godziny_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            przedmiot_tv = itemView.findViewById(R.id.przedmiot_text);
            sala_tv = itemView.findViewById(R.id.sala_text);
            godziny_tv = itemView.findViewById(R.id.godziny_text);
        }
    }
}
