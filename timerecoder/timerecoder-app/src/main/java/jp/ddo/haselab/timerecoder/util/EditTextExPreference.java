package jp.ddo.haselab.timerecoder.util;

import android.app.Service;
import android.preference.EditTextPreference;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.content.res.TypedArray;
import java.lang.CharSequence;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;


public final class EditTextExPreference extends EditTextPreference {

    private final CharSequence orgSummary;

    private AccessibilityManager mAccessibilityManager;

    public EditTextExPreference(final Context argContext,
				final AttributeSet argAttrs) {

	super(argContext, argAttrs);
	
	orgSummary = getSummary();
	
	String argInpType = argAttrs.getAttributeValue(null, "inputType");
	if (argInpType ==  null) {
	    return;
	}
	int inputType = EditorInfo.TYPE_NULL;

	if (argInpType.equals("digits")) {
	    inputType =InputType.TYPE_CLASS_NUMBER;
	}else{
	    throw new IllegalArgumentException(
	   "unknown attr in xml file.argInputType[" + argInpType + "]");
	}

	EditText et = getEditText();
	et.setInputType(inputType);
   }

    @Override
	protected void onBindView(View view) {

	String val = super.getText();
	String val2= orgSummary +" "+ val;
	setSummary((CharSequence) val2);
	
	// need after do any thning. why?
        super.onBindView(view);
    }

    @Override
	protected void onDialogClosed(boolean positiveResult) {
	super.onDialogClosed(positiveResult);

	String value = getEditText().getText().toString();
	MyLog.getInstance().verbose("onDialogClosed"+ value);
	setSummary((CharSequence) (orgSummary +" "+ value));
	notifyChanged();
    }

}
