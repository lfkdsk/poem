package com.lfk.poem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class Collect_item extends Activity {
    private DBhelper dBhelper;
    private String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_item);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        //System.out.println(id+"=================");
        TextView textView = (TextView)findViewById(R.id.poem_item);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/font_ksj.ttf");
        textView.setTypeface(typeface);
        dBhelper = new DBhelper(this,"poem_all.db",null,1);
        SQLiteDatabase database = dBhelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * From poem where mingcheng="+"\""+title+"\"", null);
        cursor.moveToFirst();
        ID = cursor.getString(cursor.getColumnIndex("_id"));
        String poem = cursor.getString(1)+"\n"+"\n"+cursor.getString(2)+"\n"+"\n"+cursor.getString(13);
        textView.setText(poem);
        Log.e("===================", "================");
        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collect_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.dislike_collect){
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            db.execSQL("UPDATE poem SET ticai = 0 WHERE _id ="+ID);
            db.close();
            Toast.makeText(getApplicationContext(),
                    "Collect Delete",
                    Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
