package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


        buttonPlaceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPlaceEdit) {
                    API.APICallback apiCallback = new API.APICallback() {
                        @Override
                        public void onSuccess(Object data) {
                            Toast.makeText(v.getContext(), "현장추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(v.getContext(), "현장추가에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };
                    API api = new API.Builder(apiCallback).build();
                    api.editPlace(place_id, eTextPlaceNameEdit.getText().toString());

                } else {

                    API.APICallback apiCallback = new API.APICallback() {
                        @Override
                        public void onSuccess(Object data) {
                            Toast.makeText(v.getContext(), "현장추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(v.getContext(), "현장추가에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };
                    API api = new API.Builder(apiCallback).build();
                    api.addPlace(eTextPlaceNameEdit.getText().toString());
                }

            }
        });


    }
}