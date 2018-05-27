package wfiis.pizzerialesna.fragments.facebookAtApp;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.model.FbPost;

public class FbDialog extends DialogFragment {
    private FbPost post;
    private ImageView img;
    private TextView text;
    private Button close;

    public FbDialog() {

    }

    @SuppressLint("ValidFragment")
    public FbDialog(FbPost post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fb_dialog, container,
                false);

        findViews(rootView);
        fillViews();

        return rootView;
    }

    private void fillViews() {
        img.setImageDrawable(post.getImg());
        text.setText(post.getText());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void findViews(View rootView) {
        img = rootView.findViewById(R.id.image);
        text = rootView.findViewById(R.id.text);
        close = rootView.findViewById(R.id.close);
    }
}
