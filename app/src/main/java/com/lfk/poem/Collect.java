package com.lfk.poem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class Collect extends Activity {
    private DBhelper dBhelper;
    private ListView listView;
    public static ArrayAdapter<String> mArrayAdapter;
    public static final String DB_NAME = "poem_all.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.lfk.poem";// 应用的包名
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() +"/"
            + PACKAGE_NAME+ "/databases"; // 在手机里存放数据库的位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        String[] data = new String[0];
        //dBhelper = new DBhelper(this,"poem_all.db",null,1);
        listView = (ListView)findViewById(R.id.list_view);
        mArrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item);
        listView.setAdapter(mArrayAdapter);
        FindyourCollect();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                System.out.println(arg2);
                String temp = (String)((TextView)arg1).getText();
                Intent intent = new Intent();
                intent.putExtra("title",temp);
                System.out.println(arg2);
                intent.setClass(Collect.this, Collect_item.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "Opening " + arg2,
                        Toast.LENGTH_SHORT).show();
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_collect, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    void FindyourCollect(){
        File myDataPath = new File(DB_PATH);
        String dbfile = myDataPath+"/"+DB_NAME;
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        Cursor cursor = database.rawQuery("Select * From poem where ticai = 1", null);
        Log.e("===================", "================");
        if(cursor.moveToFirst()) {
            Log.e("===================", "================");
            do {
                String title = cursor.getString(cursor.getColumnIndex("mingcheng"));
                mArrayAdapter.add(title);
                Log.e(title, "================");
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        mArrayAdapter.clear();
        FindyourCollect();
        mArrayAdapter.notifyDataSetChanged();
    }
}
