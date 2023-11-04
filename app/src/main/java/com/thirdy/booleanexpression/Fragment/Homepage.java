package com.thirdy.booleanexpression.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.thirdy.booleanexpression.BooleanExpression.MainBooleanExpression;
import com.thirdy.booleanexpression.R;

public class Homepage extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_homepage, container, false);

        LinearLayout cardViewTruthTable = view.findViewById(R.id.cardViewTruthTable);
        LinearLayout cardViewKarnaughMap = view.findViewById(R.id.cardViewKarnaughMap);
        LinearLayout cardViewBooleanExpression = view.findViewById(R.id.cardViewBooleanExpression);


        cardViewTruthTable.setOnClickListener(view1 -> {

        });

        cardViewKarnaughMap.setOnClickListener(view1 -> {

        });
        cardViewBooleanExpression.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), MainBooleanExpression.class));
        });


        return  view;

    }

}