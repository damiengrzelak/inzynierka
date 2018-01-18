package wfiis.pizzerialesna.model;

import android.graphics.drawable.Drawable;

import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;

public enum PizzaStatusType {
    HOT(1, IM.context().getResources().getDrawable(R.drawable.ostra)),
    NEW(2, IM.context().getResources().getDrawable(R.drawable.nowosc)),
    OUR(3, IM.context().getResources().getDrawable(R.drawable.polecamy));

    public  Drawable d;
    public  int type ;

    PizzaStatusType(int type, Drawable drawable) {
        this.type = type;
        this.d = drawable;
    }
}
