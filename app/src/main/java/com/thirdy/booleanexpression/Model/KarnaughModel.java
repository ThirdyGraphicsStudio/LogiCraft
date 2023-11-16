package com.thirdy.booleanexpression.Model;

import java.util.List;

public class KarnaughModel {
    String column1;
    String column2;
    String txtdisplay0;
    String txtdisplay1;
    String txtdisplay2;
    String txtdisplay3;
    String txtdisplay4;
    String txtdisplay5;
    String txtdisplay6;
    String txtdisplay7;

    List<String> list;

    public KarnaughModel(String column1, String column2, String txtdisplay0, String txtdisplay1, String txtdisplay2, String txtdisplay3, String txtdisplay4, String txtdisplay5, String txtdisplay6, String txtdisplay7, List<String> list) {
        this.column1 = column1;
        this.column2 = column2;
        this.txtdisplay0 = txtdisplay0;
        this.txtdisplay1 = txtdisplay1;
        this.txtdisplay2 = txtdisplay2;
        this.txtdisplay3 = txtdisplay3;
        this.txtdisplay4 = txtdisplay4;
        this.txtdisplay5 = txtdisplay5;
        this.txtdisplay6 = txtdisplay6;
        this.txtdisplay7 = txtdisplay7;
        this.list = list;
    }

    public String getColumn1() {
        return column1;
    }

    public String getColumn2() {
        return column2;
    }




    public String getTxtdisplay0() {
        return txtdisplay0;
    }

    public String getTxtdisplay1() {
        return txtdisplay1;
    }

    public String getTxtdisplay2() {
        return txtdisplay2;
    }

    public String getTxtdisplay3() {
        return txtdisplay3;
    }

    public String getTxtdisplay4() {
        return txtdisplay4;
    }

    public String getTxtdisplay5() {
        return txtdisplay5;
    }

    public String getTxtdisplay6() {
        return txtdisplay6;
    }

    public String getTxtdisplay7() {
        return txtdisplay7;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
