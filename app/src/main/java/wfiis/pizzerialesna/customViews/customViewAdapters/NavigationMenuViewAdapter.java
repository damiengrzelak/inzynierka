package wfiis.pizzerialesna.customViews.customViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

            holder.image = (ImageView) convertView.findViewById(R.id.list_item_navigation_menu_image);
            holder.itemImg = convertView.findViewById(R.id.list_item_navigation_menu_image_img);
            holder.itemTxt = convertView.findViewById(R.id.list_item_navigation_menu_image_txt);
            holder.item = convertView.findViewById(R.id.list_item_navigation_menu_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (menuItem.getDrawable() != null) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageDrawable(menuItem.getDrawable());
        } else {
            holder.image.setVisibility(View.GONE);
        }
        if (menuItem.getItemImg() != null) {
            holder.itemImg.setImageDrawable(menuItem.getItemImg());
        }
        if (menuItem.getItemName() != null && !menuItem.getItemName().equals("") && menuItem.getItemName().length() > 0){
            holder.itemTxt.setText(menuItem.getItemName());
        }

        return convertView;
    }

    //View Holder--------------------------------------------------------------------------------------
    private static class ViewHolder {
        public ImageView image;
        private ImageView itemImg;
        private TextView itemTxt;
        private LinearLayout item;
    }
}
