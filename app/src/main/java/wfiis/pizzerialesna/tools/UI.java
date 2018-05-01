package wfiis.pizzerialesna.tools;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Ui;

public class UI extends Ui {
    public static void hideSoftInput(View view) {
        if (view == null) return;
        try {
            InputMethodManager imm = (InputMethodManager) IM.context().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) { /* safely ignore, as ex in here means we could not hide keyboard */ }

       // Tools.clearCurrentFocus();
    }

    public static void showSoftInput(View view, boolean useImplicit) {
        try {
            InputMethodManager imm = (InputMethodManager) IM.context().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (useImplicit) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            } else {
                imm.toggleSoftInput(0, 0);
            }
        } catch (Exception ignored) { /* safely ignore, as ex in here means we could not hide keyboard */ }
    }

    public static String string() {
        return string();
    }

    public static String string(@StringRes int string) {
        Context context = IM.context();
        if (context != null)
            return context.getResources().getString(string);
        else return null;
    }

    public static boolean visible(View view, boolean visible) {
        return visible(view, visible, true);
    }

    public static boolean visible(View view, boolean visible, boolean gone) {
        if (view != null) {
            if (visible) {
                view.setVisibility(View.VISIBLE);
                return true;
            } else {
                if (gone) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
        return false;
    }

    public static boolean visible(View root, @IdRes int res, boolean visible, boolean gone) {
        return root != null && visible(root.findViewById(res), visible, gone);
    }

    public static void hideSoftInputForce() {
        View currentFocus = IM.activity().getCurrentFocus();
        if (currentFocus != null) {
            hideSoftInput(currentFocus);
        }
    }
}
