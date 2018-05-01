package wfiis.pizzerialesna.customDialogs;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;

public class ImageDialog extends DialogFragment {
    private TextView close;
    private ImageView img;
    private String url;

    public ImageDialog(){

    }

    @SuppressLint("ValidFragment")
    public ImageDialog(String url) {

        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_image, container,
                false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View rootView) {
        close = rootView.findViewById(R.id.dialog_image_close);
        img = rootView.findViewById(R.id.dialog_image_img);

        Glide.with(IM.context())
                .load(url)
                .fitCenter()
                .into(img);

        close.setOnClickListener(v -> dismiss());
    }
}
