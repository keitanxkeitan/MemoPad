package org.keitanxkeitan.memopad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemoDbOpenHelper extends SQLiteOpenHelper {

    // �f�[�^�x�[�X�̃t�@�C����
    public static final String DB_NAME = "daily_memo.db";
    
    // �e�[�u���̖��O
    public static final String DB_TABLE = "daily_memo";
    
    // �f�[�^�x�[�X�̃o�[�W����
    public static final int DB_VERSION = 1;
    
    // �J�����̖��O�ƃC���f�b�N�X
    public static final String KEY_ID = "_id";
    public static final int ID_COLUMN = 0;

    public static final String KEY_MEMO = "memo";
    public static final int MEMO_COLUMN = 1;
    
    public MemoDbOpenHelper(Context c) {
        // �f�[�^�x�[�X�̃t�@�C�����ƃo�[�W�������w��
        super(c, DB_NAME, null, DB_VERSION);
    }
    
    // �e�[�u���쐬��SQL��
    static final String CREATE_TABLE = "CREATE TABLE " +
            DB_TABLE + " (" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_MEMO + " TEXT NOT NULL);";
    
    // �f�[�^�x�[�X��V�K�ɍ쐬������Ă΂��
    public void onCreate(SQLiteDatabase db) {
        // �����Ƀe�[�u�����쐬����
        db.execSQL(CREATE_TABLE);
    }
    
    // ���݂���f�[�^�x�[�X�ƒ�`���Ă���o�[�W�������قȂ�Ƃ�
    public void onUpgrade(SQLiteDatabase db,
            int oldVersion, int newVersion) {
        Log.w("MemoDbAdapter",
                "Version mismatch :" + oldVersion +
                " to " + newVersion);
        // �����ł́A�e�[�u�����폜���ĐV�K�ɍ쐬���Ă���
        // �ʏ�́A�e�[�u�����̃f�[�^�̕ϊ����s��
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
    
}
