package jp.ddo.haselab.timerecoder.util;

import android.preference.DialogPreference;
import android.content.Context;
import android.util.AttributeSet;
import android.content.DialogInterface;

public final class DialogExPreference
        extends DialogPreference {
    public static interface ButtonListener {
        public void onPositiveClick();
    }

    private ButtonListener mButtonListener = null;

    public DialogExPreference(final Context argContext,
            final AttributeSet argAttrs) {
        super(argContext, argAttrs);
    }

    public void setButtonListener(final ButtonListener argListener) {
        mButtonListener = argListener;
    }

    @Override
    public void onClick(final DialogInterface argInf, final int argWhich) {
        if (argWhich == DialogInterface.BUTTON_POSITIVE) {
            mButtonListener.onPositiveClick();
        }
    }
}
