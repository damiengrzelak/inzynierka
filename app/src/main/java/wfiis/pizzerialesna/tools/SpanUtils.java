package wfiis.pizzerialesna.tools;

import android.support.annotation.ColorRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;

public class SpanUtils {
    private final TextView tw;
    SpannableStringBuilder builder;
    int length = 0;

    public static SpanUtils on(TextView textView) {
        return new SpanUtils(textView);
    }

    private SpanUtils(TextView textView) {
        this.tw = textView;
        builder = new SpannableStringBuilder();
    }

    public void convertToMoney(double money){
       tw.setText(String.valueOf(Util.decimPlace(money, 2))+IM.context().getResources().getString(R.string.zl));
    }

    public SpanUtils space() {
        return normalText(" ");
    }

    public SpanUtils clickableText(String text, ClickableSpan onClick) {
        builder.append(text);
        builder.setSpan(onClick, length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color(R.color.l_blue_text)), length, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils normalText(String text) {
        if (text == null) {
            return this;
        }
        builder.append(text);
        length += text.length();
        return this;
    }

    private static int color(@ColorRes final int colorRes) {
        return IM.context().getResources().getColor(colorRes);
    }


    public void done() {
        tw.setText(builder);
        tw.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
