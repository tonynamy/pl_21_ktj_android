package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductivityActivity extends AppCompatActivity {

    String place_id;

    RecyclerView recyclerProductivity;
    ProductivityAdapter productivityAdapter;
    TextView textProductivityResult;
    Button buttonProductivityMonth;

    int month = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productivity);

        //Intent에서 가져오기
        place_id = getIntent().getStringExtra("place_id");

        //뷰에서 가져오기
        recyclerProductivity = findViewById(R.id.recyclerProductivity);
        textProductivityResult = findViewById(R.id.textProductivityResult);
        buttonProductivityMonth = findViewById(R.id.buttonProductivityMonth);

        //리사이클러 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerProductivity.setLayoutManager(layoutManager);
        productivityAdapter = new ProductivityAdapter();
        recyclerProductivity.setAdapter(productivityAdapter);

        //팀 선택시
        productivityAdapter.setOnItemClickListener(new ProductivityAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, String team_id) {
                Intent intent = new Intent(v.getContext(), ProductivityTeamActivity.class);
                intent.putExtra("team_id", team_id);
                startActivity(intent);
            }
        });

        renderMonthButton();

        //생산성정보 가져오기
        loadProductivity();
    }

    private void renderMonthButton() {

        if(month == 0) {

            buttonProductivityMonth.setText("지난달보기");
            buttonProductivityMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    month = -1;
                    loadProductivity();
                    renderMonthButton();
                }
            });

        } else {

            buttonProductivityMonth.setText("이번달보기");
            buttonProductivityMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    month = 0;
                    loadProductivity();
                    renderMonthButton();
                }
            });

        }

    }

    private void loadProductivity() {

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                productivityAdapter.clear();

                ArrayList<ProductivityItem> productivityItems = (ArrayList<ProductivityItem>) data;

                for(ProductivityItem productivityItem : productivityItems) {
                    productivityAdapter.addItem(productivityItem);
                }

                textProductivityResult.setVisibility(View.GONE);
                productivityAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMsg) {

                if(errorMsg.equals("Not Found")){
                    textProductivityResult.setText("검색된 팀이 없습니다.\n웹서비스에서 팀등록버튼을 눌러 팀을 추가해주세요.");
                } else {
                    textProductivityResult.setText("팀정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                }

            }
        };
        API api = new API.Builder(apiCallback).build();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);

        api.getProductivity(place_id, calendar.getTime());

    }
}