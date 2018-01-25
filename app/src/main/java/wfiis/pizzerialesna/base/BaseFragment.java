package wfiis.pizzerialesna.base;

import android.content.Context;
import android.os.Bundle;
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

    // Fragment utils method may go here :)


    // For example our argument utils
    public BaseFragment addArgumentInt(String name, int value) {
        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        args.putInt(name, value);
        setArguments(args);
        return this;
    }

    public BaseFragment addArgumentString(String name, String value) {
        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        args.putString(name, value);
        setArguments(args);
        return this;
    }

    public int getArgumentInt(String name, int fallback) {
        Bundle args = getArguments();
        return args == null ? fallback : args.getInt(name, fallback);
    }

    public String getArgumentString(String name, String fallback) {
        Bundle args = getArguments();
        return args == null ? fallback : args.getString(name, fallback);
    }
}
