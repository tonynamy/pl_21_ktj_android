package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDeleteActivity extends AppCompatActivity {

    String place_id;
    String place_name;

    TextView textPlaceName;
    Button buttonPlaceDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_delete);

        //Intent에서 정보가져오기
        place_id = getIntent().getStringExtra("place_id");
        place_name = getIntent().getStringExtra("place_name");

        //뷰에서 가져오기
        textPlaceName = findViewById(R.id.textPlaceName);
        buttonPlaceDelete = findViewById(R.id.buttonPlaceDelete);

        textPlaceName.setText(place_name);


        buttonPlaceDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                finish();
                Toast.makeText(v.getContext(), place_name + "의 기록이 모두 삭제되었습니다", Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }
}