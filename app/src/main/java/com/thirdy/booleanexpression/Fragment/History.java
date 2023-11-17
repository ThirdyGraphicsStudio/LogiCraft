package com.thirdy.booleanexpression.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.thirdy.booleanexpression.Adapter.HistoryAdapter;
import com.thirdy.booleanexpression.Adapter.MyInterface;
import com.thirdy.booleanexpression.BooleanExpression.FormulaBooleanExpression;
import com.thirdy.booleanexpression.DatabaseHelper.HistoryTaskTable;
import com.thirdy.booleanexpression.KarnaughMap.FormulaKarnaughMap;
import com.thirdy.booleanexpression.Model.HistoryModel;
import com.thirdy.booleanexpression.R;
import com.thirdy.booleanexpression.TruthTable.FormulaTruthTable;

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

        HistoryTaskTable historyTaskTable = new HistoryTaskTable(getContext());
        Cursor cursor = historyTaskTable.getData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Assuming you have columns like 'id', 'expression', 'minterms' in your history table
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String expression = cursor.getString(cursor.getColumnIndexOrThrow("expression"));
                    String minterms = cursor.getString(cursor.getColumnIndexOrThrow("minterms"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    int variables = cursor.getInt(cursor.getColumnIndexOrThrow("variables"));


                    historyModels.add(new HistoryModel(id, date, expression,minterms ,variables));

                    // Log the row data
                    Log.d("Data", "Row: ID = " + id + ", Expression = " + expression + ", Minterms = " + minterms + ", Variable = " + variables + ", Date = " + date);
                }
            } finally {
                cursor.close(); // Always close the cursor when you're done with it
            }
        } else {
            Log.d("Data", "No data found");
        }

//        historyModels.add(new HistoryModel(1, "Fri Oct 27 01:50:05 GMT 2023", "Truth Table Input"));
//        historyModels.add(new HistoryModel(1, "Fri Oct 27 01:50:05 GMT 2023", "Boolean Expression Input"));
//        historyModels.add(new HistoryModel(1, "Fri Oct 28 01:50:05 GMT 2023", "Karnaugh Map Input"));
//        historyModels.add(new HistoryModel(1,"Fri Oct 28 01:50:05 GMT 2023", "Truth Table Input"));

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                historyAdapter.notifyDataSetChanged();
//            }
//        });



        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd MMM", Locale.US);
        ArrayList<HistoryModel> processedList = new ArrayList<>();
        String lastHeader = "";

        for (HistoryModel model : historyModels) {
            try {
                Date date = originalFormat.parse(model.getDate());
                Log.d("Date", "date " + model.getDate());
                String formattedDate = desiredFormat.format(date);
                if (!formattedDate.equals(lastHeader)) {
                    HistoryModel headerModel = new HistoryModel(-1, formattedDate, "", "", 2);
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
        String expression = historyModels.get(pos).getName();
        int variableCount = historyModels.get(pos).getVariable();
        Toast.makeText(getContext(), "expression", Toast.LENGTH_SHORT).show();
        Intent intent = null;

        if(expression.equals("Truth Table Input")){
            intent = new Intent(getContext(), FormulaTruthTable.class);
        }else if(expression.equals("Boolean Expression Input")){
            intent = new Intent(getContext(), FormulaBooleanExpression.class);

        } else if (expression.equals("Karnaugh Map Input")) {
            intent = new Intent(getContext(), FormulaKarnaughMap.class);

        }else{
            intent = new Intent(getContext(), FormulaKarnaughMap.class);

        }

       //the  minimers
        String x = historyModels.get(pos).getMinterms();

        // Remove brackets and trim spaces
        x = x.replace("[", "").replace("]", "").trim();

        // Split the string by commas
        String[] numbers = x.split(",");

        // Initialize the array of integers
        int[] fColumnValues = new int[numbers.length];

        // Parse and store each number
        for (int i = 0; i < numbers.length; i++) {
            fColumnValues[i] = Integer.parseInt(numbers[i].trim());
        }

        intent.putExtra("fColumnValues", fColumnValues); // Pass the "F" column values
        intent.putExtra("variableCount", variableCount); // Pass the number of variables
        // Start the new activity
        startActivity(intent);

    }

}