package com.example.a26740.todo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import org.litepal.util.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int status = 0;//判断读写
    public static final int WRITE = 1;
    public static final int READ = 2;//1 新增，2 读取已经存在的
    private RecyclerView recyclerView;
    private List<Event> eventList = new ArrayList<>();
    private EventAdapter adapter;
    private android.support.v7.app.ActionBar actionBar;
    private boolean showboxTag = false;//判断是不是显示CheckBox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        final ImageButton deletebtn = (ImageButton)findViewById(R.id.menu1);
        TextView title = (TextView)findViewById(R.id.title);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            //左上角菜单键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        eventList = DataSupport.findAll(Event.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                status = 1;
                intent.putExtra("Status", status);//传递信息
                startActivity(intent);
            }
        });


        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);//创建dialog
                dialog.setTitle("删除便签");
                dialog.setMessage("您确定要删除所选便签？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取保存通过CheckBox选中的便签，选中状态由adapter中的map保存，通过getMap()获取
                        Map<Integer,Boolean> map = adapter.getMap();
                        //从后往前删除
                        for(int i=map.size()-1; i>=0; i--){
                            if(map.get(i)){
                                //i为CheckBox选中的view的position
                                int id = eventList.get(i).getId();
                                deleteData(id);
                                //noteList中移除
                                eventList.remove(i);
                                //adapter重新设置item
                                adapter.notifyItemRemoved(i);

                            }

                        }
                        //adapter长度重新设置
                        adapter.notifyItemRangeChanged(0,eventList.size());
                        //删除后回到正常状态，CheckBox不显示，map重新归false
                        adapter.MUL_tag  = false;
                        adapter.initMaps();
                        showboxTag = false;
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//取消dialog
                    }
                });
                dialog.show();
            }
        });

        //deletebtn.setOnTouchListener(new View.OnTouchListener() {
           // @Override
           // public boolean onTouch(View v, MotionEvent event) {
             //   //delete键按下后背景发生变化
              //  if(event.getAction() == MotionEvent.ACTION_DOWN)
              //      deletebtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
              //  else if(event.getAction() == MotionEvent.ACTION_UP)
               //     deletebtn.setBackgroundColor(getResources().getColor(R.color.colorYellow));
             //   return false;
           // }
      //  });

        //创建adapter
        adapter = new EventAdapter(eventList);
        //设置监听
        adapter.setRecycleViewOnItemClickListener(new EventAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                Event event = eventList.get(position);
                status = 2;
                intent.putExtra("Status", status);
                intent.putExtra("Content",event);//传递对象
                startActivity(intent);


            }
            //长按时间
            @Override
            public boolean onLongClickListener(View view, int position) {
                //长按显示CheckBox，并且长按位置选中该便签吖
                adapter.setSelection(position);
                adapter.setCheckBox();
                adapter.notifyDataSetChanged();
                showboxTag = true;
                return true;
            }
        });
        //瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


    }



    @Override
    protected void onResume() {
        super.onResume();
        status = 0;
        eventList.clear();
        List<Event> newList = DataSupport.findAll(Event.class);
        eventList.addAll(newList);
        adapter.MUL_tag = false;
        adapter.initMaps();
        showboxTag = false;
        adapter.notifyDataSetChanged();
    }

    public void deleteData(int id) {
        DataSupport.deleteAll(Event.class, "id = ?", String.valueOf(id));
    }

    @Override
    public void onBackPressed() {
        //处于多选，按下返回键回到正常
        if (showboxTag) {
            adapter.MUL_tag = false;
            adapter.initMaps();
            adapter.notifyDataSetChanged();
            showboxTag = false;
        } else super.onBackPressed();
    }
}
