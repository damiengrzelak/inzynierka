package wfiis.pizzerialesna.modules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.interactions.TopBarInteractions;

public class TopBarFragment extends BaseFragment implements TopBarInteractions, View.OnClickListener {
    private View goBackBtn;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);

        //goBackBtn = view.findViewById(R.id.go_back_btn);
        //title = view.findViewById(R.id.top_bar_title);

        goBackBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void showBackIcon(boolean visible) {
        goBackBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTitle(@StringRes int res) {
        title.setText(res);
    }

    @Override
    public void onClick(View view) {
        if (getActions() == null) {
            return;
        }

        switch (view.getId()) {
            //case R.id.go_back_btn:
            //    getActions().navigateBack();
            //    break;
        }
    }
}

