package com.thirdy.booleanexpression.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.thirdy.booleanexpression.R;

public class Help extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_help, container, false);


        //truth table
        final CardView headerText = view.findViewById(R.id.header1);
        final TextView contentText = view.findViewById(R.id.content_text);
        final ImageView dropdownIcon = view.findViewById(R.id.dropdown_icon);

        headerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentText.getVisibility() == View.GONE) {
                    contentText.setVisibility(View.VISIBLE);
                    dropdownIcon.setImageResource(R.drawable.minus);
                } else {
                    contentText.setVisibility(View.GONE);
                    dropdownIcon.setImageResource(R.drawable.add);

                }
            }
        });

        //karnaugh map

        final CardView headerText2 = view.findViewById(R.id.header2);
        final TextView contentText2 = view.findViewById(R.id.content_text2);
        final ImageView dropdownIcon2 = view.findViewById(R.id.dropdown_icon2);

        headerText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentText2.getVisibility() == View.GONE) {
                    contentText2.setVisibility(View.VISIBLE);
                    dropdownIcon2.setImageResource(R.drawable.minus);
                } else {
                    contentText2.setVisibility(View.GONE);
                    dropdownIcon2.setImageResource(R.drawable.add);

                }
            }
        });


        //boolean expression

        final CardView headerText3 = view.findViewById(R.id.header3);
        final TextView contentText3 = view.findViewById(R.id.content_text3);
        final ImageView dropdownIcon3 = view.findViewById(R.id.dropdown_icon3);

        headerText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentText3.getVisibility() == View.GONE) {
                    contentText3.setVisibility(View.VISIBLE);
                    dropdownIcon3.setImageResource(R.drawable.minus);
                } else {
                    contentText3.setVisibility(View.GONE);
                    dropdownIcon3.setImageResource(R.drawable.add);

                }
            }
        });


        TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Search clicked!", Toast.LENGTH_SHORT).show();
                // Implement your search logic here
            }
        });






        return  view;

    }
}