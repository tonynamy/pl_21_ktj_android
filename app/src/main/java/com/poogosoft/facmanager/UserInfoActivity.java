package com.poogosoft.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {

    int level;
    String place_id;

    TextView textUserResult;
    RecyclerView recyclerUserInfo;
    UserInfoAdapter userInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //Intetn에서 가져오기
        level = getIntent().getIntExtra("level", 0);
        place_id = getIntent().getStringExtra("place_id");

        //뷰에서 불러오기
        textUserResult = findViewById(R.id.textUserResult);
        recyclerUserInfo = findViewById(R.id.recyclerUserInfo);

        //리사이클러뷰 설정
        userInfoAdapter = new UserInfoAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerUserInfo.setLayoutManager(layoutManager);
        recyclerUserInfo.setAdapter(userInfoAdapter);

        //아이템 클릭시
        userInfoAdapter.setOnUserInfoClicked(new UserInfoAdapter.OnUserInfoClicked() {
            @Override
            public void onClick(View v, UserInfoItem userInfoItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_change_level, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                TextView textLevelUserName = view.findViewById(R.id.textLevelUserName);
                TextView textLevelAdmin = view.findViewById(R.id.textLevelAdmin);
                TextView textLevelManager = view.findViewById(R.id.textLevelManager);
                TextView textLevelTeamLeader = view.findViewById(R.id.textLevelTeamLeader);
                TextView textLevelAwaiter = view.findViewById(R.id.textLevelAwaiter);
                TextView textUserDelete = view.findViewById(R.id.textUserDelete);
                TextView textDialogCancel7 = view.findViewById(R.id.textDialogCancel7);

                //Admin(4)일때만 최고관리자로 변경, 사용자 삭제 가능
                if(level != 4) {
                    textLevelAdmin.setVisibility(View.GONE);
                    textUserDelete.setVisibility(View.GONE);
                }

                textLevelUserName.setText(userInfoItem.user_name + " 직원등급");

                switch(userInfoItem.user_level) {
                    case 0:
                        textLevelAwaiter.setVisibility(View.GONE);
                        break;
                    case 1:
                        textLevelTeamLeader.setVisibility(View.GONE);
                        break;
                    case 2:
                        textLevelManager.setVisibility(View.GONE);
                        break;
                }

                dialog.show();

                ArrayList<TextView> textLevelList = new ArrayList<>();
                textLevelList.add(textLevelAwaiter); //0
                textLevelList.add(textLevelTeamLeader); //1
                textLevelList.add(textLevelManager); //2
                textLevelList.add(textLevelAdmin); //3

                //직원등급 선택시
                for(int i = 0; i < textLevelList.size(); i++) {
                    int level = i;
                    textLevelList.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            API.APICallback apiCallback = new API.APICallback() {
                                @Override
                                public void onSuccess(Object data) {
                                    loadUserInfo();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    Toast.makeText(UserInfoActivity.this, "직원 등급을 변경하는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            };
                            API api = new API.Builder(apiCallback).build();
                            api.editUserLevel(place_id, userInfoItem.user_id, level);

                            dialog.dismiss();
                        }
                    });
                }

                //사용자삭제 선택시
                textUserDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        API.APICallback apiCallback = new API.APICallback() {
                            @Override
                            public void onSuccess(Object data) {
                                loadUserInfo();
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                Toast.makeText(UserInfoActivity.this, "직원 등급을 변경하는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        };
                        API api = new API.Builder(apiCallback).build();
                        api.deleteUser(place_id, userInfoItem.user_id);

                        dialog.dismiss();
                    }
                });

                //취소 선택시
                textDialogCancel7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        //사용자정보 불러오기
        loadUserInfo();
    }

    private void loadUserInfo() {

        userInfoAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                ArrayList<UserInfoItem> userInfoList = (ArrayList<UserInfoItem>) data;

                if(userInfoList.size() > 0) {
                    textUserResult.setVisibility(View.GONE);
                } else {
                    textUserResult.setText("직원정보가 없습니다.\n사용자를 생성해주세요.");
                    textUserResult.setVisibility(View.VISIBLE);
                }

                for(UserInfoItem userInfoItem : userInfoList) {
                    userInfoAdapter.addItem(userInfoItem);
                }
                userInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                textUserResult.setText("직원정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                textUserResult.setVisibility(View.VISIBLE);
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getUser(place_id);
    }
}