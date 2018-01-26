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

public class NavigationMenuView extends LinearLayout implements AdapterView.OnItemClickListener {

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

        menuItems.add(new MenuItem("Logo", ContextCompat.getDrawable(getContext(), R.drawable.pizzeria_logo), new MenuItem.ClickListener() {
            @Override
            public void onClick() {

            }
        }));

//        //Latest posts
//        menuItems.add(new MenuItem("Najnowsze", ContextCompat.getDrawable(getContext(), R.drawable.menu_najnowsze), new MenuItem.ClickListener() {
//            @Override
//            public void onClick() {
//            //    MainFragmentNavigator.goToHome();
//            }
//        }));
//
//        //Favourites
//        menuItems.add(new MenuItem("Ulubione", ContextCompat.getDrawable(getContext(), R.drawable.menu_ulubione), new MenuItem.ClickListener() {
//            @Override
//            public void onClick() {
//             //   MainFragmentNavigator.goToFavourites();
//            }
//        }));
//
//
//        menuItems.add(new MenuItem("O blogu", ContextCompat.getDrawable(getContext(), R.drawable.menu_o_blogu), new MenuItem.ClickListener() {
//            @Override
//            public void onClick() {
//              ///  MainFragmentNavigator.goToAboutUs();
//            }
//        }));
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

        public MenuItem(String name, Drawable drawable, ClickListener clickListener) {
            this.name = name;
            this.drawable = drawable;
            this.clickListener = clickListener;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public String getName() {
            return name;
        }

        private interface ClickListener {
            void onClick();
        }
    }
}

