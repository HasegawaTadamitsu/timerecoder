
package jp.ddo.haselab.timerecoder.util;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public final class DialogExPreference extends
        DialogPreference {

    public static interface ButtonListener {

        public void onPositiveClick();
    }

    private ButtonListener mButtonListener = null;

    public DialogExPreference(final Context argContext,
            final AttributeSet argAttrs) {

        super(argContext, argAttrs);
    }

    @Override
    public void onClick(final DialogInterface argInf,
            final int argWhich) {

        if (argWhich == DialogInterface.BUTTON_POSITIVE) {
            mButtonListener.onPositiveClick();
        }
    }

    public void setButtonListener(final ButtonListener argListener) {

        mButtonListener = argListener;
    }
}
