package wfiis.pizzerialesna.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.model.Pizza;

public class PizzaMenuAdapter extends RecyclerView.Adapter<PizzaMenuAdapter.ViewHolder> {

    private List<Pizza> data;

    public PizzaMenuAdapter(List<Pizza> pizzaList) {
        data = new ArrayList<>();
        if (pizzaList != null && !pizzaList.isEmpty() && pizzaList.size() > 0) {
            this.data = pizzaList;
        }
    }

    @Override
    public PizzaMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pizza_item, parent, false);

        ViewHolder vh = new ViewHolder(rootView);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pizza pizza = data.get(position);

        holder.pizzaName.setText(pizza.getName());
        holder.pizzaContent.setText(pizza.getContent());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pizzaName;
        public TextView pizzaContent;

        public ViewHolder(View rootView) {
            super(rootView);
            pizzaName = rootView.findViewById(R.id.list_pizza_item_name);
            pizzaContent = rootView.findViewById(R.id.list_pizza_item_content);
        }
    }
}
