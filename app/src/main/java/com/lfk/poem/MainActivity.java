package com.lfk.poem;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {
    private ShareActionProvider mShareActionProvider;
    private  final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "poem_all.db"; //保存的数据库文件名
    //public static final String DB_USER_NAME = "poem_user.db";
    public static final String PACKAGE_NAME = "com.lfk.poem";// 应用的包名
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() +"/"
            + PACKAGE_NAME+ "/databases"; // 在手机里存放数据库的位置
    private SwipeRefreshLayout swipeLayout;
    private RelativeLayout main_layout;
    private TextView textView;
    private static int ID = 0;
    private String poem;
    private Intent sendIntent;
    //private String NAME;
    //private String POEM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.type(Logger.MODEL_LAUNCHER);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/font_ksj.ttf");
        textView = (TextView)findViewById(R.id.text_view);
        textView.setTypeface(typeface);

        main_layout = (RelativeLayout)findViewById(R.id.main_layout);
        //ChangeBackground();
        FindaPoem();

        //setmShareActionProvider();

        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
        swipeLayout.setColorScheme(R.color.haah);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//延迟跳转=-=
                    public void run() {
                        swipeLayout.setRefreshing(true);
                        FindaPoem();
                        swipeLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }
    private void FindaPoem() {
        int ll = (int) (1 + Math.random() * (59170));
        ID = ll;
        SQLiteDatabase database = openDatabase();
        Cursor cursor = database.rawQuery("Select * From poem Where _id = " + ll, null);
        cursor.moveToFirst();
        poem = cursor.getString(1)+"\n"+"\n"+cursor.getString(2)+"\n"+"\n"+cursor.getString(13);
        //NAME = cursor.getString(2)+": "+cursor.getString(1);
        //POEM = cursor.getString(13);
        Log.e(poem, "================");
        textView.setText(poem);
        cursor.close();
        database.close();
    }
//    private void ChangeBackground(){
//        int ln = (int) (1 + Math.random() * (3));
//        switch (ln){
//            case 1:
//                main_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_1));
//                break;
//            case 2:
//                main_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_2));
//                break;
//            case 3:
//                main_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_3));
//                break;
//        }
//    }
    public SQLiteDatabase openDatabase() {
        try {
            File myDataPath = new File(DB_PATH);
            if (!myDataPath.exists())
            {
                myDataPath.mkdirs();// 如果没有这个目录,则创建
            }
            String dbfile = myDataPath+"/"+DB_NAME;
            if (!(new File(dbfile).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is;
                is = this.getResources().openRawResource(R.raw.poem_all); // 欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0 ;
                while ((count = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            Log.e("=======================","get it  ======================");
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
    void AddaPoemToCollect(){
        //File myDataPath = new File(DB_PATH);
        //String dbfile = myDataPath+"/"+DB_USER_NAME;
        SQLiteDatabase db = openDatabase();
        //ContentValues contentValues = new ContentValues();
        db.execSQL("UPDATE poem SET ticai = 1 WHERE _id ="+ID);
        //db.insert("book", null, contentValues);
        db.close();
        Toast.makeText(getApplicationContext(),
                "Collect succeed",
                Toast.LENGTH_SHORT).show();
        //ID++;
    }
    void deleteAPoemFromCollect(){
        SQLiteDatabase db = openDatabase();

        db.execSQL("UPDATE poem SET ticai = 0 WHERE _id ="+ID);

        db.close();

        Toast.makeText(getApplicationContext(),
                "Collect Delete",
                Toast.LENGTH_SHORT).show();
    }
    private void setmShareActionProvider(){
        sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, poem);
//        sendIntent.setType("text/plain");
//        setShareIntent(sendIntent);
        sendIntent.putExtra(Intent.EXTRA_STREAM, shot());
        sendIntent.setType("image/jpeg");
        setShareIntent(sendIntent);
    }
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    private Uri shot() {
        View view = getWindow().getDecorView();
        Display display = this.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        view.setDrawingCacheEnabled(true);//允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap

        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = this.getWindowManager().getDefaultDisplay().getWidth();
        int height = this.getWindowManager().getDefaultDisplay()
                .getHeight();
        TypedArray actionbarSizeTypedArray = this.obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize});
        int h = (int)actionbarSizeTypedArray.getDimension(0, 0);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(),0, statusBarHeight+h, width, height
                - statusBarHeight-h);
        File f = new File(Environment.getExternalStorageDirectory(),
                "output_image.jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            assert fOut != null;
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(f);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        MenuItem menuItem = menu.findItem(R.id.like);
        MenuItem item1 = menu.findItem(R.id.dislike);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.collect:
                Intent intent = new Intent(this,Collect.class);
                startActivity(intent);
                break;
            case R.id.like:
                AddaPoemToCollect();
                break;
            case R.id.dislike:
                deleteAPoemFromCollect();
                break;
            case R.id.menu_item_share:
                setmShareActionProvider();
                startActivity(Intent.createChooser(sendIntent,"图片"));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
