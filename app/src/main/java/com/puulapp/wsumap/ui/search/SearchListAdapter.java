package com.puulapp.wsumap.ui.search;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puulapp.wsumap.DetailView;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.ui.home.ItemListModel;
import com.puulapp.wsumap.ui.map.MapActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> implements Filterable {

    private List<ItemListModel> list;
    private List<ItemListModel> list_filtered;
    private Context context;
    private int lastPosition = -1;

    public SearchListAdapter(List<ItemListModel> list, Context context) {
        this.list = list;
        this.context = context;
        list_filtered = list;
    }

    @NonNull
    @NotNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_lists, parent, false);
        return new SearchListAdapter.ViewHolder(v);//, mListener
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchListAdapter.ViewHolder holder, int position) {
        ItemListModel spacecraft = list.get(position);

        if (!spacecraft.getImage().isEmpty()) {
            Picasso.get().load(spacecraft.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.item_image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar_list.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        holder.item_name.setText(spacecraft.getName());
        holder.item_desc.setText(spacecraft.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailView.class);
                intent.putExtra("key", spacecraft.getKey());
                context.startActivity(intent);
            }
        });

        holder.on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("lat", spacecraft.getLat());
                intent.putExtra("lng", spacecraft.getLng());
                intent.putExtra("key", spacecraft.getKey());
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null | constraint.length() == 0){
                    filterResults.count = list_filtered.size();
                    filterResults.values = list_filtered;
                } else {
                    String searchChar = constraint.toString().toLowerCase();
                    List<ItemListModel> resultData = new ArrayList<>();
                    for (ItemListModel spacecraft : list){
                        if (spacecraft.getDescription().toLowerCase().contains(searchChar)){
                            resultData.add(spacecraft);
                            Log.d("result: ", spacecraft.getName());
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<ItemListModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
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
