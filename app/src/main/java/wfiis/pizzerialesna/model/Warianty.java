package wfiis.pizzerialesna.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.raizlabs.android.dbflow.annotation.Column;

/**
 * Created by Damian on 04.04.2018.
 */

@IgnoreExtraProperties
public class Warianty {
    private String name;

    public Warianty() {
    }

    public Warianty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
