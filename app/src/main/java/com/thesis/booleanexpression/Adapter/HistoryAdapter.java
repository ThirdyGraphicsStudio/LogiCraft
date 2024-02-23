package com.thesis.booleanexpression.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thesis.booleanexpression.Model.HistoryModel;
import com.thesis.booleanexpression.R;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final MyInterface myInterfaces;
    private final Context context;
    private final ArrayList<HistoryModel> historyModels;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModels, MyInterface myInterfaces){
        this.context = context;
        this.historyModels = historyModels;
        this.myInterfaces = myInterfaces;
    }

    @Override
    public int getItemViewType(int position) {
        return historyModels.get(position).isHeader() ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.recycler_history, parent, false);
            return new MyViewHolder(view, myInterfaces);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).txtDate.setText(historyModels.get(position).getDate());
        } else {
            ((MyViewHolder) holder).txtHistoryName.setText(historyModels.get(position).getName());

        }
    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtHistoryName;
        public MyViewHolder(@NonNull View itemView, MyInterface myInterfaces) {
            super(itemView);
            txtHistoryName = itemView.findViewById(R.id.txtHistoryName);

            if (myInterfaces == null) {
                Log.d("HistoryAdapter", "MyInterface is null");
            }

            itemView.setOnClickListener(view -> {
                if(myInterfaces != null ){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        myInterfaces.onItemClick(pos, "history");
                    }
                }
            });
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
