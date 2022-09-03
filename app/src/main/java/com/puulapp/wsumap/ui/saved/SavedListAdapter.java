package com.puulapp.wsumap.ui.saved;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puulapp.wsumap.DetailView;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.database.SaveModel;
import com.puulapp.wsumap.ui.home.ItemListModel;
import com.puulapp.wsumap.ui.map.MapActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.ViewHolder> {

    private List<SaveModel> list;
    private Context context;
    private int lastPosition = -1;

    public SavedListAdapter(List<SaveModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_lists, parent, false);
        return new ViewHolder(v);//, mListener
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        SaveModel spacecraft = list.get(position);

        if (!spacecraft.getItem_image().isEmpty()) {
            Picasso.get().load(spacecraft.getItem_image()).into(holder.item_image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar_list.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        holder.item_name.setText(spacecraft.getItem_name());
        holder.item_desc.setText(spacecraft.getItem_desc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailView.class);
                intent.putExtra("key", spacecraft.getItem_key());
                context.startActivity(intent);
            }
        });

        holder.on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("lat", spacecraft.getItem_lat());
                intent.putExtra("lng", spacecraft.getItem_lng());
                intent.putExtra("key", spacecraft.getItem_key());
                context.startActivity(intent);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ImageView item_image;
        TextView item_name, item_desc;
        Button on_map;
        ProgressBar progressBar_list;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_desc = itemView.findViewById(R.id.desc);
            on_map = itemView.findViewById(R.id.on_map);
            progressBar_list = itemView.findViewById(R.id.progressBar_list);

        }
    }
}
