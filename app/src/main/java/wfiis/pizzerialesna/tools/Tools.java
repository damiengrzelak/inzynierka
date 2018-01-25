package wfiis.pizzerialesna.tools;

import android.support.annotation.Nullable;

import com.inverce.mod.core.IM;

import java.lang.ref.WeakReference;

import wfiis.pizzerialesna.interactions.ActivityInteractions;

public class Tools {
    @Nullable
    public static WeakReference<ActivityInteractions> getMainActivityInterface() {
        Object activity = IM.activity();
        if (activity instanceof ActivityInteractions) {
            return new WeakReference<>((ActivityInteractions) activity);
        }

        return null;
    }

}
