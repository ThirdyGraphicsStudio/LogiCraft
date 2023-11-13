package com.thirdy.booleanexpression.TruthTable;

import android.content.Context;
import android.widget.TableRow;

// You might need to extend TableRow to add getIndex method
public class IndexedTableRow extends TableRow {
    private int index;

    public IndexedTableRow(Context context, int index) {
        super(context);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
