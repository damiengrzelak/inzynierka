package wfiis.pizzerialesna.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import wfiis.pizzerialesna.R;

public class LoaderView extends RelativeLayout {

    private ImageView image;

    public LoaderView(Context context) {
        super(context);
        init(context);
    }

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        inflate(context, R.layout.view_loader, this);
        image = (ImageView) findViewById(R.id.loader);
        setVisibility(this.getVisibility());
    }


    @Override
    public void setVisibility(int visibility) {
        visibility(visibility == VISIBLE);
        super.setVisibility(visibility);
    }

    private void visibility(boolean visible) {
        if (visible) {
            Glide.with(getContext())
                    .load(R.drawable.ellipsis)
                    .asGif()
                    .into(image);
        } else {
            Glide.clear(image);
        }
    }
}

