
package jp.ddo.haselab.timerecoder.util;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Dialog Preference.
 * YES/NO dialog.
 * @author hasegawa
 *
 */
public final class DialogExPreference extends
        DialogPreference {

    /**
     * ButtonListener
     * @author hasegawa
     *
     */
    public static interface ButtonListener {

        /**
         * YES(Positive) button clicked called.
         */
        public void onPositiveClick();
    }

    private ButtonListener mButtonListener = null;

    /**
     * constractor.
     * @param argContext
     * @param argAttrs
     */
    public DialogExPreference(final Context argContext,
            final AttributeSet argAttrs) {

        super(argContext, argAttrs);
    }

    @Override
    public void onClick(final DialogInterface argInf,
            final int argWhich) {

        if (argWhich == DialogInterface.BUTTON_POSITIVE) {
            this.mButtonListener.onPositiveClick();
        }
    }

    /**
     * listener set.
     * @param argListener listener.
     */
    public void setButtonListener(final ButtonListener argListener) {

        this.mButtonListener = argListener;
    }
}
