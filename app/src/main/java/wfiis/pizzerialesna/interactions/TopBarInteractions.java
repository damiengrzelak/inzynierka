package wfiis.pizzerialesna.interactions;

import android.support.annotation.StringRes;

public interface TopBarInteractions {
    void showBackIcon(boolean visible);
    void setTitle(@StringRes int res);
}