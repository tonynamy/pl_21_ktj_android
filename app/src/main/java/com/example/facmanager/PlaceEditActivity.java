package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PlaceEditActivity extends AppCompatActivity {

    Boolean isPlaceEdit;
    String place_id;
    String place_name;

    EditText eTextPlaceNameEdit;
    Button buttonPlaceEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit);

        //Intent에서 정보가져오기
        isPlaceEdit = getIntent().getBooleanExtra("isPlaceEdit", false);
        place_id = getIntent().getStringExtra("place_id");
        place_name = getIntent().getStringExtra("place_name");

        //뷰 정보 가져오기
        eTextPlaceNameEdit = findViewById(R.id.eTextPlaceNameEdit);
        buttonPlaceEdit = findViewById(R.id.buttonPlaceEdit);

        if(isPlaceEdit){
            if(!place_name.isEmpty()){
                eTextPlaceNameEdit.setText(place_name);
            }
            buttonPlaceEdit.setText("변경");
        } else {
            buttonPlaceEdit.setText("추가");
        }
    }
}