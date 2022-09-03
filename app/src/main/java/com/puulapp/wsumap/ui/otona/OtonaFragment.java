package com.puulapp.wsumap.ui.otona;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.databinding.FragmentHomeBinding;
import com.puulapp.wsumap.databinding.FragmentOtonaBinding;
import com.puulapp.wsumap.ui.home.HomeListAdapter;
import com.puulapp.wsumap.ui.home.ItemListModel;
import com.puulapp.wsumap.ui.map.MapActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OtonaFragment extends Fragment {

    private List<ItemListModel> itemLists;
    private HomeListAdapter homeListAdapter;

    private FloatingActionButton fab_1;

    private FragmentOtonaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtonaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initiate(root);

        return root;
    }

    private void initiate(View root) {

        fab_1 = root.findViewById(R.id.fab_1);
        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("lat", "6.860148693855833");
                intent.putExtra("lng", "37.7793925050146");
                intent.putExtra("key", "No");
                startActivity(intent);
            }
        });
        RecyclerView otona_rv = root.findViewById(R.id.otona_rv);
        otona_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference home_dr = FirebaseDatabase.getInstance().getReference().child("items");

        itemLists = new ArrayList<>();

        home_dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()){
                    if (snapshot.child("campus").getValue() != null){
                        if (snapshot.child("campus").getValue().toString().equals("Otona")){
                            String name = "", image = "", description = "", lat = "", lng = "";
                            if (snapshot.child("name").getValue() != null) {
                                name = snapshot.child("name").getValue().toString();
                            }
                            if (snapshot.child("photo").getValue() != null) {
                                image = snapshot.child("photo").getValue().toString();
                            }
                            if (snapshot.child("desc").getValue() != null) {
                                description = snapshot.child("desc").getValue().toString();
                            }
                            if (snapshot.child("lat").getValue() != null) {
                                lat = snapshot.child("lat").getValue().toString();
                            }
                            if (snapshot.child("lng").getValue() != null) {
                                lng = snapshot.child("lng").getValue().toString();
                            }

                            ItemListModel itemListModel = new ItemListModel(name, image, description, lat, lng, snapshot.getKey());

                            itemLists.add(itemListModel);

                            homeListAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        homeListAdapter = new HomeListAdapter(itemLists, getContext());
        otona_rv.setAdapter(homeListAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}