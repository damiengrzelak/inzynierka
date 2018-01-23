package wfiis.pizzerialesna.model;

public class OtherMenuItems {
    public static final int OTHER_TYPE = 0;
    public static final int SAL_TYPE = 1;
    public static final int ZAP_TYPE = 2;

    private Object menuItem;
    private int type;

    public OtherMenuItems(Object menuItemt, int type) {
        this.menuItem = menuItemt;
        this.type = type;
    }

    public Object getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Object menuItem) {
        this.menuItem = menuItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
