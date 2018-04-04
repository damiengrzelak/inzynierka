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

import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.model.Inne;
import wfiis.pizzerialesna.tools.SpanUtils;

public class InneMenuAdapter extends RecyclerView.Adapter<InneMenuAdapter.ViewHolder> {

    private List<Inne> inne;
    private Drawable d;

    public InneMenuAdapter(List<Inne> inne) {
        this.inne = inne;
    }

    @Override
    public InneMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Inne i = inne.get(position);

        if (position == 0) {
            holder.header.setVisibility(View.VISIBLE);
            holder.headerText.setText(IM.context().getResources().getString(R.string.inne_menu));
        } else {
            holder.header.setVisibility(View.GONE);
        }

        holder.inneName.setText(i.getName());
        SpanUtils.on(holder.innePrize).convertToMoney(i.getPrice());

        if (i.getType() != 0) {
            holder.inneType.setVisibility(View.VISIBLE);
            if (i.getType() == 1) {
                d = PizzaStatusType.HOT.d;
            } else if (i.getType() == 2) {
                d = PizzaStatusType.NEW.d;
            } else if (i.getType() == 3) {
                d = PizzaStatusType.OUR.d;
            }

            holder.inneType.setImageDrawable(d);
        } else {
            holder.inneType.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return inne.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView inneName;
        private TextView innePrize;
        private ImageView inneType;

        private RelativeLayout header;
        private TextView headerText;


        public ViewHolder(View rootView) {
            super(rootView);
            inneName = rootView.findViewById(R.id.list_other_item_name);
            innePrize = rootView.findViewById(R.id.list_other_item_price);
            inneType = rootView.findViewById(R.id.list_other_item_status_type);

            header = rootView.findViewById(R.id.list_order_item_header);
            headerText = rootView.findViewById(R.id.list_order_item_header_text);
        }
    }
}