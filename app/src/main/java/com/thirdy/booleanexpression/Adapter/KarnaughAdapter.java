package com.thirdy.booleanexpression.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.thirdy.booleanexpression.Model.KarnaughModel;
import com.thirdy.booleanexpression.R;

import java.util.List;

public class KarnaughAdapter extends RecyclerView.Adapter<KarnaughAdapter.StylistViewHolder> {
    public final MyInterface myInterfaces;

    private List<KarnaughModel> karnaughModels;
    private Context context;

    public KarnaughAdapter(List<KarnaughModel> karnaughModels, Context context, MyInterface myInterfaces) {
        this.karnaughModels = karnaughModels;
        this.context = context;
        this.myInterfaces = myInterfaces;
    }

    @NonNull
    @Override
    public StylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_karnaugh, parent, false);
        return new StylistViewHolder(view, myInterfaces);
    }

    @Override
    public void onBindViewHolder(@NonNull StylistViewHolder holder, int position) {

    holder.txtColumn1.setText(karnaughModels.get(position).getColumn1());
    holder.txtColumn2.setText(karnaughModels.get(position).getColumn2());

    holder.txtdisplay0.setText(karnaughModels.get(position).getTxtdisplay0());
    holder.txtdisplay1.setText(karnaughModels.get(position).getTxtdisplay1());
    holder.txtdisplay2.setText(karnaughModels.get(position).getTxtdisplay2());
    holder.txtdisplay3.setText(karnaughModels.get(position).getTxtdisplay3());
    holder.txtdisplay4.setText(karnaughModels.get(position).getTxtdisplay4());
    holder.txtdisplay5.setText(karnaughModels.get(position).getTxtdisplay5());
    holder.txtdisplay6.setText(karnaughModels.get(position).getTxtdisplay6());
    holder.txtdisplay7.setText(karnaughModels.get(position).getTxtdisplay7());


        holder.btn0.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 0);
        });
        holder.btn1.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 1);
        });
        holder.btn2.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 2);
        });
        holder.btn3.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 3);
        });
        holder.btn4.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 4);
        });
        holder.btn5.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 5);
        });
        holder.btn6.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 6);
        });
        holder.btn7.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            listener((Button)view, currentPos, 7);
        });

    }

    private void listener(Button button, int position, int arr) {
        String buttonText = button.getText().toString();
        KarnaughModel model = karnaughModels.get(position);
        List<String> list = model.getList();

        switch (buttonText) {
            case "0":
                list.set(arr, "1");
                button.setText("1");
                break;
            case "1":
                list.set(arr, "x");
                button.setText("x");
                break;
            case "x":
                list.set(arr, "0");
                button.setText("0");
                break;
            default:
                // Handle unexpected button text if needed
                break;
        }

        // Update the model with the modified list
        model.setList(list);
        // Notify the adapter if needed
        // adapter.notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return karnaughModels.size();
    }

    public class StylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtColumn1, txtColumn2;
        MaterialButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7;

        TextView txtdisplay0, txtdisplay1, txtdisplay2, txtdisplay3, txtdisplay4, txtdisplay5, txtdisplay6, txtdisplay7;


        public StylistViewHolder(@NonNull View itemView, MyInterface myInterfaces) {
            super(itemView);

            txtColumn1 = itemView.findViewById(R.id.txtColumn1);
            txtColumn2 = itemView.findViewById(R.id.txtColumn2);

            btn0 = itemView.findViewById(R.id.btn0);
            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
            btn3 = itemView.findViewById(R.id.btn3);
            btn4 = itemView.findViewById(R.id.btn4);
            btn5 = itemView.findViewById(R.id.btn5);
            btn6 = itemView.findViewById(R.id.btn6);
            btn7 = itemView.findViewById(R.id.btn7);
            txtdisplay0 = itemView.findViewById(R.id.txtdisplay0);
            txtdisplay1 = itemView.findViewById(R.id.txtdisplay1);
            txtdisplay2 = itemView.findViewById(R.id.txtdisplay2);
            txtdisplay3 = itemView.findViewById(R.id.txtdisplay3);
            txtdisplay4 = itemView.findViewById(R.id.txtdisplay4);
            txtdisplay5 = itemView.findViewById(R.id.txtdisplay5);
            txtdisplay6 = itemView.findViewById(R.id.txtdisplay6);
            txtdisplay7 = itemView.findViewById(R.id.txtdisplay7);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(myInterfaces != null ){
                int pos = getAdapterPosition();
                if(pos!= RecyclerView.NO_POSITION){
                    myInterfaces.onItemClick(pos, "schedule");
                }

            }
        }
    }
}
