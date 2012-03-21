package org.keitanxkeitan.memopad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;

public class MemoDbAdapter {

    private SQLiteDatabase mDb;
    private MemoDbOpenHelper mDbHelper;

    public MemoDbAdapter(Context context) {
        mDbHelper = new MemoDbOpenHelper(context);
    }

    public void close() {
        mDb.close();
    }

    public void open() throws SQLiteException {
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            mDb = mDbHelper.getReadableDatabase();
        }
    }

    // すべての行のカーソルをIDの降順で取り出す
    public Cursor fetchAllMemo() {
        return mDb.query(MemoDbOpenHelper.DB_TABLE,
                new String[] {BaseColumns._ID, MemoDbOpenHelper.KEY_MEMO},
                null, null, null, null, MemoDbOpenHelper.KEY_ID + " DESC");
    }

    // 指定したメモを追加してIDを返す
    public String addMemo(String memo) {
        ContentValues values = new ContentValues();
        values.put(MemoDbOpenHelper.KEY_MEMO, memo);
        long id = mDb.insert(MemoDbOpenHelper.DB_TABLE, null, values);
        if (id < 0) {
            return "";
        }
        return Long.toString(id);
    }

    // 指定したIDの行を削除する
    public void deleteMemo(String id) {
        mDb.delete(MemoDbOpenHelper.DB_TABLE, MemoDbOpenHelper.KEY_ID + " = ?",
                new String[] { id });
    }

    // 指定したIDの行のメモを更新する
    public void setMemo(String id, String memo) {
        // 更新する値を設定する
        ContentValues values = new ContentValues();
        values.put(MemoDbOpenHelper.KEY_MEMO, memo);

        // 行を更新する
        mDb.update(MemoDbOpenHelper.DB_TABLE, values,
                MemoDbOpenHelper.KEY_ID + " = ?", new String[] { id });
    }

}