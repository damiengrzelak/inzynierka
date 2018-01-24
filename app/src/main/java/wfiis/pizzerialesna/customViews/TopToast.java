package wfiis.pizzerialesna.customViews;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Ui;

import java.util.concurrent.ConcurrentLinkedQueue;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.tools.SpanUtils;

public class TopToast extends RelativeLayout {

    private static final String TAG = "TopToast";

    public static final int TYPE_ERROR = 0;
    public static final int TYPE_SUCCESS = 1;

    public static final long DURATION_SHORT = 2500;
    public static final long DURATION_LONG = 5000;

    private static final long SLIDE_DURATION = 200;

    private static ConcurrentLinkedQueue<QueuedToast> toastsQueue = new ConcurrentLinkedQueue<>();
    private static boolean toastIsBeingDisplayed;
    private static QueuedToast currentToast = null;

    private long duration;
    private TextView messageText;
    private float marginTopPx;
    private float heightPx;
    private static RelativeLayout container;
    private ImageView icon;
    private static ImageView closeBt;

    public static void show(String message, int type, long duration) {
        if (currentToast != null && currentToast.messageTxt.equals(message)) {
            Log.w(TAG, "Not showing top toast, same toast is displaying");
            return;
        }

        for (QueuedToast queued : toastsQueue) {
            if (queued.messageTxt.equals(message)) {
                Log.w(TAG, "Not showing top toast, same toast is in queue");
                return;
            }
        }

        QueuedToast queuedToast = new QueuedToast();
        queuedToast.messageTxt = message;
        queuedToast.type = type;
        queuedToast.duration = duration;
        toastsQueue.add(queuedToast);
        tryExecuteQueue();
    }

    public static void show(@StringRes int messageRes, int type, long duration) {
        show(IM.activity().getString(messageRes), type, duration);
    }

    public static void showToastOnDialog(String message, int type, long duration, Window window) {
        if (currentToast != null && currentToast.messageTxt.equals(message)) {
            Log.w(TAG, "Not showing top toast, same toast is displaying");
            return;
        }

        for (QueuedToast queued : toastsQueue) {
            if (queued.messageTxt.equals(message)) {
                Log.w(TAG, "Not showing top toast, same toast is in queue");
                return;
            }
        }

        QueuedToast queuedToast = new QueuedToast();
        queuedToast.messageTxt = message;
        queuedToast.type = type;
        queuedToast.duration = duration;
        toastsQueue.add(queuedToast);
        tryExecuteDialogToasts(window);
    }


    public static void showToastOnDialog(@StringRes int messageRes, int type, long duration, Window window) {
        showToastOnDialog(IM.activity().getString(messageRes), type, duration, window);
    }

    public static void showWithClickableTextInDialog(String normalMessagePart, String clickableMessagePart, ClickableSpan onClick, int type, long duration, Window window) {
        QueuedToast queuedToast = new QueuedToast();
        queuedToast.messageTxt = normalMessagePart;
        queuedToast.clickableTxt = clickableMessagePart;
        queuedToast.type = type;
        queuedToast.duration = duration;
        queuedToast.clickAction = onClick;
        toastsQueue.add(queuedToast);
        tryExecuteDialogToasts(window);
    }

    public static void showClickableTopToast(String normalMessagePart, int type, OnClickListener onClick, Window window) {
        QueuedToast queuedToast = new QueuedToast();
        queuedToast.messageTxt = normalMessagePart;
        queuedToast.type = type;
        queuedToast.onClickAction = onClick;
        toastsQueue.add(queuedToast);
        tryExecuteUnlimitedDialog(window);
    }

    private static void tryExecuteDialogToasts(Window window) {
        if (!Ui.isUiThread()) {
            IM.onUi().execute(() -> tryExecuteDialogToasts(window));
            return;
        }
        if (!toastIsBeingDisplayed && !toastsQueue.isEmpty()) {
            Activity activity = IM.activity();
            if (activity == null) {
                toastsQueue.clear();
                return;
            }
            QueuedToast queuedToast = toastsQueue.poll();
            TopToast topToast = new TopToast(activity);

            if (queuedToast.clickableTxt == null) {
                topToast.setText(queuedToast.messageTxt);
            } else {
                SpanUtils.on(topToast.getMessageText())
                        .normalText(queuedToast.messageTxt + "\n")
                        .clickableText(queuedToast.clickableTxt, queuedToast.clickAction)
                        .done();
            }
            if (queuedToast.onClickAction != null) {
                container.setOnClickListener(queuedToast.onClickAction);
            }
            topToast.setType(activity, queuedToast.type);
            topToast.setDuration(queuedToast.duration);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.addContentView(topToast, layoutParams);
            topToast.slideIn();
            currentToast = queuedToast;
            closeBt.setOnClickListener(v -> {
                if (topToast != null) {
                    topToast.slideOut();
                }
            });
        }
    }

