package wfiis.pizzerialesna.fragments.basket.posumowanie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.model.Basket;
import wfiis.pizzerialesna.tools.SpanUtils;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<Basket> data;

    public OrderListAdapter(List<Basket> zamowienieList) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data = zamowienieList;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posumowanie, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        Basket item = data.get(position);

        double cena = (item.getPriceIngredients() > 0.0) ? item.getPrize() + item.getPriceIngredients() : item.getPrize();

        SpanUtils.on(holder.cena).convertToMoney(cena);
        if (item.getSize() != null && item.getSize().toLowerCase().contains("mini")) {
            holder.name.setText(item.getName() + " - mini");
        } else if (item.getSize() != null && item.getSize().toLowerCase().contains("mał")) {
            holder.name.setText(item.getName() + " - mała");
        } else if (item.getSize() != null && item.getSize().toLowerCase().contains("śre")) {
            holder.name.setText(item.getName() + " - średnia");
        } else {
            holder.name.setText(item.getName());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateAdapter(List<Basket> basketList) {
        if (basketList.size() > 0) {
            data = new ArrayList<>();
            this.data = basketList;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView cena;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.podsumowanie_item_nazwa);
            cena = itemView.findViewById(R.id.podsumowanie_item_cena);
        }
    }
}
