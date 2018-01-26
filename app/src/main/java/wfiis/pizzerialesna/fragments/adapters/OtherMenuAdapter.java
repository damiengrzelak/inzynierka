package wfiis.pizzerialesna.fragments.adapters;

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
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.enums.PizzaStatusType;
import wfiis.pizzerialesna.tools.SpanUtils;

public class OtherMenuAdapter extends RecyclerView.Adapter<OtherMenuAdapter.ViewHolder> {

    private List<Obiad> obiady = new ArrayList<>();
    private Drawable d;

    public OtherMenuAdapter(List<Obiad> obiady) {
        this.obiady = obiady;
    }

    @Override
    public OtherMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Obiad o = obiady.get(position);

        if (position == 0) {
            holder.header.setVisibility(View.VISIBLE);
            holder.headerText.setText(IM.context().getResources().getString(R.string.fast_food));
        } else {
            holder.header.setVisibility(View.GONE);
        }

        holder.otherName.setText(o.getName());
        holder.otherContent.setText(o.getIngredients());
        SpanUtils.on(holder.ontherPrize).convertToMoney(obiady.get(position).getPrice());

        if (obiady.get(position).getType() != 0) {
            holder.statusType.setVisibility(View.VISIBLE);
            if (obiady.get(position).getType() == 1) {
                d = PizzaStatusType.HOT.d;
            } else if (obiady.get(position).getType() == 2) {
                d = PizzaStatusType.NEW.d;
            } else if (obiady.get(position).getType() == 3) {
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
        return obiady.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView otherName;
        private TextView otherContent;
        private TextView ontherPrize;
        private ImageView statusType;

        private RelativeLayout header;
        private TextView headerText;


        public ViewHolder(View rootView) {
            super(rootView);
            otherName = rootView.findViewById(R.id.list_other_item_name);
            otherContent = rootView.findViewById(R.id.list_other_item_content);
            ontherPrize = rootView.findViewById(R.id.list_other_item_price);
            statusType = rootView.findViewById(R.id.list_other_item_status_type);

            header = rootView.findViewById(R.id.list_order_item_header);
            headerText = rootView.findViewById(R.id.list_order_item_header_text);
        }
    }
}