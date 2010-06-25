package jp.ddo.haselab.timerecoder;

import java.util.List;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;
import jp.ddo.haselab.timerecoder.dataaccess.Recode;
import jp.ddo.haselab.timerecoder.dataaccess.RecodeDao;
import jp.ddo.haselab.timerecoder.dataaccess.RecodeLocation;
import jp.ddo.haselab.timerecoder.util.RecodeAudioMgr;
import jp.ddo.haselab.timerecoder.util.RecodeLocationMgr;
import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import jp.ddo.haselab.timerecoder.util.MyLog;

/**
 * 主処理(Recode)Activity. recode 画面の処理を行います。
 * 
 * @author T.Hasegawa
 */
public final class RecodeActivity extends Activity implements OnClickListener {

	public static final String KEY_CATE = "CATEGORY_KEY";

	private int categoryId = 0;

	private SQLiteDatabase mDb = null;

	private RecodeListAdapter listAdapter;

	private RecodeAudioMgr recodeAudioMgr;
	private RecodeLocationMgr recodeLocationMgr;

	private int defaultRecodeTime; // second time

	private void initWidget() {
		Button button;
		button = (Button) findViewById(R.id.button_clear);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.button_start);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.button_end);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.button_etc);
		button.setOnClickListener(this);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		boolean useAudio = preferences.getBoolean("recode_use_audio", false);
		CheckBox checkBox;
		checkBox = (CheckBox) findViewById(R.id.checkbox_use_audio);
		checkBox.setChecked(useAudio);
		boolean useLocation = preferences.getBoolean("recode_use_location",
				false);
		checkBox = (CheckBox) findViewById(R.id.checkbox_use_location);
		checkBox.setChecked(useLocation);

		recodeAudioMgr = new RecodeAudioMgr();

		defaultRecodeTime = Integer.valueOf(preferences.getString(
				"audio_recode_time", "10"));

		int localeTimeout = Integer.valueOf(preferences.getString(
				"locale_timeout", "1"));

		recodeLocationMgr = new RecodeLocationMgr(this, localeTimeout);

		initListView();
	}

	/**
	 * create. 各種ボタンのイベント登録などを行います。
	 * 
	 * @param savedInstanceState
	 *            hmm
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyLog.getInstance().verbose("start");
		setContentView(R.layout.recode);

		DatabaseHelper dbHelper = new DatabaseHelper(this);
		mDb = dbHelper.getWritableDatabase();

		initWidget();
	}

	/**
	 * onDestroy DBのclose
	 */
	@Override
	protected void onDestroy() {
		MyLog.getInstance().verbose("start");
		if (mDb != null) {
			MyLog.getInstance().verbose("close db");
			mDb.close();
		}
		super.onDestroy();
	}

	/**
	 * Menuの作成. Menuの作成します.
	 * 
	 * @param menu
	 *            メニュー
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recode, menu);
		return true;
	}

	/**
	 * 終了ダイアログの作成／処理. 終了ダイアログの作成／処理します。 OKならばゲーム終了させます。
	 */
	private void dialogQuit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_quit_sure_title);
		builder.setMessage(R.string.dialog_quit_sure_msg);
		final Activity ac = this;
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog,
					final int whichButton) {
				Intent intent = new Intent(ac, MainActivity.class);
				intent.putExtra(RecodeActivity.KEY_CATE, 0);
				startActivity(intent);
				finish();
				return;
			}
		});

		builder.setNegativeButton("No", null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return;
	}

	private int deleteByCategoryId() {
		RecodeDao dao = new RecodeDao(mDb);
		mDb.beginTransaction();
		MyLog.getInstance().startTransaction("category[" + categoryId + "]");
		int res = 0;
		try {
			res = dao.deleteByCategoryId(categoryId);
			mDb.setTransactionSuccessful();
			MyLog.getInstance().endTransaction(
					"success.delete count[" + res + "]");
		} finally {
			mDb.endTransaction();
		}
		return res;
	}

	private void dialogClear() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_clear_sure_title);
		builder.setMessage(R.string.dialog_clear_sure_msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog,
					final int whichButton) {
				deleteByCategoryId();
				initListView();
				return;
			}
		});

		builder.setNegativeButton("No", null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		return;
	}

	/**
	 * menu処理の分岐. menu押下時の処理です。終了・シャッフルなどあります。
	 * 
	 * @param item
	 *            押下されたitem
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

	private Recode insertTransaction(final Recode rec) {
		RecodeDao dao = new RecodeDao(mDb);

		mDb.beginTransaction();
		MyLog.getInstance().startTransaction("recode[" + rec + "]");

		long key = 0;
		try {
			key = dao.insert(rec);
			mDb.setTransactionSuccessful();
			MyLog.getInstance().endTransaction("success.recode[" + rec + "]");
		} finally {
			mDb.endTransaction();
		}
		return rec;
	}

	private void initListView() {
		RecodeDao dao = new RecodeDao(mDb);
		List<Recode> data = dao.findByCategoryId(categoryId);
		listAdapter = new RecodeListAdapter(this, data);
		ListView list = (ListView) findViewById(R.id.listview_data);
		list.setAdapter(listAdapter);
		list.setSelection(list.getCount());

		View emptyView = findViewById(R.id.listview_empty);
		list.setEmptyView(emptyView);

	}

	private void appendData(final Recode rec) {
		listAdapter.addData(rec);
		ListView list = (ListView) findViewById(R.id.listview_data);
		list.setSelection(list.getCount());
	}

	private void doRecodeAudio(final RecodeDateTime argRecTime) {
		String fileName = "rec" + argRecTime.toYYYYMMDDHHMMSS() + ".3gp";
		try {
			recodeAudioMgr.startRecodingExternalStrage(fileName,
					defaultRecodeTime);
		} catch (IOException e) {
			MyLog.getInstance().error(
					"recode error.IOException." + "fileName[" + fileName + "]",
					e);
			Toast.makeText(this, R.string.toast_recode_audio_error_msg,
					Toast.LENGTH_SHORT).show();
		}
	}

	private RecodeLocation doRecodeLocation() {
		RecodeLocation location = recodeLocationMgr.getRecodeLocation();
		if (location == null) {
			MyLog.getInstance().error("recode location error");
			Toast.makeText(this, R.string.toast_recode_location_error_msg,
					Toast.LENGTH_SHORT).show();
		}
		return location;
	}

	/**
	 * クリック時の処理. 各種ボタンの処理を行います。
	 * 
	 * @param v
	 *            押されたview
	 */
	@Override
	public void onClick(final View v) {
		int id = v.getId();

		if (id == R.id.button_clear) {
			EditText editText = (EditText) findViewById(R.id.edittext_memo);
			editText.setText("");
			return;

		}
		EditText editText = (EditText) findViewById(R.id.edittext_memo);
		String memo = editText.getText().toString();
		RecodeDateTime dateTime = new RecodeDateTime();

		RecodeLocation location = null;

		CheckBox recodeLocationCheckBox;
		recodeLocationCheckBox = (CheckBox) findViewById(R.id.checkbox_use_location);
		if (recodeLocationCheckBox.isChecked()) {
			location = doRecodeLocation();
		}

		Recode rec;
		switch (id) {
		case R.id.button_start:
			rec = new Recode(categoryId, dateTime, Recode.EventId.START, memo,
					location);
			break;
		case R.id.button_end:
			rec = new Recode(categoryId, dateTime, Recode.EventId.END, memo,
					location);
			break;
		case R.id.button_etc:
			rec = new Recode(categoryId, dateTime, Recode.EventId.ETC, memo,
					location);
			break;
		default:
			throw new IllegalArgumentException("unknown button(view).id[" + id
					+ "]");
		}
		insertTransaction(rec);
		appendData(rec);

		CheckBox recodeAudioCheckBox;
		recodeAudioCheckBox = (CheckBox) findViewById(R.id.checkbox_use_audio);
		if (recodeAudioCheckBox.isChecked()) {
			doRecodeAudio(dateTime);
		}

		return;
	}
}
