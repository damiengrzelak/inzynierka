package wfiis.pizzerialesna.fragments.imprezy.adapter;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.inverce.mod.core.IM;

import java.util.List;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customDialogs.ImageDialog;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> data;

    public ImageAdapter(List<String> imagesList) {
        this.data = imagesList;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = data.get(position);

        Glide.with(IM.context())
                .load(item)
                .fitCenter()
                .into(holder.imgContainer);

        holder.imgContainer.setOnClickListener(view -> {
            ImageDialog imageDialog = new ImageDialog(item);
            imageDialog.show(IM.activity().getFragmentManager(), IM.activity().getFragmentManager().getClass().toString());


        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgContainer;

        public ViewHolder(View rootView) {
            super(rootView);
            imgContainer = rootView.findViewById(R.id.image_container);
        }
    }

}
