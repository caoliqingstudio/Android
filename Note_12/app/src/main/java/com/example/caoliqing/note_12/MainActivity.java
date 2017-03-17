package com.example.caoliqing.note_12;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caoliqing on 2017/3/12.
 */

public class MainActivity extends Activity implements
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private String val;
    private ListView listView;
    private Button addText;//首界面的添加备忘按钮
    private TextView thisContent;//显示内容
    private NoteDateBaseHelper DBHelper;
    private SQLiteDatabase DB;
    private List<Map<String,Object>> dateList;
    private SimpleAdapter simpleAdapter;//用于刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
    }

    @Override
    protected void onStart(){
        super.onStart();
        refresh();
    }

    private void InitView(){

        thisContent = (TextView)findViewById(R.id.list_content);
        listView = (ListView)findViewById(R.id.list_item);
        dateList = new ArrayList<Map<String, Object>>();
        addText = (Button) findViewById(R.id.add_text);
        DBHelper = new NoteDateBaseHelper(this);
        DB = DBHelper.getReadableDatabase();

        listView.setOnItemLongClickListener(this);//长按删除
        listView.setOnItemClickListener(this);//点击进入

        addText.setOnClickListener(new View.OnClickListener() {
            //添加
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addNote.class);
                Bundle bundle = new Bundle();
                bundle.putString("information","");
                bundle.putInt("enter_state",0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void refresh(){
        if(dateList.size()>0){
            dateList.removeAll(dateList);
            simpleAdapter.notifyDataSetChanged();
        }

        Cursor cursor = DB.query("note",null,null,null,null,null,null);
        startManagingCursor(cursor);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("list_date",date);
            map.put("list_content",name);

            dateList.add(map);
        }

        simpleAdapter = new SimpleAdapter(this,dateList,R.layout.list_item,new String[]{"list_content","list_date"},
                new int[]{R.id.list_content,R.id.list_date});
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String preContent = listView.getItemAtPosition(position)+"";
        String content = preContent.substring(preContent.indexOf("c")+8,preContent.indexOf("}"));
        //getItemAtPosition

        Intent intent = new Intent(MainActivity.this,addNote.class);
        Bundle bundle =new Bundle();
        bundle.putString("information",content);
        bundle.putInt("enter_state",1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("警告");
        builder.setMessage("确认删除？");
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String preContent = listView.getItemAtPosition(position)+"";
                String content = preContent.substring(preContent.indexOf("=")+1,preContent.indexOf(","));
                DB.delete("note","date = ?",new String[]{content});
                refresh();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
            //Nothing to do
        });
        builder.create();
        builder.show();
        return true;
    }
}
