package wfiis.pizzerialesna.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Screen;

import java.util.concurrent.TimeUnit;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.tools.UI;
import wfiis.pizzerialesna.tools.Util;

public class InputEditTextView extends LinearLayout implements View.OnFocusChangeListener, TextWatcher {

    private static final String EDIT = "edit";
    private String ID_TAG;

    // PADDING ARRAYS - { LEFT, TOP, RIGHT, BOTTOM }
    private static final int[] onClickedPadding = {3, 4, 0, 8};
    private static final int[] notClickedPadding = {3, 6, 0, 17};

    private TextInputLayout view;
    private TextInputEditText edit;

    private ImageView eye;
    private int redBorder;
    private int brownBorder;
    private int red;
    private int black;
    private int gray;

    private boolean error;

    private String longHint;
    private String shortHint;
    private boolean hasShortHint;

    @IdRes
    private int nextViewId;

    public InputEditTextView(Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public InputEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1, -1);
    }

    public InputEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InputEditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString(ID_TAG, edit.getText().toString());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            edit.setText(bundle.getString(ID_TAG));
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        inflate(context, R.layout.input_edit_text_view, this);
        findViews();
        setIdTag(getResources().getResourceEntryName(this.getId()));
        setResources();
        setListeners();
        setError(false);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.InputEditTextView, 0, 0);
        try {
            setHintText(a.getString(R.styleable.InputEditTextView_hint));
            edit.setInputType(a.getInt(R.styleable.InputEditTextView_android_inputType, EditorInfo.TYPE_CLASS_TEXT));
            edit.setImeOptions(a.getInt(R.styleable.InputEditTextView_android_imeOptions, EditorInfo.TYPE_NULL));
            if (!Util.nullOrEmpty(a.getString(R.styleable.InputEditTextView_android_digits))) {
                edit.setKeyListener(DigitsKeyListener.getInstance(a.getString(R.styleable.InputEditTextView_android_digits)));
            }
            nextViewId = a.getResourceId(R.styleable.InputEditTextView_nextInputFocusId, -1);
            setShowPasswordVisibility(a.getBoolean(R.styleable.InputEditTextView_password, false));
            Typeface tf = Typeface.createFromAsset(IM.context().getAssets(), "fonts/NunitoSans-Regular.ttf");
            edit.setTypeface(tf);

        } finally {
            a.recycle();
        }
        if (nextViewId != -1) {
            setKeyboardListener();
        }
        UI.hideSoftInputForce();
    }

    private void setKeyboardListener() {
        edit.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                View parent = (View) this.getParent();
                if (parent != null && parent.getParent() != null) {
                    parent = (View) parent.getParent();
                }
                if (parent != null) {
                    View v = parent.findViewById(nextViewId);
                    if (v != null) {
                        v.requestFocus();
                    }
                }
                return true;
            }
            return false;
        });
    }

    private void findViews() {
        eye = (ImageView) findViewById(R.id.input_edit_text_view_eye);
        view = (TextInputLayout) findViewById(R.id.input_edit_text_view_layout);
        edit = (TextInputEditText) findViewById(R.id.input_edit_text_view_edit);
    }

    private void setResources() {
        redBorder = R.drawable.red_border_edit_text;
        brownBorder = R.drawable.brown_border_edit_text;
        red = ResourcesCompat.getColor(getResources(), R.color.red, null);
        black = ResourcesCompat.getColor(getResources(), R.color.black, null);
        gray = ResourcesCompat.getColor(getResources(), R.color.gray_light, null);
    }

    public void setInputEnabled(boolean value) {
        edit.setEnabled(value);
    }

    private void setListeners() {
        edit.setOnFocusChangeListener(this);
        this.setOnFocusChangeListener((v, hasFocus) -> edit.requestFocus());

        eye.setOnClickListener(v -> {
            if (edit.getTransformationMethod() != null) {
                edit.setTransformationMethod(null);
                edit.setSelection(edit.getText().length());
            } else {
                edit.setTransformationMethod(new PasswordTransformationMethod());
                edit.setSelection(edit.getText().length());
            }
        });
    }

    private void setIdTag(String id) {
        if (Util.nullOrEmpty(id)) {
            ID_TAG = "";
        } else {
            ID_TAG = id;
        }
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean value) {
        this.error = value;
        error(value);
    }

    public void setText(String text) {
        edit.setText(text);
        if (!Util.nullOrEmpty(text)) {
            view.setHintEnabled(true);
            setEditPadding(onClickedPadding);
            edit.setText(text);
        } else {
            setEditPadding(notClickedPadding);
            IM.onUi().schedule(new Runnable() {
                @Override
                public void run() {
                    view.setHintEnabled(false);
                }
            }, 80, TimeUnit.MILLISECONDS);
        }
    }

    public String getText() {
        return edit.getText().toString();
    }

    public void setHintText(String string) {
        edit.setHint(string);
    }

    public void setShortHintText(boolean hasShortHint, String longHint, String shortHint) {
        this.longHint = longHint;
        this.shortHint = shortHint;
        this.hasShortHint = hasShortHint;
    }

    public String getHintText() {
        return edit.getHint().toString();
    }

    public EditText getEdit() {
        return edit;
    }

    private void error(boolean error) {
        if (error) {
            view.setBackgroundResource(redBorder);
            view.setHintTextAppearance(R.style.FloatingLabelError);
            edit.setTextColor(red);
            edit.setHintTextColor(red);
        } else {
            view.setBackgroundResource(brownBorder);
            view.setHintTextAppearance(R.style.FloatingLabel);
            edit.setTextColor(black);
            edit.setHintTextColor(gray);
        }
    }

    private void setShowPasswordVisibility(boolean isVisible) {
        if (isVisible) {
            eye.setVisibility(VISIBLE);
        } else {
            eye.setVisibility(GONE);
        }
    }

    private void setEditPadding(int[] paddings) {
        edit.setPadding(Screen.dpToPx(paddings[0]), Screen.dpToPx(paddings[1]), Screen.dpToPx(paddings[2]), Screen.dpToPx(paddings[3]));
    }

    public void setMaxCharacterNumber(int maxLength) {
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.input_edit_text_view_edit) {
            if (hasFocus) {
                if (hasShortHint) {
                    setHintText(longHint);
                }
                setError(false);
                view.setHintEnabled(true);
                setEditPadding(onClickedPadding);
                UI.showSoftInput(v, true);
            } else {
                if (Util.nullOrEmpty(edit.getText().toString())) {
                    if (hasShortHint) {
                        setHintText(shortHint);
                    }
                    setEditPadding(notClickedPadding);
                    IM.onUi().schedule(new Runnable() {
                        @Override
                        public void run() {
                            view.setHintEnabled(false);
                        }
                    }, 100, TimeUnit.MILLISECONDS);
                }
            }
        } else if (v == this) {
            UI.hideSoftInputForce();
           // edit.requestFocus(); //Jesli chce aby na wejsciu otrzymywac focus na buttona
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setError(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}