    private static void tryExecuteUnlimitedDialog(Window window) {
        if (!toastIsBeingDisplayed && !toastsQueue.isEmpty()) {
            Activity activity = IM.activity();
            if (activity == null) {
                toastsQueue.clear();
                return;
            }
            QueuedToast queuedToast = toastsQueue.poll();
            TopToast topToast = new TopToast(activity);

            if (queuedToast.clickableTxt == null) {
                topToast.setText(queuedToast.messageTxt);
            } else {
                SpanUtils.on(topToast.getMessageText())
                        .normalText(queuedToast.messageTxt + "\n")
                        .clickableText(queuedToast.clickableTxt, queuedToast.clickAction)
                        .done();
            }
            if (queuedToast.onClickAction != null) {
                container.setOnClickListener(queuedToast.onClickAction);
            }
            topToast.setType(activity, queuedToast.type);
            topToast.setDuration(queuedToast.duration);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.addContentView(topToast, layoutParams);
            topToast.slideInUnlimited();
            currentToast = queuedToast;
            closeBt.setOnClickListener(v -> {
                if (topToast != null) {
                    topToast.slideOut();
                    topToast.removeQueuque();
                }
            });
        }
    }

    private static void tryExecuteQueue() {
        if (!Ui.isUiThread()) {
            IM.onUi().execute(TopToast::tryExecuteQueue);
            return;
        }

        if (!toastIsBeingDisplayed && !toastsQueue.isEmpty()) {
            Activity activity = IM.activity();
            if (activity == null) {
                toastsQueue.clear();
                return;
            }
            QueuedToast queuedToast = toastsQueue.poll();
            TopToast topToast = new TopToast(activity);
            if (queuedToast.clickableTxt == null) {
                topToast.setText(queuedToast.messageTxt);
            } else {
                SpanUtils.on(topToast.getMessageText())
                        .normalText(queuedToast.messageTxt + "\n")
                        .clickableText(queuedToast.clickableTxt, queuedToast.clickAction);
            }
            if (queuedToast.onClickAction != null) {
                container.setOnClickListener(queuedToast.onClickAction);
            }
            topToast.setType(activity, queuedToast.type);
            topToast.setDuration(queuedToast.duration);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            activity.getWindow().addContentView(topToast, layoutParams);
            topToast.slideIn();
            currentToast = queuedToast;

            closeBt.setOnClickListener(v -> {
                if (topToast != null) {
                    topToast.slideOut();
                }
            });
        }
    }

    public void removeQueuque() {
        toastsQueue.removeAll(toastsQueue);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TopToast(Context context) {
        super(context);
        init(context);
    }

    public TopToast(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopToast(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopToast(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.view_toast_icon, this);
        findViews();
    }

    private void findViews() {
        messageText = (TextView) findViewById(R.id.fragment_login_toast_message_text);
        container = (RelativeLayout) findViewById(R.id.view_toast_icon_container);
        icon = (ImageView) findViewById(R.id.view_toast_icon_exclamation);
        closeBt = (ImageView) findViewById(R.id.snackbar_close_button);
    }

    public TextView getMessageText() {
        return messageText;
    }

    public void setText(String text) {
        if (text != null) {
            messageText.setText(text);
        }
    }

    public void setType(Context context, int type) {
        if (type == TYPE_ERROR) {
            container.setBackgroundResource(R.color.error_background);
            messageText.setTextColor(ResourcesCompat.getColor(IM.resources(), R.color.l_warning_red, null));
            icon.setVisibility(VISIBLE);
        } else if (type == TYPE_SUCCESS) {
            container.setBackgroundResource(R.color.success_background);
            messageText.setTextColor(ResourcesCompat.getColor(IM.resources(), R.color.gray_light, null));
            icon.setVisibility(VISIBLE);
        }
    }

    private void slideInUnlimited() {
        toastIsBeingDisplayed = true;

        container.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        marginTopPx = IM.context().getResources().getDimension(R.dimen.toptoast_height);
        heightPx = container.getMeasuredHeight();

        container.setY(-heightPx);
        container
                .animate()
                .yBy(marginTopPx + heightPx)
                .setDuration(SLIDE_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    private void slideIn() {
        toastIsBeingDisplayed = true;

        container.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        marginTopPx = IM.context().getResources().getDimension(R.dimen.toptoast_height);
        heightPx = container.getMeasuredHeight();

        container.setY(-heightPx);
        container
                .animate()
                .yBy(marginTopPx + heightPx)
                .setDuration(SLIDE_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideOut();
                            }
                        }, duration);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    public void slideOut() {
        container
                .animate()
                .yBy(-heightPx - marginTopPx)
                .setDuration(SLIDE_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (TopToast.this.getParent() != null) {
                            ((ViewGroup) TopToast.this.getParent()).removeView(TopToast.this);
                        }
                        toastIsBeingDisplayed = false;
                        currentToast = null;
                        tryExecuteQueue();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    private static class QueuedToast {
        public int type;
        public long duration;
        public String messageTxt;
        public String clickableTxt;
        public ClickableSpan clickAction;
        public OnClickListener onClickAction;
    }
}
