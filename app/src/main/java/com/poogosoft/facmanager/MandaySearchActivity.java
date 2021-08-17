package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MandaySearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manday_search);

        Button testButton = findViewById(R.id.button);
        TextView testText = findViewById(R.id.textView2);

        String formula = "=(1.85*1.5*11.2)+(1.85*1.5*4)";

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testText.setText(String.valueOf(Calculator(formula)));
            }
        });
    }

    private double Calculator(String formula) {

        double result = 0;



        return result;
    }
}