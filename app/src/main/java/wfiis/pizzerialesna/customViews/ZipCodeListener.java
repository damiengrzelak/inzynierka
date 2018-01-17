package wfiis.pizzerialesna.customViews;


import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

public class ZipCodeListener implements TextWatcher {
    private static final int ZIP_CODE_LENGTH = 6;
    private EditText editText;

    public ZipCodeListener(EditText edit) {
        this.editText = edit;
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ZIP_CODE_LENGTH)});
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = editText.getText().toString();
        if (text.length() == 3 && text.charAt(2) != '-' && isNumeric(text)) {
            performChange(text);
        }
    }


    private static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void performChange(String text) {
        editText.setText(text.substring(0, 2).concat("-").concat(text.substring(2, text.length())));
        editText.setSelection(editText.length());
    }

}
