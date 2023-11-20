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
import com.thirdy.booleanexpression.MainActivity;
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


        for(int i=0; i < historyModels.size(); i++){
            Log.d("History", "historyData: " + historyModels.get(i).getMinterms());
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
        if (!categories.equals("history")) return;
        if (pos <= 0 || pos > historyModels.size()) {
            Log.e("History", "Invalid position: " + pos);
            return;
        }
        HistoryModel selectedHistory = historyModels.get(pos-1);
        String expression = selectedHistory.getName();
        int variableCount = selectedHistory.getVariable();
        Intent intent;

        switch (expression) {
            case "Truth Table Input":
                intent = new Intent(getContext(), FormulaTruthTable.class);
                intent.putExtra("fColumnValues", processMintermsInt(selectedHistory.getMinterms(), true));
                break;
            case "Boolean Expression Input":
                intent = new Intent(getContext(), FormulaBooleanExpression.class);
                // Additional logic if needed
                intent.putExtra("input", selectedHistory.getMinterms());

                break;
            case "Karnaugh Map Input":
                intent = new Intent(getContext(), FormulaKarnaughMap.class);
                intent.putExtra("fColumnValues", processMinterms(selectedHistory.getMinterms(), false));
                break;
            default:
                intent = new Intent(getContext(), MainActivity.class);
                break;
        }

        intent.putExtra("variableCount", variableCount);
        startActivity(intent);
    }

    private String[] processMinterms(String minterms, boolean asIntegers) {
        String[] numbers = minterms.replace("[", "").replace("]", "").trim().split(",");
        String[] fColumnValues = new String[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            if (asIntegers) {
                try {
                    fColumnValues[i] = String.valueOf(Integer.parseInt(numbers[i].trim()));
                } catch (NumberFormatException e) {
                    Log.e("History", "Error parsing integer: " + numbers[i], e);
                }
            } else {
                fColumnValues[i] = numbers[i].trim();
            }
        }

        return fColumnValues;
    }

    private int[] processMintermsInt(String minterms, boolean asIntegers) {
        String[] numbers = minterms.replace("[", "").replace("]", "").trim().split(",");
        int[] fColumnValues = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            if (asIntegers) {
                try {
                    fColumnValues[i] = Integer.parseInt(numbers[i].trim());
                } catch (NumberFormatException e) {
                    Log.e("History", "Error parsing integer: " + numbers[i], e);
                }
            } else {
                fColumnValues[i] = Integer.parseInt(numbers[i].trim());
            }
        }

        return fColumnValues;
    }

}