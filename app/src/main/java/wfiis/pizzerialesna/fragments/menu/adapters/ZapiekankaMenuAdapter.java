package wfiis.pizzerialesna.fragments.menu.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customDialogs.OrderCustomDialog;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.model.Zapiekanka;
import wfiis.pizzerialesna.tools.SpanUtils;

public class ZapiekankaMenuAdapter extends RecyclerView.Adapter<ZapiekankaMenuAdapter.ViewHolder> {

    private List<Zapiekanka> data = new ArrayList<>();
    private Drawable d;
    private int pos;

    public ZapiekankaMenuAdapter(List<Zapiekanka> data) {
        this.data = data;
    }

    @Override
    public ZapiekankaMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Zapiekanka z = data.get(position);

        if (position == 0) {
            holder.header.setVisibility(View.VISIBLE);
            holder.headerText.setText(IM.context().getResources().getString(R.string.zapieksy));
        } else {
            holder.header.setVisibility(View.GONE);
        }

        holder.otherName.setText(z.getNumber()+". "+z.getName());
        SpanUtils.on(holder.ontherPrize).convertToMoney(data.get(position).getPrice());

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
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView otherName;
        private TextView ontherPrize;
        private ImageView statusType;

        private RelativeLayout header;
        private TextView headerText;


        public ViewHolder(View rootView) {
            super(rootView);
            otherName = rootView.findViewById(R.id.list_other_item_name);
            ontherPrize = rootView.findViewById(R.id.list_other_item_price);
            statusType = rootView.findViewById(R.id.list_other_item_status_type);

            header = rootView.findViewById(R.id.list_order_item_header);
            headerText = rootView.findViewById(R.id.list_order_item_header_text);

            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            pos = getAdapterPosition();
            OrderCustomDialog ord = new OrderCustomDialog(data.get(pos));
            ord.show(IM.activity().getFragmentManager(), IM.activity().getFragmentManager().getClass().toString());
        }
    }
}