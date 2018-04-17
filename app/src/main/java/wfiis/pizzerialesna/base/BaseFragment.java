package wfiis.pizzerialesna.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import wfiis.pizzerialesna.interactions.ActivityInteractions;

public class BaseFragment extends Fragment {
    ActivityInteractions actions;

    @Nullable
    public ActivityInteractions getActions() {
        return actions;
    }

    public void showPreloader() {
        getActions().showPreloader();
    }

    public void dissMissPreloader() {
        if (getActions() != null) {
            getActions().disMissPreloader();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            //noinspection unchecked // will check later ^^
            actions = (ActivityInteractions) context;
        } catch (Exception ex) {
            throw new IllegalStateException("Activity must implement correct action interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actions = null;
    }
}
