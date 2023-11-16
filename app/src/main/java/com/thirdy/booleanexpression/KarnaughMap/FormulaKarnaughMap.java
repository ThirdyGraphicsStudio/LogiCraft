package com.thirdy.booleanexpression.KarnaughMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thirdy.booleanexpression.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FormulaKarnaughMap extends AppCompatActivity {
    private int variableCount;
    private String[] fColumnValues;
    private String[] minters;
    private ProgressBar progressBar;
    private TextView txtResult;
    private  ImageView imgPdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnaugh_map_formula);

        imgPdf = findViewById(R.id.imgPdf);


        imgPdf.setOnClickListener(view -> {
            Toast.makeText(this, "SAVED AS PDF", Toast.LENGTH_SHORT).show();
        });

        progressBar = findViewById(R.id.ProgressBar);
        txtResult  = findViewById(R.id.txtResult);
        //generate table

        dropdownForGroup();
        dropdownForExport();



        // Assume variableCount is also passed as an Intent extra
        variableCount = getIntent().getIntExtra("variableCount", 0);
        fColumnValues = getIntent().getStringArrayExtra("fColumnValues");


        minters = new String[fColumnValues.length];
        Log.d("minters", fColumnValues.toString());

        // Convert and transfer values
        for (int i = 0; i < fColumnValues.length; i++) {
            minters[i] = fColumnValues[i];
        }

        generateKmapTable(variableCount, "1");


        //generate table
        if (fColumnValues != null) {
            generateTruthTablePreview(variableCount, fColumnValues);
        }

        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void generateKmapTable(int variableCount, String group) {
        if (variableCount == 2) {
            String[] headers = {"  ", "B'", "B"};
            String[][] data = {{"A'", minters[0], minters[1]}, {"A", minters[2], minters[3]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        } else if (variableCount == 3) {
            String[] headers = {"  ", "B'C'", "B'C", "BC", "BC'"};
            String[][] data = {{"A'", minters[0], minters[1], minters[3], minters[2]}, {"A", minters[4], minters[5], minters[7], minters[6]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        }else if (variableCount == 4) {
            String[] headers = {"  ", "C'D'", "C'D", "CD", "CD'"};
            String[][] data = {{"A'B'", minters[0], minters[1], minters[3], minters[2]}, {"A'B", minters[4], minters[5], minters[7], minters[6]}, {"AB", minters[12], minters[13], minters[15], minters[14]}, {"AB'", minters[8], minters[9], minters[11], minters[10]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        } else if (variableCount == 5) {
            String[] headers = {"  ", "D'E'", "D'E", "DE", "DE'"};
            String[][]data = {{"A'B'C'", minters[0], minters[1], minters[3], minters[2]}, {"A'B'C", minters[4], minters[5], minters[7], minters[6]}, {"A'BC", minters[12], minters[13], minters[15], minters[14]}, {"A'BC'", minters[8], minters[9], minters[11], minters[10]}, {"AB'C'", minters[16], minters[17], minters[19], minters[18]}, {"AB'C", minters[20], minters[21], minters[23], minters[22]}, {"ABC", minters[28], minters[29], minters[31], minters[30]}, {"ABC'", minters[24], minters[25], minters[27], minters[26]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        } else if (variableCount == 6) {
            String[] headers = {"  ", "E'F'", "E'F", "EF", "EF'"};
            String[][] data = {{"A'B'C'D'", minters[0], minters[1], minters[3], minters[2]}, {"A'B'C'D", minters[4], minters[5], minters[7], minters[6]}, {"A'B'CD", minters[12], minters[13], minters[15], minters[14]}, {"A'B'CD'", minters[8], minters[9], minters[11], minters[10]}, {"A'BC'D'", minters[16], minters[17], minters[19], minters[18]}, {"A'BC'D", minters[20], minters[21], minters[23], minters[22]}, {"A'BCD", minters[28], minters[29], minters[31], minters[30]}, {"A'BCD'", minters[24], minters[25], minters[27], minters[26]}, {"AB'C'D'", minters[32], minters[33], minters[35], minters[34]}, {"AB'C'D", minters[36], minters[37], minters[39], minters[38]}, {"AB'CD", minters[44], minters[45], minters[47], minters[46]}, {"AB'CD'", minters[40], minters[41], minters[43], minters[42]}, {"ABC'D'", minters[48], minters[49], minters[51], minters[50]}, {"ABC'D", minters[52], minters[53], minters[55], minters[54]}, {"ABCD", minters[60], minters[61], minters[63], minters[62]}, {"ABCD'", minters[56], minters[57], minters[59], minters[58]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        }
    }

    private void createTable(String[] headers, String[][] data, int tableLayoutId) {
        TableLayout tableLayout = findViewById(tableLayoutId);
        tableLayout.removeAllViews();

        // Define the header titles

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
    private void createGroupTable(String[] headers, String[][] data, int tableLayoutId, String group) {
        TableLayout tableLayout = findViewById(tableLayoutId);


        tableLayout.removeAllViews();
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
                // Check the value and set background color accordingly
                if (data[i][j].equals(group)) {
                    // Set background to primary color
                    tv.setBackgroundColor(getResources().getColor(R.color.primary));
                    tv.setTextColor(getResources().getColor(R.color.white)); // Assuming you want white text on primary background
                } else  {
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

    private void generateTruthTablePreview(int variableCount, String[] fColumnValues) {
        TableLayout tableLayout = findViewById(R.id.tableLayout); // Make sure you have a TableLayout in your XML with this ID
        tableLayout.removeAllViews(); // Clear the existing table content

        // Create a header row
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        headerRow.setBackgroundColor(getResources().getColor(R.color.primary));

        // Define header texts dynamically based on the number of variables
        // Dynamic header based on the number of variables
        String[] headerTexts = new String[variableCount + 2]; // +2 for 'm' and 'F' columns
        for (int i = 0; i < variableCount; i++) {
            headerTexts[i] = String.valueOf((char) ('A' + i)); // Create header labels dynamically
        }
        headerTexts[variableCount] = "m";
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
        // Change rows to an Object array to accommodate both Integers and Strings
        Object[][] rows = new Object[rowCount][variableCount + 2];

        for (int i = 0; i < rowCount; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setPaddingRelative(0, 2, 0, 2);

            for (int j = 0; j < variableCount + 2; j++) {
                TextView textView = new TextView(this);

                if (j < variableCount) {
                    rows[i][j] = (i / (int) Math.pow(2, variableCount - j - 1)) % 2;
                } else if (j == variableCount) {
                    rows[i][j] = i;
                } else if (j == variableCount + 1) {
                    // Here, you assign a String value to the last column
                    rows[i][j] = fColumnValues[i]; // Replace with the string value you want to display
                }

                // Set the text of the TextView to the string representation of the value
                textView.setText(String.valueOf(rows[i][j]));

                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.black));

                row.addView(textView);
            }

            tableLayout.addView(row);
        }

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
    private void generateKmapGroupTable(String group) {
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
                String fColumnValuesString = String.join(", ", fColumnValues);

                if(selectedItem.equals("SOP")) {
                    txtResult.setText("");
                    generateKmapTable(variableCount, "1");
                    sendRequest("I need to solve a " + variableCount + "-variable K-Map with the following minterms:" + fColumnValuesString + " . Can you group the minterms, simplify the expression, and give me the final SOP expression?");
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    txtResult.setText("");
                    generateKmapTable(variableCount, "0");
                    progressBar.setVisibility(View.VISIBLE);
                    sendRequest("I need to solve a " + variableCount + "-variable K-Map with the following minterms:" + fColumnValuesString + " . Can you group the minterms, simplify the expression, and give me the final POS expression?");


                }
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


    private void sendRequest(String content) {
        String apiKey = "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"; // Replace with your actual API key

        new Thread(() -> {
            try {
                URL url = new URL("https://api.openai.com/v1/chat/completions");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("model", "gpt-3.5-turbo");

                JSONArray messages = new JSONArray();

                JSONObject systemMessage = new JSONObject();
                systemMessage.put("role", "system");
                systemMessage.put("content", "You are an assistant skilled in digital logic design. When given minterms of a 4-variable Karnaugh Map, your task is to identify all possible groups of 1s, simplify the expression using the Sum of Products (SOP) or POS base on user instruction method, and provide the final expression. You should return the results in a structured JSON format, including the positions of the groups, the simplified expressions for each group, and the final SOP expression.  designed to output JSON. \\n, you produce many group depend on variable   \\n example only:  {'Group++': {'Position': [answer], 'Simplified Expression': 'answer'}, 'Group++': {'Position': [answer], 'Simplified Expression': 'answer'}, the dami ng group is depend sa answer 'FINAL EXPRESSION': 'F = answer} please check the answer on http://www.32x8.com/");
                messages.put(systemMessage);

                JSONObject userMessage = new JSONObject();
                userMessage.put("role", "user");
                userMessage.put("content", content); // The content passed to this method
                messages.put(userMessage);

                payload.put("messages", messages);

                try (java.io.OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.toString().getBytes("UTF-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (Scanner scanner = new Scanner(conn.getInputStream())) {
                        String jsonResponse = scanner.useDelimiter("\\A").next();
                        JSONObject obj = new JSONObject(jsonResponse);
                        JSONArray choices = obj.getJSONArray("choices");
                        JSONObject firstChoice = choices.getJSONObject(0);
                        String messageContent = firstChoice.getString("message"); // Assuming the response structure
                        JSONObject messageJson = new JSONObject(messageContent);
                        String contents = messageJson.getString("content");
                        Log.d("messageContent", messageContent);

                        runOnUiThread(() -> {
                            // Handle the response content
                            // For example, you can start a new activity with the response
                            txtResult.setText(contents);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    runOnUiThread(() -> Toast.makeText(FormulaKarnaughMap.this, "Failed with response code: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                runOnUiThread(() -> Toast.makeText(FormulaKarnaughMap.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    // Example method to parse and format the JSON response
    private String formatResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            StringBuilder formattedResult = new StringBuilder();

            // Iterate through the groups
            for (int i = 1; jsonObject.has("Group" + i); i++) {
                JSONObject group = jsonObject.getJSONObject("Group" + i);
                formattedResult.append("Group ").append(i).append(":\n");
                formattedResult.append("Position: ").append(group.getJSONArray("Position").toString()).append("\n");
                formattedResult.append("Simplified Expression: ").append(group.getString("Simplified Expression")).append("\n\n");
            }

            // Append the final expression
            if (jsonObject.has("FINAL EXPRESSION")) {
                formattedResult.append(jsonObject.getString("FINAL EXPRESSION"));
            }

            return formattedResult.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error parsing the response.";
        }
    }



}