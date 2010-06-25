
package jp.ddo.haselab.timerecoder.util;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * My edit box.
 * 数値モードにした場合でも、文字列として、取得してから数値に直してください。
 * 
 * @author hasegawa
 *
 */
public final class EditTextExPreference extends
        EditTextPreference {

    private TextView         inputValueTextView;

    private static final int DEFAULT_MAX_LENGTH = 10;

    private String           unit               = "";

    private static final int FIRST_CALL_LAYOUT_ID = -1;

    private int              originalLayoutId   = FIRST_CALL_LAYOUT_ID;

    /**
     * set your xml config file.
     * 
     * <pre>
     *    jp.ddo.haselab.timerecoder.util.EditTextExPreference
     *     inputType="digits"  or nothing
     *     unit=" second "
     *     maxLength="2"
     *     android:key="audio_recode_time"
     *     android:title="@string/audio_recode_time_title"
     *     android:summary="@string/audio_recode_time_summary"
     * </pre>
     * @param argContext context
     * @param argAttrs  attrs
     */
    public EditTextExPreference(final Context argContext,
            final AttributeSet argAttrs) {

        super(argContext, argAttrs);

        this.unit = argAttrs.getAttributeValue(null, "unit");

        EditText et = getEditText();

        String argInpType = argAttrs.getAttributeValue(null, "inputType");

        int inputType;
        if (argInpType == null) {
            inputType = InputType.TYPE_NULL;
        } else if (argInpType.equals("digits")) {
            inputType = InputType.TYPE_CLASS_NUMBER;
        } else {
            throw new IllegalArgumentException("unknown attr in xml file.argInputType[" + argInpType
                    + "]");
        }
        et.setInputType(inputType);

        int maxLength = argAttrs.getAttributeIntValue(null,
                "maxLength",
                DEFAULT_MAX_LENGTH);
        et.setFilters(new InputFilter[] {
            new InputFilter.LengthFilter(maxLength)
        });
    }

    @Override
    protected void onBindView(View view) {

        setInputValueTextView();
        super.onBindView(view);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {

        Context ct = super.getContext();

        LinearLayout baseLayout = new LinearLayout(ct);
        baseLayout.setOrientation(LinearLayout.HORIZONTAL);
        baseLayout.setGravity(Gravity.CENTER_VERTICAL);

        final LayoutInflater layoutInflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (this.originalLayoutId == FIRST_CALL_LAYOUT_ID) {
            this.originalLayoutId = getLayoutResource();
        }
        final View orgLayout = layoutInflater.inflate(this.originalLayoutId,
                null);

        this.inputValueTextView = new TextView(ct);
        this.inputValueTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        baseLayout.addView(orgLayout,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        baseLayout.addView(this.inputValueTextView,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        setLayoutResource(android.R.id.widget_frame);
        return baseLayout;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        super.onDialogClosed(positiveResult);
        setInputValueTextView();
    }

    private void setInputValueTextView() {

        String val = super.getText();
        if (val == null || val.equals("")) {
            this.inputValueTextView.setText("");
            return;
        }
        this.inputValueTextView.setText(val + this.unit + " ");
    }
}
