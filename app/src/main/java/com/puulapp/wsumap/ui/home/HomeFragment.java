package com.puulapp.wsumap.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.wsumap.DetailView;
import com.puulapp.wsumap.MainActivity;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.databinding.FragmentHomeBinding;
import com.puulapp.wsumap.ui.map.MapActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<ItemListModel> itemLists;
    private HomeListAdapter homeListAdapter;

    private FloatingActionButton fab;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initiate(root);

        return root;
    }

    private void initiate(View root) {

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("lat", "6.829969159424438");
                intent.putExtra("lng", "37.75164882536292");
                intent.putExtra("key", "No");
                startActivity(intent);
            }
        });
        RecyclerView home_rv = root.findViewById(R.id.home_rv);
        home_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference home_dr = FirebaseDatabase.getInstance().getReference().child("items");

        itemLists = new ArrayList<>();

        home_dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()){
                    if (snapshot.child("campus").getValue() != null){
                        if (snapshot.child("campus").getValue().toString().equals("Gandaba")){
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
        home_rv.setAdapter(homeListAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}