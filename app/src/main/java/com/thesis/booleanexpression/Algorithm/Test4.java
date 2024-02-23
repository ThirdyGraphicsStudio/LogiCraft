package com.thesis.booleanexpression.Algorithm;

import android.util.Log;

import java.util.ArrayList;

public class Test4 {

    // main for testing
    public static void main(String[] args) {
        String minterms = "0 5 7 9 12 15";

        String dontCares = "";

        Solver s = new Solver(minterms, dontCares);

        s.solve();
        s.printResults();

        ArrayList<ArrayList<Term>[]> s1 = s.step1;
        StringBuilder builder = new StringBuilder();

        // to print 1st step
        for (int i = 0; i < s1.size(); i++) {
            builder.append("Step ").append(i + 1).append("\n");
            for (int j = 0; j < s1.get(i).length; j++) {
                for (int k = 0; k < s1.get(i)[j].size(); k++) {
                    String stepString = s1.get(i)[j].get(k).getString();
                    builder.append(stepString);
                    if (s.taken_step1.size() > i && s.taken_step1.get(i).contains(stepString)) {
                        builder.append(" taken");
                    }
                    builder.append("\n");
                }
                builder.append("---------------------------\n");
            }
            builder.append("\n");
        }

        // Printing step2
        for (int k = 0; k < s.step2.size(); k++) {
            String[][] step2 = s.step2.get(k);
            for (int i = 0; i < step2.length; i++) {
                for (int j = 0; j < step2[0].length; j++) {
                    builder.append(step2[i][j]).append("  ");
                }
                builder.append("\n");
            }
            builder.append("\n");
        }

        // Printing petrickKey
        for (int i = 0; i < s.petrickKey.size(); i++) {
            builder.append(s.petrickKey.get(i)).append("\n");
        }

        // Printing step3
        for (int i = 0; i < s.step3.size(); i++) {
            builder.append(s.step3.get(i)).append("\n");
        }

        Log.d("StepLog", builder.toString());
    }

}