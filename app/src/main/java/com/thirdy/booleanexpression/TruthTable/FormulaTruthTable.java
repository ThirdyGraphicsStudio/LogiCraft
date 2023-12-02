package com.thirdy.booleanexpression.TruthTable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.thirdy.booleanexpression.Algorithm.Solver;
import com.thirdy.booleanexpression.Algorithm.Term;
import com.thirdy.booleanexpression.KarnaughMap.FormulaKarnaughMap;
import com.thirdy.booleanexpression.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FormulaTruthTable extends AppCompatActivity {

    private int variableCount;
    private int[] fColumnValues;
    File myPath;
    int totalHeight, totalWidth;

    private String[] minters;
    Bitmap bitmap;

    private TextView txtResult;
    private ProgressBar progressBar;
    private ImageView imgPdf;
    private TextView txtSolution, txtAnswer, txtInput;
    private NestedScrollView nestedScrollView;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    String path,imageUri,file_name = "Download";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truth_table_formula);
        progressBar = findViewById(R.id.ProgressBar);

        // Assume variableCount is also passed as an Intent extra
        variableCount = getIntent().getIntExtra("variableCount", 0);
        fColumnValues = getIntent().getIntArrayExtra("fColumnValues");
        txtResult = findViewById(R.id.txtResult);
        txtSolution = findViewById(R.id.txtSolution);
        txtAnswer = findViewById(R.id.txtAnswer);
        imgPdf = findViewById(R.id.imgPdf);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        linearLayout = findViewById(R.id.ll_output);
        txtInput = findViewById(R.id.txtInput);


        imgPdf.setOnClickListener(view -> {
            savedPdf();
        });

        //ilagay sa minters yung f values
        // Assuming fColumnValues is already filled with values
        // Initialize minters with the same length as fColumnValues
        minters = new String[fColumnValues.length];
        // Convert and transfer values
        for (int i = 0; i < fColumnValues.length; i++) {
            minters[i] = String.valueOf(fColumnValues[i]);
        }


        ArrayList<Integer> indexes = new ArrayList<>();

        // Iterate through fColumnValues
        for (int i = 0; i < fColumnValues.length; i++) {
            if (fColumnValues[i] == 1) {
                indexes.add(i); // Add index to the list if value is 1
            }
        }

        // Convert the list of indexes to a string representation with commas
        StringBuilder builder = new StringBuilder();
        for (int index : indexes) {
            builder.append(index).append(", ");
        }

        // Remove the last comma
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }

        String minterms = builder.toString(); // Contains "1,3,4,5"

        txtInput.setText("Input = ∑ ( " + minterms + " )");

        Log.d("minters", "Minters" +  Arrays.toString(minters));
        Log.d("minters",  "column values" + Arrays.toString(fColumnValues));

        //generate table
        if (fColumnValues != null) {
            generateTruthTablePreview(variableCount, fColumnValues);
        }

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

    private void savedPdf() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FormulaTruthTable.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CODE);
        }

        takeScreenshot();

    }



    private  void takeScreenshot(){
        progressBar.setVisibility(View.VISIBLE);
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/");
        if(!folder.exists()){
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + "logicraft" + System.currentTimeMillis() + ".pdf";

        totalHeight = nestedScrollView.getChildAt(0).getHeight();
        totalWidth = nestedScrollView.getChildAt(0).getWidth();
        String extr = Environment.getExternalStorageDirectory() + "/logicraft/";
        File file  = new File(extr);

        //    boolean mkdir = file.mkdir();
        if(!file.exists()){

        }
        file.mkdir();
        String fileName = file_name + ".pdf";
        myPath = new File(extr, fileName);
        imageUri = myPath.getPath();
        bitmap = getBitmapFromView(linearLayout, totalHeight, totalWidth);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.v("PDF", "Message1: " + e.getMessage());
            Log.v("PDF", "Track1: " + e.getStackTrace());

        }
        downloadPdf();

    }
    //6-22
    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }


    private void downloadPdf() {
        //6-22
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPaint(paint);
        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap,this.bitmap.getWidth(),this.bitmap.getHeight(),true);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);
        File filePath = new File(path);
        try{
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Saved Pdf Successfully! ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.v("PDF","Message: "+ e.getMessage());
            Log.v("PDF","Track: "+ e.getStackTrace());
            progressBar.setVisibility(View.GONE);
        }
        document.close();
        if (myPath.exists())
            myPath.delete();
