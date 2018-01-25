package wfiis.pizzerialesna.fragments.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.model.Pizza;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.tools.SpanUtils;

public class PizzaMenuAdapter extends RecyclerView.Adapter<PizzaMenuAdapter.ViewHolder> {

    private List<Pizza> data;
    private Drawable d;

    public PizzaMenuAdapter(List<Pizza> pizzaList) {
        data = new ArrayList<>();
        this.data = pizzaList;
    }

    @Override
    public PizzaMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pizza_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pizza pizza = data.get(position);

        holder.pizzaName.setText(pizza.getNumber() + ". " + pizza.getName());
        holder.pizzaContent.setText(pizza.getIngredients());
        SpanUtils.on(holder.cm28).convertToMoney(data.get(position).getPrice28());
        SpanUtils.on(holder.cm34).convertToMoney(data.get(position).getPrice34());
        SpanUtils.on(holder.cm44).convertToMoney(data.get(position).getPrice44());

        if (data.get(position).getType() != 0) {
            holder.statusType.setVisibility(View.VISIBLE);
            if (data.get(position).getType() == 1) {
                d = PizzaStatusType.HOT.d;
            } else if (data.get(position).getType() == 2) {
                d = PizzaStatusType.NEW.d;
            } else if (data.get(position).getType() == 3) {
                d = PizzaStatusType.OUR.d;
            }

            holder.statusType.setImageDrawable(d);
        } else {
            holder.statusType.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateAdapter(List<Pizza> pizzaList) {
        data.clear();
        this.data = pizzaList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pizzaName;
        private TextView pizzaContent;
        private TextView cm28;
        private TextView cm34;
        private TextView cm44;
        private ImageView statusType;


        public ViewHolder(View rootView) {
            super(rootView);
            pizzaName = rootView.findViewById(R.id.list_pizza_item_name);
            pizzaContent = rootView.findViewById(R.id.list_pizza_item_content);
            cm28 = rootView.findViewById(R.id.list_pizza_item_28);
            cm34 = rootView.findViewById(R.id.list_pizza_item_34);
            cm44 = rootView.findViewById(R.id.list_pizza_item_44);
            statusType = rootView.findViewById(R.id.list_pizza_item_status_type);
        }
    }
}
