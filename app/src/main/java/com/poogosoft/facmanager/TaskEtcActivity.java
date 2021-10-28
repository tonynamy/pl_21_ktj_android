package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.TaskplanEtc;

import java.util.ArrayList;

public class TaskEtcActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    String task_id;
    int button_right;
    
    TaskplanEtc taskplanEtc = new TaskplanEtc();
    int manday = 1;

    TextView textEtcTaskResult;
    ConstraintLayout layoutEtcTask;
    TextView textEtcTaskName;
    SeekBar seekBarEtcTask;
    TextView textEtcTaskState;
    Button buttonEtcTaskFin;
    LinearLayout layoutEtcTeamLeader;
    ImageView imageUp2;
    TextView textManday2;
    ImageView imageDown2;
    Button buttonEtcTask;
    Button buttonEtcTaskplan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_etc);

        //Intent에서 가져오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        if(team_id == null) { team_id = ""; }
        task_id = getIntent().getStringExtra("task_id");
        button_right = getIntent().getIntExtra("button_right", 0);

        //뷰에서 가져오기
        textEtcTaskResult = findViewById(R.id.textEtcTaskResult);
        layoutEtcTask = findViewById(R.id.layoutEtcTask);
        textEtcTaskName = findViewById(R.id.textEtcTaskName);
        seekBarEtcTask = findViewById(R.id.seekBarEtcTask);
        textEtcTaskState = findViewById(R.id.textEtcTaskState);
        buttonEtcTaskFin = findViewById(R.id.buttonEtcTaskFin);
        layoutEtcTeamLeader = findViewById(R.id.layoutEtcTeamLeader);
        imageUp2 = findViewById(R.id.imageUp2);
        textManday2 = findViewById(R.id.textManday2);
        imageDown2 = findViewById(R.id.imageDown2);
        buttonEtcTask = findViewById(R.id.buttonEtcTask);
        buttonEtcTaskplan = findViewById(R.id.buttonEtcTaskplan);

        //SeekBar에 터치가 안되게
        seekBarEtcTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //권한이 필요한 모든 뷰 비활성화
        layoutEtcTask.setVisibility(View.GONE);
        buttonEtcTaskFin.setVisibility(View.GONE);
        layoutEtcTeamLeader.setVisibility(View.GONE);
        buttonEtcTaskplan.setVisibility(View.GONE);

        //관리자 버튼으로 왔을시
        if(button_right == ButtonRight.TEAM_LEADER && !team_id.isEmpty()) {
            layoutEtcTeamLeader.setVisibility(View.VISIBLE);
        } else if(level == 1 && !team_id.isEmpty()) {
            layoutEtcTeamLeader.setVisibility(View.VISIBLE);
        } else if(level == 2 || level == 3 || level == 4) {
            buttonEtcTaskplan.setVisibility(View.VISIBLE);
        }

        //작업완료 버튼을 눌렀을때

        //투입인원 텍스트박스
        textManday2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manday = Integer.parseInt(taskplanEtc.attendance) > 1 ? Integer.parseInt(taskplanEtc.attendance) : 1;
                textManday2.setText(String.valueOf(manday));
            }
        });

        //투입인원 화살표
        ArrayList<ImageView> arrow_list = new ArrayList<>();
        arrow_list.add(imageUp2);
        arrow_list.add(imageDown2);
        for(int i = 0; i < arrow_list.size(); i++) {
            ImageView arrow_image = arrow_list.get(i);
            int num = i;
            arrow_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(num == 0 && manday < 100) {
                        manday++;
                    } else if(num == 1 && manday > 1) {
                        manday--;
                    }
                    textManday2.setText(String.valueOf(manday));
                }
            });
        }

        //작업버튼
        buttonEtcTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Toast.makeText(v.getContext(), "작업보고에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        getEtcTaskplan();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(v.getContext(), "작업보고에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };
                API api = new API.Builder(apiCallback).build();
                api.addTask(team_id, taskplanEtc.name, manday, 4);

            }
        });

        getEtcTaskplan();
    }

    private void getEtcTaskplan() {
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                TaskplanEtc taskplanEtc = (TaskplanEtc) data;

                setEtcTaskplan(taskplanEtc);
            }

            @Override
            public void onFailed(String errorMsg) {
                textEtcTaskResult.setGravity(Gravity.NO_GRAVITY);
                textEtcTaskResult.setText("작업정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                textEtcTaskResult.setVisibility(View.VISIBLE);
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getEtcTaskplan(task_id);
    }

    private void setEtcTaskplan(TaskplanEtc taskplanEtc) {

        this.taskplanEtc = taskplanEtc;

        onEtcTaskplanChanged();
    }

    private void onEtcTaskplanChanged() {

        //작업명
        textEtcTaskName.setText(taskplanEtc.name);

        //작업이 있을시
        if(taskplanEtc.in_task) {
            seekBarEtcTask.setProgress(1);
            textEtcTaskState.setText("작업중");
            buttonEtcTaskFin.setVisibility(View.VISIBLE);
        }

        //모든과정이 끝나면 안내문 지우고 레이아웃 보여주기
        textEtcTaskResult.setVisibility(View.GONE);
        layoutEtcTask.setVisibility(View.VISIBLE);
    }
}