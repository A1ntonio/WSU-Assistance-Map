package com.puulapp.wsumap;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.wsumap.database.DBAdapter;
import com.puulapp.wsumap.notification.NotificationHandler;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MyService extends Service {
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground(){
        Log.d("notification: ", "started");
        prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);

        boolean next_item = prefs.getBoolean("item_key_added", true);
        if (next_item) {
            edit = prefs.edit();
            edit.putBoolean("item_key_added", false);
            edit.apply();
            add_item();
            Log.d("notification: ", "not entered");
        }

        boolean previouslyStarted = prefs.getBoolean("notification", true);
        if (previouslyStarted){
            Log.d("notification: ", "started1");
            notification();
        }

    }

    private void add_item() {
        FirebaseDatabase.getInstance().getReference()
                .child("items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                new DBAdapter(MyService.this).addNewKey(snapshot.getKey());

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
    }

    private void notification() {
        FirebaseDatabase.getInstance().getReference()
                .child("items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                if (!(new DBAdapter(MyService.this).check_key_exist(snapshot.getKey()))){
                    Log.d("notification: ", "not exist");
                    new DBAdapter(MyService.this).addNewKey(snapshot.getKey());
                    if (snapshot.exists()){
                        String name = snapshot.child("name").getValue().toString();
                        String image = snapshot.child("photo").getValue().toString();
                        String campus = snapshot.child("campus").getValue().toString();

                        Random random = new Random();
                        int id = random.nextInt(9999);

                        new NotificationHandler(MyService.this).notification(name, image, campus, snapshot.getKey(), id);
                    }

                } else {
                    Log.d("notification: ", "Exist");
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
    }

}