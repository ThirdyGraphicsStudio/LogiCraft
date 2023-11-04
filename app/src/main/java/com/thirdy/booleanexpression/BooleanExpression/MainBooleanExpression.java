package com.thirdy.booleanexpression.BooleanExpression;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.thirdy.booleanexpression.R;

public class MainBooleanExpression extends AppCompatActivity {
    private EditText displayEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_boolean_expression);

        displayEditText = findViewById(R.id.displayEditText);
        CardView btnCurlyDash = findViewById(R.id.btnCurlyDash);
        CardView btnSlash = findViewById(R.id.btnSlash);
        CardView btnSingleAtmosphere = findViewById(R.id.btnSingleAtmosphere);
        CardView btnDeleteAll = findViewById(R.id.btnDeleteAll);
        CardView btnBackSpace = findViewById(R.id.btnBackSpace);
        CardView btnA = findViewById(R.id.btnA);
        CardView btnB = findViewById(R.id.btnB);
        CardView btnC = findViewById(R.id.btnC);
        CardView btnOP = findViewById(R.id.btnOP);
        CardView btnCP = findViewById(R.id.btnCP);
        CardView btnD = findViewById(R.id.btnD);
        CardView btnE = findViewById(R.id.btnE);
        CardView btnF = findViewById(R.id.btnF);
        CardView btnDot = findViewById(R.id.btnDot);
        CardView btnAdd = findViewById(R.id.btnAdd);
        CardView btnT = findViewById(R.id.btnT);
        CardView btnZero = findViewById(R.id.btnZero);
        CardView btnOne = findViewById(R.id.btnOne);
        CardView btnExlamation = findViewById(R.id.btnExlamation);
        CardView btnMinus = findViewById(R.id.btnMinus);
        CardView btnAnd = findViewById(R.id.btnAnd);
        CardView btnEqual = findViewById(R.id.btnEqual);
        CardView btnGreaterThan = findViewById(R.id.btnGreaterThan);
        CardView btnLessThan = findViewById(R.id.btnLessThan);
        CardView btnNor = findViewById(R.id.btnNor);




        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

        MaterialButton btnGenerate = findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(view -> {
            String input = displayEditText.getText().toString();

            if(input.isEmpty()){
                displayEditText.setError("Please input a boolean expression");
                displayEditText.setFocusable(true);
                return;
            }

            if(!input.isEmpty()){
                startActivity(new Intent(MainBooleanExpression.this, FormulaBooleanExpression.class));
            }


        });
        displayEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(displayEditText.getWindowToken(), 0);
                }
            }
        });



        // Set the buttons' onClickListeners
        btnSingleAtmosphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDisplay("'");
            }
        });

        btnSlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDisplay("|");
            }
        });

        btnCurlyDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDisplay("~");
            }
        });


        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditText.setText("");
            }
        });

        btnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastCharacter();
            }
        });

        btnA.setOnClickListener(v -> {
                addToDisplay("A");
        });

        btnB.setOnClickListener(v -> {
            addToDisplay("B");
        });

        btnC.setOnClickListener(v -> {
            addToDisplay("C");
        });

        btnOP.setOnClickListener(v -> {
            addToDisplay("(");
        });

        btnCP.setOnClickListener(v -> {
            addToDisplay(")");
        });


        btnD.setOnClickListener(v -> {
            addToDisplay("D");
        });


        btnE.setOnClickListener(v -> {
            addToDisplay("E");
        });


        btnF.setOnClickListener(v -> {
            addToDisplay("F");
        });


        btnDot.setOnClickListener(v -> {
            addToDisplay(".");
        });


        btnAdd.setOnClickListener(v -> {
            addToDisplay("+");
        });

        btnT.setOnClickListener(v -> {
            addToDisplay("T");
        });

        btnZero.setOnClickListener(v -> {
            addToDisplay("0");
        });

        btnOne.setOnClickListener(v -> {
            addToDisplay("1");
        });

        btnExlamation.setOnClickListener(v -> {
            addToDisplay("!");
        });

        btnMinus.setOnClickListener(v -> {
            addToDisplay("-");
        });

        btnAnd.setOnClickListener(v -> {
            addToDisplay("&");
        });

        btnEqual.setOnClickListener(v -> {
            addToDisplay("=");
        });

        btnLessThan.setOnClickListener(v -> {
            addToDisplay("<");
        });

        btnGreaterThan.setOnClickListener(v -> {
            addToDisplay(">");
        });

        btnNor.setOnClickListener(v -> {
            addToDisplay("⊕");
        });



    }

    private void addToDisplay(String value) {
        String currentText = displayEditText.getText().toString();
        displayEditText.setText(currentText + value);
    }


    private void removeLastCharacter() {
        String currentText = displayEditText.getText().toString();
        if (currentText.length() > 0) {
            String newText = currentText.substring(0, currentText.length() - 1);
            displayEditText.setText(newText);
            displayEditText.setSelection(newText.length()); // position the cursor at the end
        }
    }

}