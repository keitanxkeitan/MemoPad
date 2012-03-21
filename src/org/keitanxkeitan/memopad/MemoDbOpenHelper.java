package org.keitanxkeitan.memopad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemoDbOpenHelper extends SQLiteOpenHelper {

    // データベースのファイル名
    public static final String DB_NAME = "daily_memo.db";
    
    // テーブルの名前
    public static final String DB_TABLE = "daily_memo";
    
    // データベースのバージョン
    public static final int DB_VERSION = 1;
    
    // カラムの名前とインデックス
    public static final String KEY_ID = "_id";
    public static final int ID_COLUMN = 0;

    public static final String KEY_MEMO = "memo";
    public static final int MEMO_COLUMN = 1;
    
    public MemoDbOpenHelper(Context c) {
        // データベースのファイル名とバージョンを指定
        super(c, DB_NAME, null, DB_VERSION);
    }
    
    // テーブル作成のSQL文
    static final String CREATE_TABLE = "CREATE TABLE " +
            DB_TABLE + " (" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_MEMO + " TEXT NOT NULL);";
    
    // データベースを新規に作成した後呼ばれる
    public void onCreate(SQLiteDatabase db) {
        // 内部にテーブルを作成する
        db.execSQL(CREATE_TABLE);
    }
    
    // 存在するデータベースと定義しているバージョンが異なるとき
    public void onUpgrade(SQLiteDatabase db,
            int oldVersion, int newVersion) {
        Log.w("MemoDbAdapter",
                "Version mismatch :" + oldVersion +
                " to " + newVersion);
        // ここでは、テーブルを削除して新規に作成している
        // 通常は、テーブル内のデータの変換を行う
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
    
}
