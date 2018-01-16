package wfiis.pizzerialesna.tools;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;

import static android.graphics.Typeface.BOLD;

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

    public SpanUtils asterisks(@ColorRes int colorRes) {
        return coloredText("*", colorRes);
    }

    public SpanUtils space() {
        return normalText(" ");
    }

    public SpanUtils enter() {
        return normalText("\n");
    }

    public SpanUtils coloredText(@StringRes int textRes, @ColorRes int colorRes) {
        return coloredText(text(textRes), colorRes);
    }

    public SpanUtils coloredText(String text, @ColorRes int colorRes) {
        builder.append(text);
        builder.setSpan(new ForegroundColorSpan(color(colorRes)), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils clickableText(@StringRes int textRes, ClickableSpan onClick) {
        return clickableText(text(textRes), onClick);
    }

    public SpanUtils clickableText(String text, ClickableSpan onClick) {
        builder.append(text);
        builder.setSpan(onClick, length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color(R.color.l_blue_text)), length, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils clickableTextUrl(@StringRes int textRes, String url, @ColorRes int colorRes) {
        return clickableTextUrl(text(textRes), url, colorRes);
    }

    public SpanUtils clickableTextUrl(String text, String url, @ColorRes int colorRes) {
        builder.append(text);
        builder.setSpan(new URLSpanNoUnderline(url), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color(colorRes)), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils clickableTextUrlBold(@StringRes int textRes, String url) {
        return clickableTextUrlBold(text(textRes), url);
    }

    public SpanUtils clickableTextUrlBold(String text, String url) {
        builder.append(text);
        builder.setSpan(new URLSpanNoUnderline(url), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(BOLD), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color(R.color.white)), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils coloredBoldText(String text, @ColorRes int colorRes) {
        builder.append(text);
        builder.setSpan(new ForegroundColorSpan(color(colorRes)), length, length + text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(BOLD), length, length + text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils clickableTextDialog(@StringRes int textRes, @ColorRes int colorRes, ClickableSpan onClick) {
        return clickableTextDialog(text(textRes), colorRes, onClick);
    }

    public SpanUtils clickableTextDialog(String text, @ColorRes int colorRes, ClickableSpan onClick) {
        builder.append(text);
        builder.setSpan(onClick, length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color(colorRes)), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    public SpanUtils normalText(@StringRes int textRes) {
        return normalText(text(textRes));
    }

    public SpanUtils normalText(String text) {
        if (text == null) {
            return this;
        }
        builder.append(text);
        length += text.length();
        return this;
    }

    public SpanUtils boldText(@StringRes int textRes) {
        return boldText(text(textRes));
    }

    public SpanUtils boldText(String text) {
        builder.append(text);
        builder.setSpan(new StyleSpan(BOLD), length, length + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        length += text.length();
        return this;
    }

    private static int color(@ColorRes final int colorRes) {
        return IM.context().getResources().getColor(colorRes);
    }

    private static String text(@StringRes final int stringRes) {
        return IM.context().getResources().getString(stringRes);
    }

    public SpanUtils setLinkColor(@ColorRes int linkColor) {
        tw.setLinkTextColor(color(linkColor));
        return this;
    }

    public void doneHint() {
        tw.setHint(builder);
        if (tw instanceof EditText) {
            tw.setCursorVisible(true);
        } else {
            tw.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void done() {
        tw.setText(builder);
        tw.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void doneSetText() {
        tw.setText(builder);
    }

    public Spannable makeSpannable() {
        return builder;
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    public static abstract class ClickableSpanNoUnderline extends ClickableSpan {
        public ClickableSpanNoUnderline() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
