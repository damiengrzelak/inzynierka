package wfiis.pizzerialesna.customDialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wfiis.pizzerialesna.R;

public class DostawaInfoDialog extends DialogFragment {
    private Button close;

    public DostawaInfoDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_dostawa_info, container,
                false);

        findViews(rootView);

        return rootView;
    }

    private void findViews(View rootView) {
        close = rootView.findViewById(R.id.dialog_dostawa_info_close);

        close.setOnClickListener(v -> dismiss());
    }
}
