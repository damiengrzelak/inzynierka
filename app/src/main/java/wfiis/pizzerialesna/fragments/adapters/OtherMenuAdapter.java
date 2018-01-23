package wfiis.pizzerialesna.fragments.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.model.Obiad;
import wfiis.pizzerialesna.model.OtherMenuItems;
import wfiis.pizzerialesna.model.PizzaStatusType;
import wfiis.pizzerialesna.model.Salatka;
import wfiis.pizzerialesna.model.Zapiekanka;
import wfiis.pizzerialesna.tools.SpanUtils;

import static wfiis.pizzerialesna.model.OtherMenuItems.OTHER_TYPE;
import static wfiis.pizzerialesna.model.OtherMenuItems.SAL_TYPE;
import static wfiis.pizzerialesna.model.OtherMenuItems.ZAP_TYPE;

public class OtherMenuAdapter extends RecyclerView.Adapter<OtherMenuAdapter.ViewHolder> {

    private List<OtherMenuItems> data;
    private List<Obiad> obiady = new ArrayList<>();
    private List<Zapiekanka> zapiekanki = new ArrayList<>();
    private List<Salatka> salatki = new ArrayList<>();
    private Drawable d;

    public OtherMenuAdapter(List<Obiad> obiady, List<Zapiekanka> zapiekankaList, List<Salatka> salatkaList) {
        this.obiady = obiady;
        this.zapiekanki = zapiekankaList;
        this.salatki = salatkaList;
    }

    public OtherMenuAdapter(List<OtherMenuItems> otherMenuItemsList) {
        this.data = otherMenuItemsList;
    }

    @Override
    public OtherMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);
//
//        return new ViewHolder(rootView);

        View view;
        switch (viewType) {
            case OTHER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);
                return new OtherViewHolder(view);
            case SAL_TYPE:
              //  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
              //  return new EventViewHolder(view);
            case ZAP_TYPE:
               // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
              //  return new CityViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OtherMenuItems object = data.get(position);
        if (object != null) {
            switch (object.getType()) {
                case OTHER_TYPE:
                    if (position == 0) {
                        holder.header.setVisibility(View.VISIBLE);
                        holder.headerText.setText("FastFood");
                    } else {
                        holder.header.setVisibility(View.GONE);
                    }

                    holder.otherName.setText(obiady.get(position).getName());
                    holder.otherContent.setText(obiady.get(position).getIngredients());
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
                    break;
               // case EVENT_TYPE:
                  //  ((EventViewHolder) holder).mTitle.setText(object.getName());
                 //   ((EventViewHolder) holder).mDescription.setText(object.getDescription());
                 //   break;
            }
        }
//        Obiad o = obiady.get(position);
//
//        if (position == 0) {
//            holder.header.setVisibility(View.VISIBLE);
//            holder.headerText.setText("FastFood");
//        } else {
//            holder.header.setVisibility(View.GONE);
//        }
//
//        holder.otherName.setText(o.getName());
//        holder.otherContent.setText(o.getIngredients());
//        SpanUtils.on(holder.ontherPrize).convertToMoney(obiady.get(position).getPrice());
//
//        if (obiady.get(position).getType() != 0) {
//            holder.statusType.setVisibility(View.VISIBLE);
//            if (obiady.get(position).getType() == 1) {
//                d = PizzaStatusType.HOT.d;
//            } else if (obiady.get(position).getType() == 2) {
//                d = PizzaStatusType.NEW.d;
//            } else if (obiady.get(position).getType() == 3) {
//                d = PizzaStatusType.OUR.d;
//            }
//
//            holder.statusType.setImageDrawable(d);
//        } else {
//            holder.statusType.setVisibility(View.GONE);
//        }

//        Salatka z = salatki.get(position);
//
//        if (position == 0) {
//            holder.header.setVisibility(View.VISIBLE);
//            holder.headerText.setText("Sałatki");
//        } else {
//            holder.header.setVisibility(View.GONE);
//        }
//
//        holder.otherName.setText(o.getName());
//        holder.otherContent.setText(o.getIngredients());
//        SpanUtils.on(holder.ontherPrize).convertToMoney(obiady.get(position).getPrice());
//
//        if (obiady.get(position).getType() != 0) {
//            holder.statusType.setVisibility(View.VISIBLE);
//            if (obiady.get(position).getType() == 1) {
//                d = PizzaStatusType.HOT.d;
//            } else if (obiady.get(position).getType() == 2) {
//                d = PizzaStatusType.NEW.d;
//            } else if (obiady.get(position).getType() == 3) {
//                d = PizzaStatusType.OUR.d;
//            }
//
//            holder.statusType.setImageDrawable(d);
//        } else {
//            holder.statusType.setVisibility(View.GONE);
//        }
}

    @Override
    public int getItemViewType(int position) {
        if (data != null) {
            OtherMenuItems object = data.get(position);
            if (object != null) {
                return object.getType();
            }
        }
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

    private class OtherViewHolder extends ViewHolder {
        private TextView otherName;
        private TextView otherContent;
        private TextView ontherPrize;
        private ImageView statusType;

        private RelativeLayout header;
        private TextView headerText;
        public OtherViewHolder(View rootView) {
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