//        openPdf(path);
    }

    private void generateTruthTablePreview(int variableCount, int[] fColumnValues) {
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
        // Change rows to an Object array to accommodate both Integers and Strings
        Object[][] rows = new Object[rowCount][variableCount + 2];

        for (int i = 0; i < rowCount; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setPaddingRelative(0, 2, 0, 2);

            // Create the 'm' column TextView first
            TextView mTextView = new TextView(this);
            mTextView.setText(String.valueOf(i)); // The value for 'm' is the row number
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setPadding(10, 10, 10, 10);
            mTextView.setTextSize(14);
            mTextView.setTextColor(getResources().getColor(R.color.black));
            row.addView(mTextView);

            // Now create TextViews for each variable column
            for (int j = 0; j < variableCount; j++) {
                TextView textView = new TextView(this);
                // Calculate the value for this variable and set the text
                int value = (i / (int) Math.pow(2, variableCount - j - 1)) % 2;
                textView.setText(String.valueOf(value));

                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.black));
                row.addView(textView);
            }

            // Create the 'F' column TextView last
            TextView fTextView = new TextView(this);
            fTextView.setText(String.valueOf(fColumnValues[i])); // Set the text from the provided F column values
            fTextView.setGravity(Gravity.CENTER);
            fTextView.setPadding(10, 10, 10, 10);
            fTextView.setTextSize(14);
            fTextView.setTextColor(getResources().getColor(R.color.black));
            row.addView(fTextView);

            tableLayout.addView(row);
        }

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
                String fColumnValuesString = Arrays.stream(fColumnValues)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", "));

                if(selectedItem.equals("SOP")) {
                    generateKmapTable(variableCount, "1");

                    String minterms = "";
                    for (int i = 0; i < fColumnValues.length; i++) {
                        if (fColumnValues[i] == 1) {
                            minterms += i + " ";
                        }
                    }

                    // Optional: Remove the trailing space
                    if (!minterms.isEmpty()) {
                        minterms = minterms.substring(0, minterms.length() - 1);
                    }

                    // Now you can use the minterms string

                    txtResult.setText("");
                    answer(minterms, "");



                }else {
                    txtResult.setText("");
                    generateKmapTable(variableCount, "0");

                    String minterms = "";
                    for (int i = 0; i < fColumnValues.length; i++) {
                        if (fColumnValues[i] == 0) {
                            minterms += i + " ";
                        }
                    }

                    // Optional: Remove the trailing space
                    if (!minterms.isEmpty()) {
                        minterms = minterms.substring(0, minterms.length() - 1);
                    }


                    txtResult.setText("");
                    answer(minterms, "");

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




    }





    public void answer(String minterms, String dontCares) {

            Solver s = new Solver(minterms, dontCares);

            s.solve();
            s.printResults();

        ArrayList<String[][]> step2 = s.step2;
        StringBuilder builder = new StringBuilder();

// Iterate over each 2D array
        for (int arrayIndex = 0; arrayIndex < step2.size(); arrayIndex++) {
            String[][] array = step2.get(arrayIndex);
            boolean hasNumericHeader = false;
            StringBuilder headerBuilder = new StringBuilder();

            // Check and build the numeric headers (first row) of each 2D array
            for (int i = 0; i < array[0].length; i++) {
                try {
                    // Attempt to parse the header as an integer
                    Integer.parseInt(array[0][i]);
                    headerBuilder.append(array[0][i]).append(" ");
                    hasNumericHeader = true;
                } catch (NumberFormatException e) {
                    // Not a numeric header, ignore
                }
            }

            // Only append the group if there are numeric headers
            if (hasNumericHeader) {
                builder.append("Group ").append(arrayIndex + 1).append(":\n");
                builder.append(headerBuilder.toString()).append("\n\n");
            }
        }

        Log.d("StepLog", builder.toString());

        txtSolution.setText(builder.toString());
            txtAnswer.setText(s.printResults());

        // Split the input text by '\n' to separate lines
        String[] lines = s.printResults().split("\n");

        // Get the last line (assuming it contains the expression)
        String lastLine = lines[lines.length - 1];

        // Remove leading and trailing spaces
        String expression = lastLine.trim();


        //remove f
        // Find the opening and closing parentheses
        int startIndex = expression.indexOf('(');
        int endIndex = expression.indexOf(')');

        // Check if both opening and closing parentheses are found
        if (startIndex != -1 && endIndex != -1) {
            // Extract the expression between the parentheses
            expression = expression.substring(startIndex + 1, endIndex);

            // Remove leading and trailing spaces
            expression = expression.trim();


            Log.d("StepLog", expression);
            Log.d("StepLog", convertToBoolean(expression));



//            <TextView
//            android:id="@+id/txtSimplified"
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:text="Simplified Expression: "
//            android:layout_marginTop="10dp"
//            android:textSize="12sp"
//            android:textColor="@color/black"
//            android:fontFamily="@font/poppinsregular"
//                    />

           TextView txtSimplified = findViewById(R.id.txtSimplified);
           txtSimplified.setVisibility(View.VISIBLE);
           txtSimplified.setText("INTERPRET: \n " + convertToBoolean(expression));
            simplified(convertToBoolean(expression));

           //simple expression


            String encodedQuery = null;
            try {
                encodedQuery = URLEncoder.encode(convertToBoolean(expression), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            //TODO Remove Comment
            finalSimplified(encodedQuery);

        }else{
            Log.d("StepLog", "No parentheses found in the input text.");
        }

        }

    private void simplified(String encodedQuery) {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("https://www.emathhelp.net/en/calculators/discrete-mathematics/boolean-algebra-calculator/?f=" + encodedQuery);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // JavaScript code to remove unwanted elements
                view.evaluateJavascript("javascript:(function() { " +
                        "document.body.innerHTML = document.getElementById('solution').outerHTML; " +
                        "})()", null);
            }
        });

    }


    private void fetchWolframAlphaResult(String query) {
        progressBar.setVisibility(View.VISIBLE);
        String appID = "LUQHKE-P74YUH66QT";
        String url = "https://api.wolframalpha.com/v2/query?appid=" + appID + "&input=logic+circuit+" + query + "&format=image";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle error
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FormulaTruthTable.this, "Can Not Make Logic Diagram", Toast.LENGTH_SHORT).show();

                } else {
                    String responseData = response.body().string();
                    // Parse the XML to find the image URL
                    String imageUrl = extractImageUrl(responseData);
                    // Update the ImageView on the UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout containerLogicDiagram = findViewById(R.id.containerLogicDiagram);
                            MaterialButton btnSave = findViewById(R.id.btnSave);
                            try{
                                Log.d("StepLog", imageUrl);

                                progressBar.setVisibility(View.GONE);
                                ImageView imageView = findViewById(R.id.myImageView);
                                Picasso.get().load(imageUrl).into(imageView);
                                containerLogicDiagram.setVisibility(View.VISIBLE);

                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                        File file = savedDiagram(bitmap);
                                        saveToGallery(file);
                                    }
                                });

                            }catch (Exception e) {
                                containerLogicDiagram.setVisibility(View.GONE);
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FormulaTruthTable.this, "Can Not Make Logic Diagram", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            private String extractImageUrl(String xmlResponse) {
                try {
                    // Create a new DocumentBuilderFactory
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();

                    // Parse the XML
                    InputStream is = new ByteArrayInputStream(xmlResponse.getBytes());
                    Document doc = builder.parse(is);
                    NodeList imgTags = doc.getElementsByTagName("img");

                    // Loop through <img> tags to find the desired image
                    for (int i = 0; i < imgTags.getLength(); i++) {
                        Node node = imgTags.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            // Check if this is the image you want (based on alt/title/other attributes)
                            if (element.getAttribute("alt").contains("Logic circuit")) {
                                return element.getAttribute("src").replace("&amp;", "&");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null; // Or a default image URL
            }


        });
    }

    private File  savedDiagram(Bitmap bitmap) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FormulaTruthTable.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CODE);
        }
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/Download/");
        dir.mkdirs();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;

    }

    private void saveToGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Toast.makeText(this, "Saved Logic Diagram Successfully! ", Toast.LENGTH_SHORT).show();
    }


    private String convertToBoolean(String expression) {
        // Split the input expression by '+' to get individual terms
        String[] terms = expression.split("\\s*\\+\\s*");

        StringBuilder outputExpression = new StringBuilder();

        for (int i = 0; i < terms.length; i++) {
            String term = terms[i];
            StringBuilder convertedTerm = new StringBuilder();

            boolean previousWasNegatedVariable = false;

            for (int j = 0; j < term.length(); j++) {
                char character = term.charAt(j);

                if (character == '\'') {
                    // If it's a prime (negation), prepend '~' to the last character in convertedTerm
                    convertedTerm.insert(convertedTerm.length() - 1, '~');
                    previousWasNegatedVariable = true; // Marking that the last variable was negated
                } else {
                    // Check if the current and previous characters are letters or if previous was a negated variable
                    if (j > 0 && (Character.isLetter(character) || previousWasNegatedVariable)) {
                        // If it's a letter or previous was a negated variable, insert "OR"
                        convertedTerm.append(" OR ");
                    }
                    convertedTerm.append(character);
                    previousWasNegatedVariable = false; // Resetting the flag
                }
            }
            // Add the converted term to the output expression
            if (i > 0) {
                outputExpression.append(" AND ");
            }
            outputExpression.append('(').append(convertedTerm).append(')');
        }
        return outputExpression.toString();
    }



    private void finalSimplified(String query) {
        progressBar.setVisibility(View.VISIBLE);
        String appID = "VLG5Q7-2XL8AYYWQW";
        String url = "https://api.wolframalpha.com/v2/query?appid=" + appID + "&input=simplifify%20" + query;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle error
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FormulaTruthTable.this, "Can Not Make Logic Diagram", Toast.LENGTH_SHORT).show();

                } else {
                    String responseData = response.body().string();
                    // Parse the XML to find the image URL
                    final String text = extractPlainText(responseData);

                    String encodedQuery = null;
                    try {
                        encodedQuery = URLEncoder.encode(text, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    //TODO Remove Comment
                    fetchWolframAlphaResult(encodedQuery);


                    // Update the ImageView on the UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout container_simplify = findViewById(R.id.container_simplify);
                            TextView txtSimplifiedExpression = findViewById(R.id.txtSimplifiedExpression);
                            try{
                                Log.d("StepLog", text);
                                progressBar.setVisibility(View.GONE);
                                container_simplify.setVisibility(View.VISIBLE);
                                txtSimplifiedExpression.setText("F = " + text );


                            }catch (Exception e) {
                                container_simplify.setVisibility(View.GONE);
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FormulaTruthTable.this, "Already Simplified", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            private String extractPlainText(String xmlResponse) {
                try {
                    // Create a new DocumentBuilderFactory
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();

                    // Parse the XML
                    InputStream is = new ByteArrayInputStream(xmlResponse.getBytes());
                    Document doc = builder.parse(is);

                    // Find the 'Result' pod
                    NodeList podNodes = doc.getElementsByTagName("pod");
                    for (int i = 0; i < podNodes.getLength(); i++) {
                        Node podNode = podNodes.item(i);
                        if (podNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element podElement = (Element) podNode;
                            if ("Result".equals(podElement.getAttribute("title"))) {
                                NodeList plaintextTags = podElement.getElementsByTagName("plaintext");
                                if (plaintextTags.getLength() > 0) {
                                    Node plaintextNode = plaintextTags.item(0);
                                    if (plaintextNode != null) {
                                        return plaintextNode.getTextContent();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ""; // Return empty if not found or in case of exception
            }



        });
    }

    public static String transformExpression(String input) {
        // Define a regular expression pattern for (Variable1Variable2...) AND (Variable1Variable2...)
        String pattern = "\\((\\w+)\\) AND \\((\\w+)\\)";

        // Create a pattern matcher
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        // Initialize the result
        StringBuilder result = new StringBuilder();

        int previousEnd = 0;

        // Find and replace matching patterns
        while (matcher.find()) {
            // Append the part of the input before the match
            result.append(input.substring(previousEnd, matcher.start()));

            // Extract variables
            String variables1 = matcher.group(1);
            String variables2 = matcher.group(2);

            // Split the variables into individual characters
            String[] vars1 = variables1.split("(?!^)");
            String[] vars2 = variables2.split("(?!^)");

            // Build the replacement string
            StringBuilder replacement = new StringBuilder();
            for (int i = 0; i < vars1.length; i++) {
                if (i > 0) {
                    replacement.append(" OR ");
                }
                replacement.append(vars1[i]).append(" OR ~").append(vars2[i]);
            }

            // Append the replacement string
            result.append("(").append(replacement).append(")");

            // Update the previous end index
            previousEnd = matcher.end();
        }

        // Append the remaining part of the input
        result.append(input.substring(previousEnd));

        return result.toString();
    }



}

