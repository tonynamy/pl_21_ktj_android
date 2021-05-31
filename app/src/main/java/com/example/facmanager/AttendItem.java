package com.example.facmanager;

import android.widget.Button;

public class AttendItem {
    String txtName;
    String txtAttend;
    Button btnAttend;

    public AttendItem(String txtName, String txtAttend) {
        this.txtName = txtName;
        this.txtAttend = txtAttend;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtAttend() {
        return txtAttend;
    }

    public void setTxtAttend(String txtAttend) {
        this.txtAttend = txtAttend;
    }

    public Button getBtnAttend() {
        return btnAttend;
    }

    public void setBtnAttend(Button btnAttend) {
        this.btnAttend = btnAttend;
    }
}
