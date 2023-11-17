package com.thirdy.booleanexpression.KarnaughMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.thirdy.booleanexpression.Adapter.KarnaughAdapter;
import com.thirdy.booleanexpression.Adapter.MyInterface;
import com.thirdy.booleanexpression.Model.KarnaughModel;
import com.thirdy.booleanexpression.R;
import com.thirdy.booleanexpression.TruthTable.FormulaTruthTable;
import com.thirdy.booleanexpression.TruthTable.MainTruthTable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainKarnaughMap extends AppCompatActivity implements MyInterface {

    private MaterialButton dropdownButton;

    private RecyclerView recyclerView;
    private LinearLayout morethan2, equal2;
    private TextView txtFirst, txtSecond,txtThird, txtFourth; ;

    List<KarnaughModel> karnaughModels = new ArrayList<>();
    KarnaughAdapter karnaughAdapter;
    List<String> minimers = new ArrayList<>();
    List<String> for2variables = new ArrayList<>();

    int variables = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnaugh_map_main);
        recyclerView = findViewById(R.id.recyclerView);
        morethan2 = findViewById(R.id.morethan2);
        equal2 = findViewById(R.id.equal2);
        txtFirst = findViewById(R.id.txtFirst);
        txtSecond = findViewById(R.id.txtSecond);
        txtThird = findViewById(R.id.txtThird);
        txtFourth = findViewById(R.id.txtFourth);


        karnaughAdapter = new KarnaughAdapter(karnaughModels, this,this);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

        dropdown();

        MaterialButton btnGenerate = findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(v -> {
           generateFinal();
            Log.d("TAG", "all"  + karnaughModels.size());
        });

        setData();
        txtFirst.setText("B'C'");
        txtSecond.setText("B'C");
        txtThird.setText("BC");
        txtFourth.setText("BC'");
        recyclerView.setAdapter(karnaughAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void generateFinal() {

        String[] fColumnValues ;

        if(variables == 2){
            fColumnValues = new String[4];
            fColumnValues[0] = for2variables.get(0);
            fColumnValues[1] = for2variables.get(1);
            fColumnValues[2] = for2variables.get(2);
            fColumnValues[3] = for2variables.get(3);
        } else if (variables == 3) {
            fColumnValues = new String[8];
            fColumnValues[0] = karnaughModels.get(0).getList().get(0);
            fColumnValues[1] = karnaughModels.get(0).getList().get(1);
            fColumnValues[2] = karnaughModels.get(0).getList().get(3);
            fColumnValues[3] = karnaughModels.get(0).getList().get(2);
            fColumnValues[4] = karnaughModels.get(0).getList().get(4);
            fColumnValues[5] = karnaughModels.get(0).getList().get(5);
            fColumnValues[6] = karnaughModels.get(0).getList().get(6);
            fColumnValues[7] = karnaughModels.get(0).getList().get(7);
        }else if (variables == 4) {
            fColumnValues = new String[16];
            fColumnValues[0] = karnaughModels.get(0).getList().get(0);
            fColumnValues[1] = karnaughModels.get(0).getList().get(1);
            fColumnValues[2] = karnaughModels.get(0).getList().get(3);
            fColumnValues[3] = karnaughModels.get(0).getList().get(2);
            fColumnValues[4] = karnaughModels.get(0).getList().get(4);
            fColumnValues[5] = karnaughModels.get(0).getList().get(5);
            fColumnValues[6] = karnaughModels.get(0).getList().get(6);
            fColumnValues[7] = karnaughModels.get(0).getList().get(7);
            fColumnValues[8] = karnaughModels.get(1).getList().get(4);
            fColumnValues[9] = karnaughModels.get(1).getList().get(5);
            fColumnValues[10] = karnaughModels.get(1).getList().get(7);
            fColumnValues[11] = karnaughModels.get(1).getList().get(6);
            fColumnValues[12] = karnaughModels.get(1).getList().get(0);
            fColumnValues[13] = karnaughModels.get(1).getList().get(1);
            fColumnValues[14] = karnaughModels.get(1).getList().get(3);
            fColumnValues[15] = karnaughModels.get(1).getList().get(2);
        }else if (variables == 5) {
            fColumnValues = new String[32];
            fColumnValues[0] = karnaughModels.get(0).getList().get(0);
            fColumnValues[1] = karnaughModels.get(0).getList().get(1);
            fColumnValues[2] = karnaughModels.get(0).getList().get(3);
            fColumnValues[3] = karnaughModels.get(0).getList().get(2);
            fColumnValues[4] = karnaughModels.get(0).getList().get(4);
            fColumnValues[5] = karnaughModels.get(0).getList().get(5);
            fColumnValues[6] = karnaughModels.get(0).getList().get(6);
            fColumnValues[7] = karnaughModels.get(0).getList().get(7);
            fColumnValues[8] = karnaughModels.get(1).getList().get(4);
            fColumnValues[9] = karnaughModels.get(1).getList().get(5);
            fColumnValues[10] = karnaughModels.get(1).getList().get(7);
            fColumnValues[11] = karnaughModels.get(1).getList().get(6);
            fColumnValues[12] = karnaughModels.get(1).getList().get(0);
            fColumnValues[13] = karnaughModels.get(1).getList().get(1);
            fColumnValues[14] = karnaughModels.get(1).getList().get(3);
            fColumnValues[15] = karnaughModels.get(1).getList().get(2);
            fColumnValues[16] = karnaughModels.get(2).getList().get(0);
            fColumnValues[17] = karnaughModels.get(2).getList().get(1);
            fColumnValues[18] = karnaughModels.get(2).getList().get(3);
            fColumnValues[19] = karnaughModels.get(2).getList().get(2);
            fColumnValues[20] = karnaughModels.get(2).getList().get(4);
            fColumnValues[21] = karnaughModels.get(2).getList().get(5);
            fColumnValues[22] = karnaughModels.get(2).getList().get(7);
            fColumnValues[23] = karnaughModels.get(2).getList().get(6);
            fColumnValues[24] = karnaughModels.get(3).getList().get(4);
            fColumnValues[25] = karnaughModels.get(3).getList().get(5);
            fColumnValues[26] = karnaughModels.get(3).getList().get(7);
            fColumnValues[27] = karnaughModels.get(3).getList().get(6);
            fColumnValues[28] = karnaughModels.get(3).getList().get(0);
            fColumnValues[29] = karnaughModels.get(3).getList().get(1);
            fColumnValues[30] = karnaughModels.get(3).getList().get(3);
            fColumnValues[31] = karnaughModels.get(3).getList().get(2);
        }else if (variables == 6) {
            fColumnValues = new String[64];
            fColumnValues[0] = karnaughModels.get(0).getList().get(0);
            fColumnValues[1] = karnaughModels.get(0).getList().get(1);
            fColumnValues[2] = karnaughModels.get(0).getList().get(3);
            fColumnValues[3] = karnaughModels.get(0).getList().get(2);
            fColumnValues[4] = karnaughModels.get(0).getList().get(4);
            fColumnValues[5] = karnaughModels.get(0).getList().get(5);
            fColumnValues[6] = karnaughModels.get(0).getList().get(6);
            fColumnValues[7] = karnaughModels.get(0).getList().get(7);
            fColumnValues[8] = karnaughModels.get(1).getList().get(4);
            fColumnValues[9] = karnaughModels.get(1).getList().get(5);
            fColumnValues[10] = karnaughModels.get(1).getList().get(7);
            fColumnValues[11] = karnaughModels.get(1).getList().get(6);
            fColumnValues[12] = karnaughModels.get(1).getList().get(0);
            fColumnValues[13] = karnaughModels.get(1).getList().get(1);
            fColumnValues[14] = karnaughModels.get(1).getList().get(3);
            fColumnValues[15] = karnaughModels.get(1).getList().get(2);
            fColumnValues[16] = karnaughModels.get(2).getList().get(0);
            fColumnValues[17] = karnaughModels.get(2).getList().get(1);
            fColumnValues[18] = karnaughModels.get(2).getList().get(3);
            fColumnValues[19] = karnaughModels.get(2).getList().get(2);
            fColumnValues[20] = karnaughModels.get(2).getList().get(4);
            fColumnValues[21] = karnaughModels.get(2).getList().get(5);
            fColumnValues[22] = karnaughModels.get(2).getList().get(7);
            fColumnValues[23] = karnaughModels.get(2).getList().get(6);
            fColumnValues[24] = karnaughModels.get(3).getList().get(4);
            fColumnValues[25] = karnaughModels.get(3).getList().get(5);
            fColumnValues[26] = karnaughModels.get(3).getList().get(7);
            fColumnValues[27] = karnaughModels.get(3).getList().get(6);
            fColumnValues[28] = karnaughModels.get(3).getList().get(0);
            fColumnValues[29] = karnaughModels.get(3).getList().get(1);
            fColumnValues[30] = karnaughModels.get(3).getList().get(3);
            fColumnValues[31] = karnaughModels.get(3).getList().get(2);
            fColumnValues[32] = karnaughModels.get(4).getList().get(0);
            fColumnValues[33] = karnaughModels.get(4).getList().get(1);
            fColumnValues[34] = karnaughModels.get(4).getList().get(3);
            fColumnValues[35] = karnaughModels.get(4).getList().get(2);
            fColumnValues[36] = karnaughModels.get(4).getList().get(4);
            fColumnValues[37] = karnaughModels.get(4).getList().get(5);
            fColumnValues[38] = karnaughModels.get(4).getList().get(7);
            fColumnValues[39] = karnaughModels.get(4).getList().get(6);
            fColumnValues[40] = karnaughModels.get(5).getList().get(4);
            fColumnValues[41] = karnaughModels.get(5).getList().get(5);
            fColumnValues[42] = karnaughModels.get(5).getList().get(7);
            fColumnValues[43] = karnaughModels.get(5).getList().get(6);
            fColumnValues[44] = karnaughModels.get(5).getList().get(0);
            fColumnValues[45] = karnaughModels.get(5).getList().get(1);
            fColumnValues[46] = karnaughModels.get(5).getList().get(3);
            fColumnValues[47] = karnaughModels.get(5).getList().get(2);
            fColumnValues[48] = karnaughModels.get(6).getList().get(0);
            fColumnValues[49] = karnaughModels.get(6).getList().get(1);
            fColumnValues[50] = karnaughModels.get(6).getList().get(3);
            fColumnValues[51] = karnaughModels.get(6).getList().get(2);
            fColumnValues[52] = karnaughModels.get(6).getList().get(4);
            fColumnValues[53] = karnaughModels.get(6).getList().get(5);
            fColumnValues[54] = karnaughModels.get(6).getList().get(7);
            fColumnValues[55] = karnaughModels.get(6).getList().get(6);
            fColumnValues[56] = karnaughModels.get(7).getList().get(4);
            fColumnValues[57] = karnaughModels.get(7).getList().get(5);
            fColumnValues[58] = karnaughModels.get(7).getList().get(7);
            fColumnValues[59] = karnaughModels.get(7).getList().get(6);
            fColumnValues[60] = karnaughModels.get(7).getList().get(0);
            fColumnValues[61] = karnaughModels.get(7).getList().get(1);
            fColumnValues[62] = karnaughModels.get(7).getList().get(3);
            fColumnValues[63] = karnaughModels.get(7).getList().get(2);
        } else {
            fColumnValues = new String[0];
        }


        Intent intent = new Intent(MainKarnaughMap.this, FormulaKarnaughMap.class);
        intent.putExtra("fColumnValues", fColumnValues); // Pass the "F" column values
        intent.putExtra("variableCount", variables); // Pass the number of variables
        startActivity(intent);
    }

    private void setData() {
        karnaughModels.clear();
        String[] arrays = {"0", "0", "0", "0","0", "0", "0", "0"};
        karnaughModels.add(new KarnaughModel("A'", "A", "0", "1", "3", "2", "4", "5", "7", "6", Arrays.asList(arrays)));

        recyclerView.setAdapter(karnaughAdapter);
    }


    private void set4Variables() {
        karnaughModels.clear();
        String[] arrays = {"0", "0", "0", "0","0", "0", "0", "0"};
        karnaughModels.add(new KarnaughModel("A'B'", "A'B", "0", "1", "3", "2", "4", "5", "7", "6", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("AB", "AB'", "12", "13", "15", "14", "8", "9", "11", "10", Arrays.asList(arrays)));

        recyclerView.setAdapter(karnaughAdapter);
    }


    private void set5Variables() {
        karnaughModels.clear();
        String[] arrays = {"0", "0", "0", "0","0", "0", "0", "0"};
        karnaughModels.add(new KarnaughModel("A'B'C'", "A'B'C", "0", "1", "3", "2", "4", "5", "7", "6", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("A'BC", "A'BC'", "12", "13", "15", "14", "8", "9", "11", "10", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("AB'C'", "AB'C", "16", "17", "19", "18", "20", "21", "23", "22", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("ABC", "ABC''", "28", "29", "31", "30", "24", "25", "27", "26", Arrays.asList(arrays)));

        recyclerView.setAdapter(karnaughAdapter);
    }


    private void set6Variables() {
        karnaughModels.clear();
        String[] arrays = {"0", "0", "0", "0","0", "0", "0", "0"};
        karnaughModels.add(new KarnaughModel("A'B'C'D'", "A'B'C'D", "0", "1", "3", "2", "4", "5", "7", "6", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("A'B'CD", "A'B'CD''", "12", "13", "15", "14", "8", "9", "11", "10", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("A'BC'D''", "A'BC'D", "16", "17", "19", "18", "20", "21", "23", "22", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("A'BCD", "A'BCD'", "28", "29", "31", "30", "24", "25", "27", "26", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("AB'C'D'", "AB'C'D", "32", "33", "35", "34", "36", "37", "39", "38", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("AB'CD", "AB'CD'", "44", "45", "47", "46", "40", "41", "43", "42", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("ABC'D'", "ABC'D", "48", "49", "51", "50", "52", "53", "55", "54", Arrays.asList(arrays)));
        karnaughModels.add(new KarnaughModel("ABCD", "ABCD'", "60", "61", "63", "62", "56", "57", "59", "58", Arrays.asList(arrays)));

        recyclerView.setAdapter(karnaughAdapter);
    }

    private void dropdown() {
        dropdownButton = findViewById(R.id.dropdownButton);
        // In your activity
        dropdownButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.variable_dropdown, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.two) {
                    // Handle "two" click
                    String[] arrays = {"0", "0", "0", "0"};
                    second();
                    minimers = Arrays.asList(arrays);
                    morethan2.setVisibility(View.GONE);
                    equal2.setVisibility(View.VISIBLE);
                    variables = 2;
                    dropdownButton.setText("2 variables");
                } else if (id == R.id.three) {
                    // Handle "three" click
//                    String[] arrays = {"0", "0", "0", "0","0", "0", "0", "0"};
                    dropdownButton.setText("3 variables");
                    setData();
                    morethan2.setVisibility(View.VISIBLE);
                    equal2.setVisibility(View.GONE);
                    txtFirst.setText("B'C'");
                    txtSecond.setText("B'C");
                    txtThird.setText("BC");
                    txtFourth.setText("BC'");
                    variables = 3;


                } else if (id == R.id.four) {
                    // Handle "four" click
                    dropdownButton.setText("4 variables");
                    set4Variables();
                    morethan2.setVisibility(View.VISIBLE);
                    equal2.setVisibility(View.GONE);
                    txtFirst.setText("C'D'");
                    txtSecond.setText("C'D");
                    txtThird.setText("CD");
                    txtFourth.setText("CD'");
                    variables = 4;

                } else if (id == R.id.five) {
                    // Handle "five" click
                    set5Variables();
                    morethan2.setVisibility(View.VISIBLE);
                    equal2.setVisibility(View.GONE);
                    dropdownButton.setText("5 variables");
                    txtFirst.setText("D'E'");
                    txtSecond.setText("D'E");
                    txtThird.setText("DE");
                    txtFourth.setText("DE'");
                    variables = 5;

                } else if (id == R.id.six) {
                    // Handle "six" click
                    dropdownButton.setText("6 variables");
                    morethan2.setVisibility(View.VISIBLE);
                    set6Variables();
                    equal2.setVisibility(View.GONE);
                    txtFirst.setText("E'F'");
                    txtSecond.setText("E'F");
                    txtThird.setText("EF");
                    txtFourth.setText("EF'");
                    variables = 6;

                } else {
                    // If none of the above, return false.
                    return false;
                }
                return true;
            });
            popupMenu.show();
        });

    }

    private void second() {
        MaterialButton btn0 = findViewById(R.id.btn0);
        MaterialButton btn1 = findViewById(R.id.btn1);
        MaterialButton btn2 = findViewById(R.id.btn2);
        MaterialButton btn3 = findViewById(R.id.btn3);

        for2variables.add("0");
        for2variables.add("0");
        for2variables.add("0");
        for2variables.add("0");

        btn1.setOnClickListener(v -> {
            listener(btn1, 1);
        });

        btn0.setOnClickListener(v -> {
            listener(btn0, 0);
        });

        btn2.setOnClickListener(v -> {
            listener(btn2, 2);
        });

        btn3.setOnClickListener(v -> {
            listener(btn3, 3);
        });
    }



    private void listener(Button button, int arr ) {
        String buttonText = button.getText().toString();
        switch (buttonText) {
            case "0":
                for2variables.set(arr, "1");
                button.setText("1");
                break;
            case "1":
                button.setText("x");
                for2variables.set(arr, "x");
                break;
            case "x":
                button.setText("0");
                for2variables.set(arr, "0");
                break;
            default:
                // Handle unexpected button text if needed
                break;
        }
    }
    @Override
    public void onItemClick(int pos, String categories) {

    }
}