package com.thirdy.booleanexpression.TruthTable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.thirdy.booleanexpression.R;

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
        generateTruthTable();


        MaterialButton btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(v -> {
            //startActivity(new Intent(MainTruthTable.this, TruthTable.class));
        });

        MaterialButton dropdownButton = findViewById(R.id.dropdownButton);

        // In your activity
        dropdownButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.variable_dropdown, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.two) {
                    // Handle "two" click
                    dropdownButton.setText("2 variables");
                } else if (id == R.id.three) {
                    // Handle "three" click
                    dropdownButton.setText("3 variables");
                } else if (id == R.id.four) {
                    // Handle "four" click
                    dropdownButton.setText("4 variables");
                } else if (id == R.id.five) {
                    // Handle "five" click
                    dropdownButton.setText("5 variables");
                } else if (id == R.id.six) {
                    // Handle "six" click
                    dropdownButton.setText("6 variables");
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

    private void generateTruthTable() {
        TableLayout tableLayout = findViewById(R.id.tableLayout); // Make sure you have a TableLayout in your XML with this ID

        // Create a header row
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        headerRow.setBackgroundColor(getResources().getColor(R.color.primary));
        String[] headerTexts = {"A", "B", "C", "m", "F"};
        for (String headerText : headerTexts) {
            TextView textView = new TextView(this);
            textView.setText(headerText);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            textView.setPadding(10, 10, 10, 10);
            headerRow.addView(textView);
        }

        tableLayout.addView(headerRow);

        // Add data as rows
        int[][] data = {
                {0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {1, 1, 1, 7, 1}
        };

        for (int[] rowData : data) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setPaddingRelative(0, 2, 0, 2);

            for (int cellData : rowData) {
                TextView textView = new TextView(this);
                textView.setText(String.valueOf(cellData));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.black));
                row.addView(textView);
            }

            tableLayout.addView(row);
        }
    }

}