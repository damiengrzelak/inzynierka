package wfiis.pizzerialesna.tools;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Screen;
import com.inverce.mod.core.Ui;

public class UI extends Ui {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            try {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            } catch (Exception ignored) {
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Nullable
    public static Bitmap createScreenShoot(Fragment view) {
        return view != null ? createScreenShoot(view.getView()) : null;
    }

    @Nullable
    public static Bitmap createScreenShoot(View view) {
        if (view == null) {
            return null;
        }
        try {
            // create bitmap screen capture
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return bitmap;
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            com.inverce.mod.core.Log.exs(e);
        }

        return null;
    }

    public static void hideSoftInput(View view) {
        if (view == null) return;
        try {
            InputMethodManager imm = (InputMethodManager) IM.context().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) { /* safely ignore, as ex in here means we could not hide keyboard */ }

       // Tools.clearCurrentFocus();
    }

    public static void showSoftInput(View view) {
        showSoftInput(view, false);
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


    public static void makeToast(String message) {
        Toast.makeText(IM.context(), message, Toast.LENGTH_SHORT).show();
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
