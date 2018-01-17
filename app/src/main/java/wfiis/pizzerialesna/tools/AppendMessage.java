package wfiis.pizzerialesna.tools;

import android.support.annotation.StringRes;

import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.customViews.TopToast;

import static wfiis.pizzerialesna.tools.Util.nullOrEmpty;

public class AppendMessage {
    private static StringBuilder messageBuilder;

    public static void appendMessage(@StringRes int res) {
        if (messageBuilder == null) {
            messageBuilder = new StringBuilder();
        } else {
            messageBuilder.append("\n");
        }
        messageBuilder.append(IM.application().getString(res));
    }

    public static void showSuccessOrError() {
        if (messageBuilder != null && !nullOrEmpty(messageBuilder.toString())) {
            TopToast.show(messageBuilder.toString(), TopToast.TYPE_ERROR, TopToast.DURATION_LONG);
            messageBuilder = null;
        }

    }
}