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

    // ���ׂĂ̍s�̃J�[�\����ID�̍~���Ŏ��o��
    public Cursor fetchAllMemo() {
        return mDb.query(MemoDbOpenHelper.DB_TABLE,
                new String[] {BaseColumns._ID, MemoDbOpenHelper.KEY_MEMO},
                null, null, null, null, MemoDbOpenHelper.KEY_ID + " DESC");
    }

    // �w�肵��������ǉ�����ID��Ԃ�
    public String addMemo(String memo) {
        ContentValues values = new ContentValues();
        values.put(MemoDbOpenHelper.KEY_MEMO, memo);
        long id = mDb.insert(MemoDbOpenHelper.DB_TABLE, null, values);
        if (id < 0) {
            return "";
        }
        return Long.toString(id);
    }

    // �w�肵��ID�̍s���폜����
    public void deleteMemo(String id) {
        mDb.delete(MemoDbOpenHelper.DB_TABLE, MemoDbOpenHelper.KEY_ID + " = ?",
                new String[] { id });
    }

    // �w�肵��ID�̍s�̃������X�V����
    public void setMemo(String id, String memo) {
        // �X�V����l��ݒ肷��
        ContentValues values = new ContentValues();
        values.put(MemoDbOpenHelper.KEY_MEMO, memo);

        // �s���X�V����
        mDb.update(MemoDbOpenHelper.DB_TABLE, values,
                MemoDbOpenHelper.KEY_ID + " = ?", new String[] { id });
    }

}