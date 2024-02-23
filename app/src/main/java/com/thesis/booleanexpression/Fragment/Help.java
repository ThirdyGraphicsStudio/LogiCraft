package com.thesis.booleanexpression.Fragment;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.thesis.booleanexpression.R;

public class Help extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_help, container, false);


        //truth table
        final CardView headerText = view.findViewById(R.id.header1);
        final LinearLayout contentText = view.findViewById(R.id.content_text);
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
        final LinearLayout contentText2 = view.findViewById(R.id.content_text2);
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
        final LinearLayout contentText3 = view.findViewById(R.id.content_text3);
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


        final CardView headerText4 = view.findViewById(R.id.header4);
        final LinearLayout contentText4 = view.findViewById(R.id.content_text4);
        final ImageView dropdownIcon4 = view.findViewById(R.id.dropdown_icon4);

        headerText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentText4.getVisibility() == View.GONE) {
                    contentText4.setVisibility(View.VISIBLE);
                    dropdownIcon4.setImageResource(R.drawable.minus);
                } else {
                    contentText4.setVisibility(View.GONE);
                    dropdownIcon4.setImageResource(R.drawable.add);

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



        TextView textView = view.findViewById(R.id.truth_table_step2);
        textView.setText(Html.fromHtml(getString(R.string.truth_table_step2), Html.FROM_HTML_MODE_LEGACY));


        TextView karnaughStep2 = view.findViewById(R.id.karnaughStep2);
        karnaughStep2.setText(Html.fromHtml(getString(R.string.karnaugh_step2), Html.FROM_HTML_MODE_LEGACY));

        TextView karnaughStep2v1 = view.findViewById(R.id.karnaughStep2v1);
        karnaughStep2v1.setText(Html.fromHtml(getString(R.string.karnaugh_step2v1), Html.FROM_HTML_MODE_LEGACY));

        TextView karnaughStep4 = view.findViewById(R.id.karnaughStep4);
        karnaughStep4.setText(Html.fromHtml(getString(R.string.karnaugh_step4), Html.FROM_HTML_MODE_LEGACY));


        TextView booleanStep1 = view.findViewById(R.id.booleanStep1);
        booleanStep1.setText(Html.fromHtml(getString(R.string.boolean_step1), Html.FROM_HTML_MODE_LEGACY));

        TextView booleanStep3 = view.findViewById(R.id.booleanStep3);
        booleanStep3.setText(Html.fromHtml(getString(R.string.boolean_step3), Html.FROM_HTML_MODE_LEGACY));


        return  view;

    }
}