package wfiis.pizzerialesna.modules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;
import wfiis.pizzerialesna.interactions.ActivityInteractions;
import wfiis.pizzerialesna.interactions.TopBarInteractions;
import wfiis.pizzerialesna.tools.Tools;

public class TopBarFragment extends BaseFragment implements TopBarInteractions, View.OnClickListener {
    private View goBackBtn;
    private View menuBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);

        goBackBtn = view.findViewById(R.id.go_back_btn);
        //title = view.findViewById(R.id.top_bar_title);
        menuBtn = view.findViewById(R.id.menu_btn);

        goBackBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void showBackIcon(boolean visible) {
        goBackBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTitle(@StringRes int res) {
    }

    @Override
    public void showMenuIcon(boolean visible) {
        menuBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int isMenuIconVisible() {
        return menuBtn.getVisibility();
    }

    @Override
    public void onClick(View view) {
//        if (getActions() == null) {
//            return;
//        }

        switch (view.getId()) {
            case R.id.go_back_btn:
                if (getActions() != null) {
                    getActions().disMissPreloader();
                }
                getActions().navigateBack();
                break;
            case R.id.menu_btn:
                onClickMenu(view);
                break;
        }
    }

    private void onClickMenu(View view) {
        WeakReference<ActivityInteractions> mainActivityInterface = Tools.getMainActivityInterface();
        if (mainActivityInterface != null) {
            mainActivityInterface.get().changeDrawerMenuState();
        }
    }

}

