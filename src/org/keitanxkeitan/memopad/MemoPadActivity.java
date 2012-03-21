package org.keitanxkeitan.memopad;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MemoPadActivity extends ListActivity implements OnClickListener {

    MemoDbAdapter mMemoDbAdapter;

    private TextView mCurrentId;
    private EditText mEdit;
    private SimpleCursorAdapter mAdapter;

    // アクティビティの開始時にボタンを登録する
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCurrentId = (TextView)findViewById(R.id.view_id);
        mEdit = (EditText)findViewById(R.id.edit_memo);

        // ボタンを登録
        int buttons[] = {R.id.delete_button,
                R.id.modify_button,
                R.id.add_button
        };
        for (int id : buttons) {
            Button button = (Button)findViewById(id);
            button.setOnClickListener(this);
        }
        setEnabled(false);

        mMemoDbAdapter = new MemoDbAdapter(this);

        // データベースをオープンもしくは作成する
        mMemoDbAdapter.open();
    }

    // アクティビティがフォアグラウンドになったタイミングでデータを表示する
    @Override
    protected void onResume() {
        super.onResume();

        Cursor c = mMemoDbAdapter.fetchAllMemo();

        // ベースクラスにカーソルのライフサイクルを管理させる
        startManagingCursor(c);

        // データベースのカラムとリストビューを関連付ける
        String[] from = new String[] { BaseColumns._ID,
                MemoDbOpenHelper.KEY_MEMO };
        int[] to = new int[] { R.id._id, R.id.memo_text };
        mAdapter = new SimpleCursorAdapter(
                this, R.layout.memo_row, c, from, to);
        setListAdapter(mAdapter);
    }

    // アクティビティがフォアグラウンドでなくなったら、データベースをクローズする
    @Override
    protected void onPause() {
        super.onPause();
        mMemoDbAdapter.close();
    }

    public void onClick(View view) {
        String id = mCurrentId.getText().toString();
        String str = mEdit.getText().toString();
        if (view.getId() == R.id.delete_button) {
            mMemoDbAdapter.deleteMemo(id);
        } else if (view.getId() == R.id.modify_button) {
            if (str.length() != 0) {
                mMemoDbAdapter.setMemo(id, str);
            } else {
                mMemoDbAdapter.deleteMemo(id);
            }
        } else if (view.getId() == R.id.add_button) {
            if (str.length() != 0) {
                mMemoDbAdapter.addMemo(str);
            }
        } else {
            Log.e("MemoPadActivity", "Invalid button id");
        }

        // カーソルを再度取り出して、リストビューを再描画する
        mCurrentId.setText("");
        mEdit.setText("");
        setEnabled(false);
        Cursor c = mMemoDbAdapter.fetchAllMemo();
        startManagingCursor(c);
        mAdapter.changeCursor(c);
    }

    // リストがタップされた時の処理
    @Override
    protected void onListItemClick(
            ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // タップされた行のIDとメモの内容をビューに設定
        LinearLayout ll = (LinearLayout)v;
        TextView t = (TextView)ll.findViewById(R.id.memo_text);
        mEdit.setText(t.getText());
        mCurrentId.setText(Long.toString(id));
        setEnabled(true);
    }

    // 削除、変更ボタンの状態を変更する
    private void setEnabled(boolean enabled) {
        int buttons[] = {R.id.delete_button,
                R.id.modify_button,
        };
        for (int id : buttons) {
            Button button = (Button)findViewById(id);
            button.setEnabled(enabled);
        }
    }
}