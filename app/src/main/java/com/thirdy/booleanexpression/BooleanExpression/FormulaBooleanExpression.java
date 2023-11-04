package com.thirdy.booleanexpression.BooleanExpression;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thirdy.booleanexpression.R;

public class FormulaBooleanExpression extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boolean_expression_formula);

        //generate table
        generateTruthTable();
        generateKmapTable();
        generateKmapGroupTable();
        dropdownForGroup();
        dropdownForExport();

        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
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
            textView.setPadding(5, 5, 5, 5);
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
                textView.setPadding(5, 5, 5, 5);
                textView.setTextSize(10);
                textView.setTextColor(getResources().getColor(R.color.black));
                row.addView(textView);
            }

            tableLayout.addView(row);
        }
    }
    private void generateKmapTable() {
        TableLayout tableLayout = findViewById(R.id.kmapTableLayout);

        // Define the header titles
        String[] headers = {"  ", "B'C'", "B'C", "BC", "BC'"};

        // Create a row for the header
        TableRow headerRow = new TableRow(this);
        for (String header : headers) {
            TextView tv = new TextView(this);
            tv.setText(header);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextColor(getResources().getColor(R.color.primary));
            // Add TextView to the TableRow
            headerRow.addView(tv);
        }

        // Add the header row to the table layout without border
        tableLayout.addView(headerRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        // Data for the table cells
        String[][] data = {
                {"A'", "1", "0", "0", "0"},
                {"A", "1", "1", "1", "1"}
        };

        // Add data rows
        for (int i = 0; i < data.length; i++) {
            TableRow tr = new TableRow(this);
            for (int j = 0; j < data[i].length; j++) {
                TextView tv = new TextView(this);
                tv.setText(String.valueOf(data[i][j]));
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(10, 10, 10, 10);
                tv.setTextColor(getResources().getColor(R.color.primary));
                // Remove border effect by setting the background to a transparent drawable for the first column
                if (j == 0) {
                    tv.setBackgroundResource(android.R.color.transparent);
                } else {
                    // You can create a custom drawable with borders and use it here for other cells
                    tv.setBackgroundResource(R.drawable.cell_border);
                }

                // Add TextView to the TableRow
                tr.addView(tv);
            }
            // Add the TableRow to the TableLayout
            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }
    private void generateKmapGroupTable() {
        TableLayout tableLayout = findViewById(R.id.groupTableLayout);

        // Define the header titles
        String[] headers = {"  ", "B'C'", "B'C", "BC", "BC'"};

        // Create a row for the header
        TableRow headerRow = new TableRow(this);
        for (String header : headers) {
            TextView tv = new TextView(this);
            tv.setText(header);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextColor(getResources().getColor(R.color.primary));
            // Add TextView to the TableRow
            headerRow.addView(tv);
        }

        // Add the header row to the table layout without border
        tableLayout.addView(headerRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        // Data for the table cells
        String[][] data = {
                {"A'", "1", "0", "0", "0"},
                {"A", "1", "1", "1", "1"}
        };

        // Add data rows
        // Loop through each row
        for (int i = 0; i < data.length; i++) {
            TableRow tr = new TableRow(this);
            // Loop through each column
            for (int j = 0; j < data[i].length; j++) {
                TextView tv = new TextView(this);
                tv.setText(String.valueOf(data[i][j]));
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(10, 10, 10, 10);

                // Check the value and set background color accordingly
                if (data[i][j].equals("1")) {
                    // Set background to primary color
                    tv.setBackgroundColor(getResources().getColor(R.color.primary));
                    tv.setTextColor(getResources().getColor(R.color.white)); // Assuming you want white text on primary background
                } else if (data[i][j].equals("0")) {
                    // Set background to white
                    tv.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv.setTextColor(getResources().getColor(R.color.black)); // Assuming you want black text on white background
                }

                // Remove border effect by setting the background to a transparent drawable for the first column
                if (j == 0) {
                    tv.setBackgroundResource(android.R.color.transparent);
                    tv.setTextColor(getResources().getColor(R.color.primary)); // Set text color for the first column
                }

                // Add TextView to the TableRow
                tr.addView(tv);
            }
            // Add the TableRow to the TableLayout
            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }
    private void dropdownForGroup() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                R.layout.custom_spinner_item, getResources().getStringArray(R.array.dropdown_items)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view; // Now we cast directly to TextView
                // Set your custom font here using Typeface if needed
                // Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.your_custom_font);
                // textView.setTypeface(typeface);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.textView);
                // Set your custom font here using Typeface if needed
                // Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.your_custom_font);
                // textView.setTypeface(typeface);
                return view;
            }
        };


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected item
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void dropdownForExport() {
        Spinner spinner = findViewById(R.id.spinner_export);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                R.layout.custom_export_spinner_item, getResources().getStringArray(R.array.dropdown_items_export)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view; // Now we cast directly to TextView
                // Set your custom font here using Typeface if needed
                // Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.your_custom_font);
                // textView.setTypeface(typeface);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.textView);
                // Set your custom font here using Typeface if needed
                // Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.your_custom_font);
                // textView.setTypeface(typeface);
                return view;
            }
        };


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected item
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}