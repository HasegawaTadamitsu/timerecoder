
package jp.ddo.haselab.timerecoder;

import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 主処理Activity. main 画面の処理を行います。
 * 
 * @author T.Hasegawa
 */
public final class MainActivity extends
        Activity implements
        OnClickListener {

    private SQLiteDatabase mDb = null;

    /**
     * クリック時の処理. 各種ボタンの処理を行います。
     * 
     * @param v
     *            押されたview
     */
    @Override
    public void onClick(final View v) {

        int id = v.getId();
        if (id == R.id.button_start) {
            MyLog.getInstance().verbose("start button");
            Intent intent = new Intent(this, RecodeActivity.class);
            intent.putExtra(RecodeActivity.KEY_CATE, 0);
            startActivity(intent);
            return;
        }
        if (id == R.id.button_config) {
            MyLog.getInstance().verbose("config button");
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
            return;
        }
        if (id == R.id.button_quit) {
            MyLog.getInstance().verbose("quit button");
            finish();
            return;
        }
        return;
    }

    /**
     * create. 
     * 各種ボタンのイベント登録など行います。
     * 
     * @param savedInstanceState
     *            hmm
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyLog.getInstance().verbose("start");

        setContentView(R.layout.main);
        RecodeDateTime.setFormat(this);

        Button button;
        button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_config);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_quit);
        button.setOnClickListener(this);
    }

    /**
     * onDestroy DBのclose
     */
    @Override
    protected void onDestroy() {

        MyLog.getInstance().verbose("start");
        if (this.mDb != null) {
            MyLog.getInstance().verbose("close database");
            this.mDb.close();
        }
        super.onDestroy();
    }
}
