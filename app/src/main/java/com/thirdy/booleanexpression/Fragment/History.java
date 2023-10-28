package com.thirdy.booleanexpression.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.thirdy.booleanexpression.Adapter.HistoryAdapter;
import com.thirdy.booleanexpression.Adapter.MyInterface;
import com.thirdy.booleanexpression.Model.HistoryModel;
import com.thirdy.booleanexpression.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class History extends Fragment implements MyInterface {
    private View view;

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryModel> historyModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);

        //initiate UI
        initUI();

        //init adapter
        historyAdapter = new HistoryAdapter(getContext(), historyModels, this);

        //set up HistoryData
        historyData();

        //set Up Recycler View
        //setUpRecyclerView();

        return  view;

    }

    private void initUI() {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void historyData() {


        historyModels.add(new HistoryModel(1, "Fri Oct 27 01:50:05 GMT 2023", "Truth Table Input"));
        historyModels.add(new HistoryModel(1, "Fri Oct 27 01:50:05 GMT 2023", "Boolean Expression Input"));
        historyModels.add(new HistoryModel(1, "Fri Oct 28 01:50:05 GMT 2023", "Karnaugh Map Input"));
        historyModels.add(new HistoryModel(1,"Fri Oct 28 01:50:05 GMT 2023", "Truth Table Input"));

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                historyAdapter.notifyDataSetChanged();
//            }
//        });


        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd MMM", Locale.US);
        ArrayList<HistoryModel> processedList = new ArrayList<>();
        String lastHeader = "";

        for (HistoryModel model : historyModels) {
            try {
                Date date = originalFormat.parse(model.getDate());
                String formattedDate = desiredFormat.format(date);
                if (!formattedDate.equals(lastHeader)) {
                    HistoryModel headerModel = new HistoryModel(-1, formattedDate, "");
                    headerModel.setHeader(true);
                    processedList.add(headerModel);
                }
                processedList.add(model);
                lastHeader = formattedDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        HistoryAdapter adapter = new HistoryAdapter(getContext(), processedList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }



    @Override
    public void onItemClick(int pos, String categories) {

    }

}