package jp.ddo.haselab.timerecoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;
import jp.ddo.haselab.timerecoder.dataaccess.Recode;
import jp.ddo.haselab.timerecoder.dataaccess.RecodeDao;
import jp.ddo.haselab.timerecoder.util.RecodeDateTime;

/**
 * 主処理(Recode)Activity.
 * recode 画面の処理を行います。
 *
 * @author T.Hasegawa
 */
public final class RecodeActivity extends Activity implements OnClickListener {

    public static final String  KEY_CATE = "CATEGORY_KEY";

    private static final String LOG_TAG = "RecodeActivity";

    private SQLiteDatabase mDb = null;

    /**
     * create.
     * 各種ボタンのイベント登録などを行います。
     * @param savedInstanceState hmm
     */
    @Override
        protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	Log.v(LOG_TAG,"start onCreate");
        setContentView(R.layout.recode);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
	mDb = dbHelper.getWritableDatabase();

        Button button;
        button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_end);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_etc);
        button.setOnClickListener(this);
     }

    /**
     * onDestroy
     * DBのclose
     */
    @Override
	protected void onDestroy(){
	Log.v(LOG_TAG,"start onDestory");
	if(mDb != null) {
	    Log.v(LOG_TAG,"close db");
	    mDb.close();
	}
	super.onDestroy();
    }

    /**
     * Menuの作成.
     * Menuの作成します.
     * @param menu メニュー
     */
    @Override
        public boolean onCreateOptionsMenu(final Menu menu) {
                super.onCreateOptionsMenu(menu);

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.recode, menu);
                return true;
        }


    /**
     * 終了ダイアログの作成／処理.
     * 終了ダイアログの作成／処理します。
     * OKならばゲーム終了させます。
     */
    private void dialogQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_quit_sure_title);
        builder.setMessage(R.string.dialog_quit_sure_msg);
	final Activity ac = this;
        builder.setPositiveButton("OK",
	  new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog,
                                  final int whichButton) {
		  Intent intent = new Intent(ac, MainActivity.class);
		  intent.putExtra(RecodeActivity.KEY_CATE, 0);
		  startActivity(intent);
		  finish();
		  return;
              }
          }
                                  );

        builder.setNegativeButton("No", null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    private void dialogClear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_clear_sure_title);
        builder.setMessage(R.string.dialog_clear_sure_msg);
        builder.setPositiveButton("OK",
          new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog,
                                  final int whichButton) {
                finish();
                return;
              }
          }
                                  );

        builder.setNegativeButton("No", null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    /**
     * menu処理の分岐.
     * menu押下時の処理です。終了・シャッフルなどあります。
     * @param item 押下されたitem
     */
    @Override
        public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_quit:
            dialogQuit();
            return true;
        case R.id.menu_clear:
            dialogClear();
            return true;
        default:
            return false;
        }
    }

    /**
     * クリック時の処理.
     * 各種ボタンの処理を行います。
     * @param v 押されたview
     */
    @Override
        public void onClick(final View v) {
        int id = v.getId();

	EditText editText = (EditText) findViewById(R.id.edittext_memo);
	String memo = editText.getText().toString();


	if (id == R.id.button_start) {
	    Log.v(LOG_TAG,"button_start");

	    Recode rec = new Recode(new RecodeDateTime(),1,memo);
	    RecodeDao dao = new RecodeDao(mDb);
	    
	    mDb.beginTransaction();
	    long key = 0;
	    try {
		key = dao.insert(rec);
		mDb.setTransactionSuccessful();
	    } finally {
		mDb.endTransaction();
		Log.v(LOG_TAG,"commit key=" + key);
	    }
	    long co = dao.count();
	    Log.v(LOG_TAG,"count =" + co);
            return;
        }
        return;
    }
}
