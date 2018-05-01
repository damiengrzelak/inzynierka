package wfiis.pizzerialesna.fragments.menu.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customDialogs.OrderCustomDialog;
import wfiis.pizzerialesna.model.Extras;
import wfiis.pizzerialesna.model.Pizza;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.model.Warianty;
import wfiis.pizzerialesna.tools.SpanUtils;

public class PizzaMenuAdapter extends RecyclerView.Adapter<PizzaMenuAdapter.ViewHolder> {

    private List<Object> data;
    private Drawable d;
    private int pos = 0;

    public PizzaMenuAdapter(List<Object> pizzaList) {
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
        if (data.get(position) instanceof Pizza) {
            Pizza pizza = (Pizza) data.get(position);

            holder.pizzaName.setText(pizza.getNumber() + ". " + pizza.getName());
            holder.pizzaContent.setText(pizza.getIngredients());
            holder.cm28.setVisibility(View.VISIBLE);
            holder.cm34.setVisibility(View.VISIBLE);
            holder.cm44.setVisibility(View.VISIBLE);
            holder.cm28Bt.setVisibility(View.VISIBLE);
            holder.cm34Bt.setVisibility(View.VISIBLE);
            holder.cm44Bt.setVisibility(View.VISIBLE);
            SpanUtils.on(holder.cm28).convertToMoney(pizza.getPrice28());
            SpanUtils.on(holder.cm34).convertToMoney(pizza.getPrice34());
            SpanUtils.on(holder.cm44).convertToMoney(pizza.getPrice44());

            if (pizza.getType() != 0) {
                holder.statusType.setVisibility(View.VISIBLE);
                if (pizza.getType() == 1) {
                    d = PizzaStatusType.HOT.d;
                } else if (pizza.getType() == 2) {
                    d = PizzaStatusType.NEW.d;
                } else if (pizza.getType() == 3) {
                    d = PizzaStatusType.OUR.d;
                }

                holder.statusType.setImageDrawable(d);
            } else {
                holder.statusType.setVisibility(View.GONE);
            }
        } else {
            Extras extras = (Extras) data.get(position);

            holder.pizzaName.setText(extras.getNumber() + ". " + extras.getName());
            if (extras.getVariants() != null && extras.getVariants().size() > 0) {
                String content = "";
                int size = extras.getVariants().size();
                for (int i = 0; i < size; i++) {
                    if (i != size - 1) {
                        content = content + extras.getVariants().get(i).getName() + ", ";
                    } else {
                        content = content + extras.getVariants().get(i).getName();
                    }
                }
                holder.pizzaContent.setText(content);
            } else {
                holder.pizzaContent.setText("");
            }

            if (extras.getLowPrice() > 0) {
                if (extras.getMediumPrice() > 0 || extras.getHighPrice() > 0) {
                    SpanUtils.on(holder.cm28).convertToMoney(extras.getLowPrice());
                    holder.cm28Bt.setVisibility(View.VISIBLE);
                } else {
                    SpanUtils.on(holder.cm28).convertToMoney(extras.getLowPrice());
                    holder.cm28Bt.setVisibility(View.GONE);
                }
            } else {
                holder.cm28.setVisibility(View.GONE);
                holder.cm28Bt.setVisibility(View.GONE);
            }

            if (extras.getMediumPrice() > 0) {
                SpanUtils.on(holder.cm34).convertToMoney(extras.getMediumPrice());
                holder.cm34Bt.setVisibility(View.VISIBLE);
            } else {
                holder.cm34.setVisibility(View.GONE);
                holder.cm34Bt.setVisibility(View.GONE);
            }

            if (extras.getHighPrice() > 0) {
                SpanUtils.on(holder.cm44).convertToMoney(extras.getHighPrice());
                holder.cm44Bt.setVisibility(View.VISIBLE);
            } else {
                holder.cm44.setVisibility(View.GONE);
                holder.cm44Bt.setVisibility(View.GONE);
            }

            holder.statusType.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateAdapter(List<Object> pizzaList) {
        data.clear();
        data = new ArrayList<>();
        this.data = pizzaList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView pizzaName;
        private TextView pizzaContent;
        private TextView cm28;
        private TextView cm34;
        private TextView cm44;
        private ImageView statusType;
        private Button cm28Bt;
        private Button cm34Bt;
        private Button cm44Bt;


        public ViewHolder(View rootView) {
            super(rootView);
            pizzaName = rootView.findViewById(R.id.list_pizza_item_name);
            pizzaContent = rootView.findViewById(R.id.list_pizza_item_content);
            cm28 = rootView.findViewById(R.id.list_pizza_item_28);
            cm34 = rootView.findViewById(R.id.list_pizza_item_34);
            cm44 = rootView.findViewById(R.id.list_pizza_item_44);
            cm28Bt = rootView.findViewById(R.id.cm28);
            cm34Bt = rootView.findViewById(R.id.cm34);
            cm44Bt = rootView.findViewById(R.id.cm44);
            statusType = rootView.findViewById(R.id.list_pizza_item_status_type);

            rootView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            pos = getAdapterPosition();
            if(data.get(pos) instanceof Extras) {
                return;
            }

            OrderCustomDialog ord = new OrderCustomDialog(data.get(pos));
            ord.show(IM.activity().getFragmentManager(), IM.activity().getFragmentManager().getClass().toString());
        }
    }
}
