package com.thesis.booleanexpression.BooleanExpression;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.thesis.booleanexpression.R;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FormulaBooleanExpression extends AppCompatActivity {
    private NestedScrollView nestedScrollView;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    String path,imageUri,file_name = "Download";
    private LinearLayout linearLayout;
    File myPath;
    int totalHeight, totalWidth;
    Bitmap bitmap;
    private ImageView imgPdf;
    private ProgressBar progressBar;

    private int[] fColumnValues;

    private String[] minters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boolean_expression_formula);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        linearLayout = findViewById(R.id.ll_output);
        imgPdf = findViewById(R.id.imgPdf);
        progressBar = findViewById(R.id.ProgressBar);

        imgPdf.setOnClickListener(view -> {
            savedPdf();
        });

        //generate table
        generateTruthTable();


        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });


//        generate LOGIC DIAGRAM
        Intent intent = getIntent();
        String input = intent.getStringExtra("input");

        simplified(input);

        //generateLogicDiagram(input);

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


    private void generateLogicDiagram(String expression) {
        String encodedQuery = null;
        try {
            encodedQuery = URLEncoder.encode(convertToBoolean(expression), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //TODO Remove Comment
        finalSimplified(encodedQuery);
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
                    Toast.makeText(FormulaBooleanExpression.this, "Can Not Make Logic Diagram", Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(FormulaBooleanExpression.this, "Can Not Make Logic Diagram", Toast.LENGTH_SHORT).show();
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
            ActivityCompat.requestPermissions(FormulaBooleanExpression.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CODE);
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
                    Toast.makeText(FormulaBooleanExpression.this, "Can Not Make Logic Diagram", Toast.LENGTH_SHORT).show();

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
                            //LinearLayout container_simplify = findViewById(R.id.container_simplify);
                            //TextView txtSimplifiedExpression = findViewById(R.id.txtSimplifiedExpression);
                            try{
                                Log.d("StepLog", text);
                                progressBar.setVisibility(View.GONE);
                               // container_simplify.setVisibility(View.VISIBLE);
                               // txtSimplifiedExpression.setText("F = " + text );


                            }catch (Exception e) {
                               // container_simplify.setVisibility(View.GONE);
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FormulaBooleanExpression.this, "Already Simplified", Toast.LENGTH_SHORT).show();
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


    private void savedPdf() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FormulaBooleanExpression.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CODE);
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



    private void generateTruthTable() {
        Intent intent = getIntent();
        String input = intent.getStringExtra("input");

        // Get data from print method
        BooleanToTruth table = new BooleanToTruth(input, false);
        List<List<String>> tableData = table.printTable(); // Get data from print method

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (int rowIndex = 0; rowIndex < tableData.size(); rowIndex++) {
            List<String> rowData = tableData.get(rowIndex);
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            for (String cellData : rowData) {
                TextView textView = new TextView(this);
                textView.setText(cellData);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(5, 5, 5, 5);
                textView.setTextSize(10);
                if (rowIndex == 0) {
                    // Set header row style
                    textView.setTextColor(getResources().getColor(R.color.white));
                    row.setBackgroundColor(getResources().getColor(R.color.primary));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                row.addView(textView);
            }

            tableLayout.addView(row);
        }


        ArrayList<String> mintermIndexes = new ArrayList<>();
        int childCount = tableLayout.getChildCount();

        // Iterate through each TableRow
        for (int i = 1; i < childCount; i++) { // Start from 1 to skip the header row
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            int lastCellIndex = row.getChildCount() - 1;
            TextView lastTextView = (TextView) row.getChildAt(lastCellIndex);

            mintermIndexes.add(lastTextView.getText().toString()); // Subtract 1 to adjust for header row
            Log.d("TAG", i + String.valueOf(mintermIndexes));
        }



        minters = new String[mintermIndexes.size()];
        // Convert and transfer values
        for (int i = 0; i < mintermIndexes.size(); i++) {
            minters[i] = String.valueOf(mintermIndexes.get(i));
            Log.d("TAG", String.valueOf(minters[i]));
        }


        int variableCount = 0;
        int columnCount = mintermIndexes.size();
        if (columnCount == 4) {
            variableCount = 2;
        } else if (columnCount == 8) {
            variableCount = 3;
        } else if (columnCount == 16) {
            variableCount = 4;
        } else if (columnCount == 32) {
            variableCount = 5;
        } else if (columnCount == 64) {
            variableCount = 6;
        }



        generateKmapTable( variableCount, "1");

        Log.d("TAG", String.valueOf(columnCount));
        Log.d("TAG", String.valueOf(variableCount));
        Log.d("TAG", String.valueOf(mintermIndexes));
    }




    private void generateKmapTable(int variableCount, String group) {
        if (variableCount == 2) {
            String[] headers = {"  ", "", ""};
            String[][] data = {{"  ", minters[0], minters[1]}, {"  ", minters[2], minters[3]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        } else if (variableCount == 3) {
            String[] headers = {"  ", "", "", "", ""};
            String[][] data = {{"  ", minters[0], minters[1], minters[3], minters[2]}, {"  ", minters[4], minters[5], minters[7], minters[6]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        }else if (variableCount == 4) {
            String[] headers = {"  ", "", "", "", ""};
            String[][] data = {{"  ", minters[0], minters[1], minters[3], minters[2]}, {"  ", minters[4], minters[5], minters[7], minters[6]}, {"  ", minters[12], minters[13], minters[15], minters[14]}, {"  ", minters[8], minters[9], minters[11], minters[10]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        } else if (variableCount == 5) {
            String[] headers = {"  ", "", "", " ", ""};
            String[][]data = {{"  ", minters[0], minters[1], minters[3], minters[2]}, {"  ", minters[4], minters[5], minters[7], minters[6]}, {"  ", minters[12], minters[13], minters[15], minters[14]}, {"  '", minters[8], minters[9], minters[11], minters[10]}, {"  ", minters[16], minters[17], minters[19], minters[18]}, {"  ", minters[20], minters[21], minters[23], minters[22]}, {"  ", minters[28], minters[29], minters[31], minters[30]}, {"  ", minters[24], minters[25], minters[27], minters[26]}};
            createTable(headers, data, R.id.kmapTableLayout);
            createGroupTable(headers, data, R.id.groupTableLayout, group);
        } else if (variableCount == 6) {
            String[] headers = {"  ", "", " ", "", " "};
            String[][] data = {{"  ", minters[0], minters[1], minters[3], minters[2]}, {"  ", minters[4], minters[5], minters[7], minters[6]}, {"  ", minters[12], minters[13], minters[15], minters[14]}, {"  ", minters[8], minters[9], minters[11], minters[10]}, {" ", minters[16], minters[17], minters[19], minters[18]}, {"  ", minters[20], minters[21], minters[23], minters[22]}, {"  ", minters[28], minters[29], minters[31], minters[30]}, {"  ", minters[24], minters[25], minters[27], minters[26]}, {"  ", minters[32], minters[33], minters[35], minters[34]}, {"  ", minters[36], minters[37], minters[39], minters[38]}, {"  ", minters[44], minters[45], minters[47], minters[46]}, {"  ", minters[40], minters[41], minters[43], minters[42]}, {"  ", minters[48], minters[49], minters[51], minters[50]}, {"  ", minters[52], minters[53], minters[55], minters[54]}, {"  ", minters[60], minters[61], minters[63], minters[62]}, {"  ", minters[56], minters[57], minters[59], minters[58]}};
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






}