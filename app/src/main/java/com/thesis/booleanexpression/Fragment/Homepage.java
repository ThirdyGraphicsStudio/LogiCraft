package com.thesis.booleanexpression.Fragment;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.thesis.booleanexpression.BooleanExpression.MainBooleanExpression;
import com.thesis.booleanexpression.KarnaughMap.MainKarnaughMap;
import com.thesis.booleanexpression.R;
import com.thesis.booleanexpression.TruthTable.MainTruthTable;

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
            startActivity(new Intent(getContext(), MainTruthTable.class));
        });

        cardViewKarnaughMap.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), MainKarnaughMap.class));

        });
        cardViewBooleanExpression.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), MainBooleanExpression.class));
        });


        return  view;

    }

}