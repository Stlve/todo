package com.example.a26740.todo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toolbar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {
    private android.support.v7.app.ActionBar actionBar;
    private int status;
    private EditText contentText;
    private ImageButton menuView;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        contentText = (EditText)findViewById(R.id.editText2);
        menuView = (ImageButton)findViewById(R.id.menu2);
       setSupportActionBar(toolbar);
       actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Intent intent = getIntent();
        status = intent.getIntExtra("Status",0);
        if(status == MainActivity.READ){
            //读操作
            ArrayList<Event> nEvents = (ArrayList<Event>) DataSupport.findAll(Event.class);
            int position = intent.getIntExtra("position",0);
            event = nEvents.get(position);
            //显示内容和游标放到内容末端
            contentText.setText(event.getName());
            contentText.setSelection(event.getName().length());
        }
        //按钮按下背景变化
        //menuView.setOnTouchListener(new View.OnTouchListener() {
           // @Override
           //public boolean onTouch(View v, MotionEvent event) {
              //  if(event.getAction() == MotionEvent.ACTION_DOWN)
              //      menuView.setBackgroundColor(getResources().getColor(R.color.colorYellow));
              //  else if(event.getAction() == MotionEvent.ACTION_UP)
               //     menuView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
              //  return false;
//        });

        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增便签
                if(status == MainActivity.WRITE){
                    saveData();
                }
                else if(status == MainActivity.READ){
                    //读取便签后更新数据
                    updateData();
                }
               finish();
            }
        });
    }

    //左上角home键
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(WriteActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    public void updateData(){
        if(!(contentText.getText().toString().equals(""))){
            event.setName(contentText.getText().toString());
            event.save();
        }

    }

    public void saveData(){
        if (contentText.getText() != null){
            if (!(contentText.getText().toString().equals(""))){
                Event event = new Event();
                event.setName(contentText.getText().toString());
                event.save();
            }
        }

    }

    }

