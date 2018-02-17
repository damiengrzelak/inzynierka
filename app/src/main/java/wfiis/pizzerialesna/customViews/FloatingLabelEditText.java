package wfiis.pizzerialesna.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Screen;
import com.inverce.mod.core.verification.Conditions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.tools.UI;
import wfiis.pizzerialesna.tools.Util;

public class FloatingLabelEditText extends LinearLayout implements View.OnFocusChangeListener {

    private TextInputLayout view;
    private TextInputEditText edit;
    private RelativeLayout root;

    private int red;
    private int gray;
    private int black;

    private boolean valid = false;
    private Validator validator;
    private ValidChangeListener validChangeListener;

    public FloatingLabelEditText(Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public FloatingLabelEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1, -1);
    }

    public FloatingLabelEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingLabelEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        inflate(context, R.layout.floating_label_edittext, this);
        UI.hideSoftInputForce();
        findViews();
        setResources();
        setValidator(null, Conditions::notNullOrEmpty);
        setListeners();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FloatingLabelEditText, 0, 0);
        try {
            setHintText(a.getString(R.styleable.FloatingLabelEditText_hint_text));
            setInputText(a.getString(R.styleable.FloatingLabelEditText_input_text));
            lockInput(a.getBoolean(R.styleable.FloatingLabelEditText_lock_text, false));
            edit.setInputType(a.getInt(R.styleable.FloatingLabelEditText_android_inputType, EditorInfo.TYPE_CLASS_TEXT));
            edit.setImeOptions(a.getInt(R.styleable.FloatingLabelEditText_android_imeOptions, EditorInfo.TYPE_NULL));
            if (!Util.nullOrEmpty(a.getString(R.styleable.InputEditTextView_android_digits))) {
                edit.setKeyListener(DigitsKeyListener.getInstance(a.getString(R.styleable.InputEditTextView_android_digits)));
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.floating_label_edit_text_view_edit) {
            if (hasFocus) {
                validate(true);
                view.setHintEnabled(true);
                edit.setPadding(0, Screen.dpToPx(6), 0, Screen.dpToPx(6));
                UI.showSoftInput(v, true);
            } else {
                if (Util.nullOrEmpty(edit.getText().toString())) {
                    edit.setPadding(0, Screen.dpToPx(6), 0, 0);
                    IM.onUi().schedule(() -> view.setHintEnabled(false), 100, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    private void findViews() {
        view = (TextInputLayout) findViewById(R.id.floating_label_edit_text_view_layout);
        edit = (TextInputEditText) findViewById(R.id.floating_label_edit_text_view_edit);
        root = (RelativeLayout) findViewById(R.id.floating_label_edit_text_view_root);
    }

    private void setResources() {
        red = ResourcesCompat.getColor(getResources(), R.color.red, null);
        gray = ResourcesCompat.getColor(getResources(), R.color.gray_light, null);
        black = ResourcesCompat.getColor(getResources(), R.color.black, null);
    }

    private void setListeners() {
        edit.setOnFocusChangeListener(this);
        root.setOnClickListener(v -> edit.requestFocus());

    }

    public void setPasswordInputType() {
        if (edit.getTransformationMethod() != null) {
            edit.setTransformationMethod(null);
            edit.setSelection(edit.getText().length());
        } else {
            edit.setTransformationMethod(new PasswordTransformationMethod());
            edit.setSelection(edit.getText().length());
        }
    }


    public void validate(boolean isValid) {
        valid = isValid;
        view.setHintTextAppearance(isValid ? R.style.FloatingLabel : R.style.FloatingLabelError);
        edit.setTextColor(isValid ? black : red);
        edit.setHintTextColor(isValid ? gray : red);
    }

    public void setText(String text) {
        if (!Util.nullOrEmpty(text)) {
            view.setHintEnabled(true);
            edit.setText(text);
        } else {
            IM.onUi().schedule(() -> view.setHintEnabled(false), 80, TimeUnit.MILLISECONDS);
        }
    }

    private void setInputText(String text) {
        if (Conditions.notNullOrEmpty(text)) {
            view.setHintEnabled(true);
            edit.setText(text);
        }
    }

    private void lockInput(boolean lockText) {
        if (lockText) {
            edit.setEnabled(false);
            edit.setInputType(InputType.TYPE_NULL);
            edit.setFocusable(false);
            edit.setClickable(false);
        }
    }

    public String getText() {
        return edit.getText().toString().trim();
    }

    private void setHintText(String string) {
        edit.setHint(string);
    }

    public void setValidator(ValidChangeListener validChangeListener, Validator validator) {
        this.validChangeListener = validChangeListener;
        this.validator = validator;

        checkValid();
    }

    public boolean isValid() {
        validate(validator.isValid(getText()));
        return valid;
    }

    public void checkValid() {
        boolean newValid;
        newValid = validator != null && validator.isValid(getText());

        if (newValid == valid) {
            return;
        } else {
            valid = newValid;
        }

        if (validChangeListener != null) {
            validChangeListener.onValidChanged(this, valid);
        }
    }

    public EditText getEditText() {
        return edit;
    }

    public void setInputFilters(ArrayList<InputFilter> filters) {
        if (filters == null) {
            return;
        }
        InputFilter[] inputFilters = new InputFilter[filters.size()];
        inputFilters = filters.toArray(inputFilters);
        edit.setFilters(inputFilters);
    }

    //Validator-------------------------------------------------------------------------------------------------
    public interface Validator {
        boolean isValid(String value);
    }

    //Validator-------------------------------------------------------------------------------------------------
    public interface ValidChangeListener {
        void onValidChanged(FloatingLabelEditText view, boolean isValid);
    }
}

