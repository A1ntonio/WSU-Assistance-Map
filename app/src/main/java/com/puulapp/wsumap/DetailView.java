package com.puulapp.wsumap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.puulapp.wsumap.database.DBAdapter;
import com.puulapp.wsumap.database.SaveModel;
import com.puulapp.wsumap.ui.map.MapActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class DetailView extends AppCompatActivity {


    private FloatingActionMenu fabMenu;
    private com.github.clans.fab.FloatingActionButton fab_share;
    private com.github.clans.fab.FloatingActionButton fab_save;
    private com.github.clans.fab.FloatingActionButton fab_map;

    private ProgressBar progressBar_detail;
    private ImageView item_photo;
    private TextView item_title, item_desc;

    String key = "", latitude = "", longitude = "", desc = "", name = "", image = "", campus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("key");
        }

        initiate();
        action();

        display();
    }

    private void display() {

        if (key != null) {
            FirebaseDatabase.getInstance().getReference().child("items").child(key)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                if (snapshot.child("name").getValue() != null) {
                                    name = snapshot.child("name").getValue().toString();
                                }
                                if (snapshot.child("photo").getValue() != null) {
                                    image = snapshot.child("photo").getValue().toString();
                                }
                                if (snapshot.child("desc").getValue() != null) {
                                    desc = snapshot.child("desc").getValue().toString();
                                }
                                if (snapshot.child("lat").getValue() != null) {
                                    latitude = snapshot.child("lat").getValue().toString();
                                }
                                if (snapshot.child("lng").getValue() != null) {
                                    longitude = snapshot.child("lng").getValue().toString();
                                }
                                if (snapshot.child("campus").getValue() != null) {
                                    campus = snapshot.child("campus").getValue().toString();
                                }

                                item_title.setText(name);
                                item_desc.setText(desc);

                                if (!image.isEmpty()) {
                                    Picasso.get().load(image).into(item_photo, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBar_detail.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    private void action() {

        fab_save.setOnClickListener(v -> {
            if (new DBAdapter(this).check_exist(key)) {
                Toast.makeText(this, "Already Saved!", Toast.LENGTH_SHORT).show();
            } else {
                SaveModel saveModel = new SaveModel(0, image, name, desc, latitude, longitude, key, campus);
                new DBAdapter(this).addNewItem(saveModel);
            }
            fabMenu.close(true);
        });
        fab_share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = image + "\n\n" + desc + "\n\n" + "http://maps.google.com/maps?saddr=" +  latitude + "," + longitude;
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, name + "(" + campus + " Campus)");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            share_item();
            fabMenu.close(true);
        });
        fab_map.setOnClickListener(v -> {
            Intent intent = new Intent(DetailView.this, MapActivity.class);
            intent.putExtra("lat", latitude);
            intent.putExtra("lng", longitude);
            intent.putExtra("key", key);
            startActivity(intent);
            fabMenu.close(true);
        });

    }

    private void share_item() {
    }

    private void initiate() {

        fab_share = findViewById(R.id.fab_share);
        fab_save = findViewById(R.id.fab_save);
        fab_map = findViewById(R.id.fab_map);
        fabMenu = findViewById(R.id.fabMenu);

        item_desc = findViewById(R.id.item_desc);
        item_photo = findViewById(R.id.item_photo);
        item_title = findViewById(R.id.item_title);

        progressBar_detail = findViewById(R.id.progressBar_detail);
    }

}