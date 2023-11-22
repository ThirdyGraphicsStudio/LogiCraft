package com.thirdy.booleanexpression.TruthTable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.thirdy.booleanexpression.DatabaseHelper.HistoryTaskTable;
import com.thirdy.booleanexpression.R;

import java.util.ArrayList;
import java.util.Arrays;

public class MainTruthTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truth_table_main);

        ImageButton back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            finish();
        });

        //generate table
        generateTruthTable(3);

        MaterialButton dropdownButton = findViewById(R.id.dropdownButton);

        // In your activity
        dropdownButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.variable_dropdown, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.two) {
                    // Handle "two" click
                    generateTruthTable(2);
                    dropdownButton.setText("2 variables");
                } else if (id == R.id.three) {
                    // Handle "three" click
                    dropdownButton.setText("3 variables");
                    generateTruthTable(3);

                } else if (id == R.id.four) {
                    // Handle "four" click
                    dropdownButton.setText("4 variables");
                    generateTruthTable(4);

                } else if (id == R.id.five) {
                    // Handle "five" click
                    dropdownButton.setText("5 variables");
                    generateTruthTable(5);
                } else if (id == R.id.six) {
                    // Handle "six" click
                    dropdownButton.setText("6 variables");
                    generateTruthTable(6);

                } else {
                    // If none of the above, return false.
                    return false;
                }

                // If one of the cases matched and the action was handled, return true.
                return true;
            });
            popupMenu.show();
        });

    }

    public void generateTruthTable(int variableCount) {
        TableLayout tableLayout = findViewById(R.id.tableLayout); // Make sure you have a TableLayout in your XML with this ID
        tableLayout.removeAllViews(); // Clear the existing table content

        // Create a header row
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        headerRow.setBackgroundColor(getResources().getColor(R.color.primary));

        // Define header texts dynamically based on the number of variables
        // Dynamic header based on the number of variables
        // Adjusted header texts with 'm' at the beginning
        String[] headerTexts = new String[variableCount + 2]; // +1 for 'm' and +1 for 'F'
        headerTexts[0] = "m";
        for (int i = 1; i <= variableCount; i++) {
            headerTexts[i] = String.valueOf((char) ('A' + i - 1));
        }
        headerTexts[variableCount + 1] = "F";

        // Add header texts to the header row
        for (String text : headerTexts) {
            TextView textView = new TextView(this);
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(16);
            textView.setPadding(10, 10, 10, 10);
            headerRow.addView(textView);
        }

        tableLayout.addView(headerRow);

        // Generate the data for the truth table based on the number of variables
        int rowCount = (int) Math.pow(2, variableCount); // Calculate the number of rows for the table
        int[][] rows = new int[rowCount][variableCount + 2]; // Initialize your rows array with the correct size

        for (int i = 0; i < rowCount; i++) {
            TableRow row = new TableRow(this);

            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setPaddingRelative(0, 2, 0, 2);



            final int rowIdx = i; // Capture the row index for use in the lambda expression

            for (int j = 0; j < variableCount + 2; j++) { // Iterate over each column
                TextView textView = new TextView(this);
                 if (j == 0) { // For 'm' column
                    rows[i][j] = i; // 'm' column holds the row number
                 } else if (j < variableCount) { // For variable columns
                    rows[i][j] = (i / (int) Math.pow(2, variableCount - j - 1)) % 2; // Calculate truth value
                }// 'F' column will be handled separately as it's interactive

                textView.setText(String.valueOf(rows[i][j]));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.black));

                // If this is the 'F' column, make it interactive
                if (j == variableCount + 1) {
                    textView.setBackgroundColor(getResources().getColor(R.color.inputfield));
                    textView.setTag("F" + i);
                    textView.setOnClickListener(v -> {
                        TextView tv = (TextView) v;
                        int newValue = tv.getText().toString().equals("0") ? 1 : 0;
                        tv.setText(String.valueOf(newValue));
                        // Update the corresponding value in the rows array
                        rows[rowIdx][variableCount+1] = newValue;
                    });
                }
                row.addView(textView);
            }

            tableLayout.addView(row);
        }



        //when the user click generate button
        MaterialButton btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(v -> {



            // Create an array to hold the state of the "F" column
            int[] fColumnValues = new int[rowCount];
            for (int i = 0; i < rowCount; i++) {
                fColumnValues[i] = rows[i][variableCount + 1]; // Assuming this is where the "F" column is
            }

            if(Arrays.stream(fColumnValues).allMatch(value -> value == 0) || Arrays.stream(fColumnValues).allMatch(value -> value == 1)){
                Toast.makeText(this, "Please input a valid truth table", Toast.LENGTH_SHORT).show();
                return;
            }

            HistoryTaskTable historyTaskTable = new HistoryTaskTable(this);
            historyTaskTable.insertData("Truth Table Input", Arrays.toString(fColumnValues), variableCount);

            // Create the intent to start the new activity
            Intent intent = new Intent(MainTruthTable.this, FormulaTruthTable.class);
            intent.putExtra("fColumnValues", fColumnValues); // Pass the "F" column values
            intent.putExtra("variableCount", variableCount); // Pass the number of variables
            // Start the new activity
            startActivity(intent);
        });


    }


}


