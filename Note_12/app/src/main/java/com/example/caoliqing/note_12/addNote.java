package com.example.caoliqing.note_12;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by caoliqing on 2017/3/12.
 */

public class addNote extends Activity implements View.OnClickListener {

    private String val;
    private Button okButt,cancleButt;//保存取消
    private EditText thisContent;//文件内容
    private String editContent;
    private TextView ThisDate;//时间
    private NoteDateBaseHelper DBHelper;
    public int enter_state = 0;//用来区分是新建一个note还是更改原来的note

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cell);
        InitView();
    }
    private void InitView(){
        thisContent = (EditText)findViewById(R.id.this_content);
        ThisDate=(TextView) findViewById(R.id.list_date);
        okButt=(Button) findViewById(R.id.btn_ok);
        cancleButt=(Button) findViewById(R.id.btn_cancel);
        DBHelper = new NoteDateBaseHelper(this);

        ///获取时间
        Date nowDate = new Date();
        SimpleDateFormat nowDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String nowDateString = nowDateFormat.format(nowDate);
        ThisDate.setText(nowDateString);

        //内容
        Bundle thisBundle = this.getIntent().getExtras();//获取句柄,不同acitivity通讯

        enter_state = thisBundle.getInt("enter_state");
       if(enter_state==1){
           editContent = thisBundle.getString("information");//通过information获取内容给editContent
           thisContent.setText(editContent);//显示内容
       }

        okButt.setOnClickListener(this);
        cancleButt.setOnClickListener(this);

    }

    @Override
    public void onClick(View nextView) {
        switch (nextView.getId()){
            case R.id.btn_ok:
                saveNoteInformation();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:break;
        }
    }

    private void saveNoteInformation(){
        SQLiteDatabase db =DBHelper.getReadableDatabase();//获取内容
        String saveContent =  thisContent.getText().toString();
        //参考网上教程设计失误，这里本来直接用上面的editContent内容就可以了

        ContentValues contentValue = new ContentValues();

        if(enter_state == 0){//没有
            Date nowDate = new Date();
            SimpleDateFormat nowDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String nowDateString = nowDateFormat.format(nowDate);

            contentValue.put("content",saveContent);//内容
            contentValue.put("date",nowDateString);//日期
            db.insert("note",null,contentValue);//插入
            finish();
        }else{//已有
            contentValue.put("content",saveContent);
            db.update("note",contentValue,"content = ?",new String[]{editContent});
            //不是很理解，待查询
            finish();
        }
    }
}
