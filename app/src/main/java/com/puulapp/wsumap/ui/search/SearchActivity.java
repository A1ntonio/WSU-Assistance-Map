package com.puulapp.wsumap.ui.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.ui.home.ItemListModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<ItemListModel> itemLists;
    private SearchListAdapter homeListAdapter;
    RecyclerView search_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initiate();

    }

    private void initiate() {
        search_rv = findViewById(R.id.search_rv);
        search_rv.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference home_dr = FirebaseDatabase.getInstance().getReference().child("items");

        itemLists = new ArrayList<>();

        home_dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()){
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

        homeListAdapter = new SearchListAdapter(itemLists, this);
        search_rv.setAdapter(homeListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        androidx.appcompat.widget.SearchView searchView;
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_view);
        searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeListAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}