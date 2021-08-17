package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Place;

import java.util.ArrayList;

public class PlaceActivity extends AppCompatActivity {

    RecyclerView recyclerPlace;
    PlaceAdapter placeAdapter;
    TextView textPlaceResult;
    Button btnCreatePlace;

    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        //뷰 가져오기
        recyclerPlace = findViewById(R.id.recyclerPlace);
        textPlaceResult = findViewById(R.id.textPlaceResult);
        btnCreatePlace = findViewById(R.id.btnCreatePlace);

        placeAdapter = new PlaceAdapter();

        //리사이클러 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPlace.setLayoutManager(layoutManager);
        recyclerPlace.setAdapter(placeAdapter);

        loadPlaceInfo();

        placeAdapter.setOnPlaceItemClicked(new PlaceAdapter.OnPlaceClicked() {
            @Override
            public void onClick(View v, Place place) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_place_option, null);
                builder.setView(view);

                TextView textPlaceName = view.findViewById(R.id.textPlaceName);
                TextView textChangePlaceName = view.findViewById(R.id.textChangePlaceName);
                TextView textDeletePlace = view.findViewById(R.id.textDeletePlace);
                TextView textDialogCancel6 = view.findViewById(R.id.textDialogCancel6);

                textPlaceName.setText(place.name);

                AlertDialog dialog = builder.create();
                dialog.show();

                //현장명변경
                textChangePlaceName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PlaceEditActivity.class);
                        intent.putExtra("isPlaceEdit", true);
                        intent.putExtra("place_id", place.id);
                        intent.putExtra("place_name", place.name);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                //현장기록삭제
                textDeletePlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PlaceDeleteActivity.class);
                        intent.putExtra("place_id", place.id);
                        intent.putExtra("place_name", place.name);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                //취소
                textDialogCancel6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //현장추가버튼
        btnCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Intent intent = new Intent(v.getContext(), PlaceEditActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String errorMsg) {

                        Toast.makeText(v.getContext(), "현장을 추가할수 없습니다\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };
                API api = new API.Builder(apiCallback).build();
                api.authCheck();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loadPlaceInfo();
    }

    private void loadPlaceInfo() {
        placeAdapter.clear();
        //API
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                places = (ArrayList<Place>) data;

                for(Place place : places) {
                    Place placeItem = new Place();
                    placeItem.id = place.id;
                    placeItem.name = place.name;
                    placeAdapter.addItem(placeItem);
                }
                textPlaceResult.setVisibility(View.GONE);
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                if(errorMsg.equals("Not Found")) {
                    textPlaceResult.setText("현장정보가 없습니다.\n아래의 현장추가버튼을 눌러 현장을 추가해주세요.");
                } else {
                    textPlaceResult.setText("현장정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                }
            }
        };

        API api = new API.Builder(apiCallback).build();
        api.getPlaces(true);
    }
}