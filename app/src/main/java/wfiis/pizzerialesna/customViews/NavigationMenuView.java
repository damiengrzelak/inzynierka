package wfiis.pizzerialesna.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customViews.customViewAdapters.NavigationMenuViewAdapter;
import wfiis.pizzerialesna.tools.LeftMenuNavigator;
import wfiis.pizzerialesna.tools.sharedPref.UserUtils;

public class NavigationMenuView extends LinearLayout implements AdapterView.OnItemClickListener{

    private ListView menuListView;
    private NavigationMenuViewAdapter menuAdapter;


    public NavigationMenuView(Context context) {
        super(context);
        init();
    }

    public NavigationMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_navigation_menu, this);
        findViews();
        fillMenuList();
        setListeners();
    }

    private void findViews() {
        menuListView = findViewById(R.id.navigation_menu_list);
    }

    private void fillMenuList() {
        menuAdapter = new NavigationMenuViewAdapter(createMenuItems());
        menuListView.setAdapter(menuAdapter);
    }

    private List<MenuItem> createMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        menuItems.add(new MenuItem("Logo", ContextCompat.getDrawable(getContext(), R.drawable.pizzeria_logo), null, "", new MenuItem.ClickListener() {
            @Override
            public void onClick() {

            }
        }));

//        //My profil
        menuItems.add(new MenuItem("My profile", null, ContextCompat.getDrawable(getContext(), R.drawable.my_profile_icon), "Mój profil", new MenuItem.ClickListener() {
            @Override
            public void onClick() {
            //    MainFragmentNavigator.goToHome();
            }
        }));
//
//        //settings
        menuItems.add(new MenuItem("Settings", null, ContextCompat.getDrawable(getContext(), R.drawable.setting_icon), "Ustawienia", new MenuItem.ClickListener() {
            @Override
            public void onClick() {
//             //   MainFragmentNavigator.goToFavourites();
            }
        }));
//
//      //wyloguj
        menuItems.add(new MenuItem("Logout", null, ContextCompat.getDrawable(getContext(), R.drawable.logout_icon), "Wyloguj", new MenuItem.ClickListener() {
            @Override
            public void onClick() {
                UserUtils userUtils = new UserUtils();
                userUtils.logOut();
                LeftMenuNavigator.logOut();
           }
        }));
//
//        //About
//        menuItems.add(new MenuItem("Apptento", ContextCompat.getDrawable(getContext(), R.drawable.menu_apptento), new MenuItem.ClickListener() {
//            @Override
//            public void onClick() {
//              //  MainFragmentNavigator.goToAboutApp();
//            }
//        }));
        return menuItems;
    }

    private void setListeners() {
        menuListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       menuAdapter.getItem(i).clickListener.onClick();
    }

    public static class MenuItem {
        private String name;
        private Drawable drawable;
        private ClickListener clickListener;

        private String itemName;
        private Drawable itemImg;

        public MenuItem(String name, Drawable drawable, Drawable itemdrawable, String itemText, ClickListener clickListener) {
            this.name = name;
            this.drawable = drawable;
            this.clickListener = clickListener;
            this.itemImg = itemdrawable;
            this.itemName = itemText;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public Drawable getItemImg(){
            return itemImg;
        }
        public String getItemName(){
            return itemName;
        }

        public String getName() {
            return name;
        }

        private interface ClickListener {
            void onClick();
        }
    }
}

