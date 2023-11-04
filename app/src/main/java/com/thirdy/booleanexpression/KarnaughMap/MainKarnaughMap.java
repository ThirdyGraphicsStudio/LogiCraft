package com.thirdy.booleanexpression.KarnaughMap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.thirdy.booleanexpression.R;
import com.thirdy.booleanexpression.TruthTable.FormulaTruthTable;
import com.thirdy.booleanexpression.TruthTable.MainTruthTable;

public class MainKarnaughMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karnaugh_map_main);

        ImageButton back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            finish();
        });



        MaterialButton btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), FormulaKarnaughMap.class));
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
}