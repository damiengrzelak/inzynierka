package wfiis.pizzerialesna.fragments.facebookAtApp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import java.util.ArrayList;
import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customDialogs.OrderCustomDialog;
import wfiis.pizzerialesna.fragments.facebookAtApp.FbDialog;
import wfiis.pizzerialesna.model.FbPost;

public class FbPostAdapter extends RecyclerView.Adapter<FbPostAdapter.ViewHolder> {
    private List<FbPost> data;

    public FbPostAdapter(List<FbPost> data) {
        if (data.size() <= 0) {
            data = new ArrayList<>();
        }

        this.data = data;
    }


    @Override
    public FbPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FbPostAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fb_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FbPost post = data.get(position);

        holder.img.setImageDrawable(post.getImg());
        holder.text.setText(post.getText());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FbDialog ord = new FbDialog(data.get(position));
                ord.show(IM.activity().getFragmentManager(), IM.activity().getFragmentManager().getClass().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView text;
        private LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.fb_img);
            text = itemView.findViewById(R.id.fb_text);
            container = itemView.findViewById(R.id.container);
        }
    }
}
