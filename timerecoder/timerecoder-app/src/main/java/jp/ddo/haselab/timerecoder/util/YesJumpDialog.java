
package jp.ddo.haselab.timerecoder.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * @author hasegawa
 * 
 */
public final class YesJumpDialog {

    private Activity activity;

    private Class<Activity> toIntentClass;

    private int title_no;

    private int message_no;

    /**
     * @param arg0
     *            遷移元
     * @param arg1
     *            遷移先
     * @param arg2
     *            タイトル
     * @param arg3
     *            メッセージ
     */
    @SuppressWarnings("unchecked")
    public YesJumpDialog(final Activity arg0,
            final Class arg1,
            final int arg2,
            final int arg3) {

        this.activity = arg0;
        this.toIntentClass = arg1;
        this.title_no = arg2;
        this.message_no = arg3;
    }

    /**
     * ダイアログ. OKならば遷移先へ遷移します。
     */
    public void execute() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setTitle(this.title_no);
        builder.setMessage(this.message_no);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @SuppressWarnings("synthetic-access")
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {

                        Intent intent = new Intent(YesJumpDialog.this.activity,
                                YesJumpDialog.this.toIntentClass);
                        YesJumpDialog.this.activity.startActivity(intent);
                        YesJumpDialog.this.activity.finish();
                        return;
                    }
                });

        builder.setNegativeButton("No",
                null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }
}
