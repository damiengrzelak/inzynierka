package wfiis.pizzerialesna.customViews.customViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customViews.NavigationMenuView;

public class NavigationMenuViewAdapter extends BaseAdapter {

    private List<NavigationMenuView.MenuItem> menuItems;

    public NavigationMenuViewAdapter(List<NavigationMenuView.MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public NavigationMenuView.MenuItem getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavigationMenuView.MenuItem menuItem = getItem(position);
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_navigation_menu, null);

            holder = new ViewHolder();
//            holder.name = (TextView) convertView.findViewById(R.id.list_item_navigation_drawer_menu_name);
            holder.image = (ImageView) convertView.findViewById(R.id.list_item_navigation_menu_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.name.setText(menuItem.getName());
        holder.image.setImageDrawable(menuItem.getDrawable());

        return convertView;
    }

    //View Holder--------------------------------------------------------------------------------------
    private static class ViewHolder {
        public TextView name;
        public ImageView image;
    }
}
