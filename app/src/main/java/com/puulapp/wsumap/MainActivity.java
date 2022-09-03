package com.puulapp.wsumap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.puulapp.wsumap.databinding.ActivityMainBinding;
import com.puulapp.wsumap.ui.search.SearchActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private MultiAutoCompleteTextView your_feedbck;
    private EditText your_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("intro_activity", false);
        if (!previouslyStarted){
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("intro_activity", Boolean.TRUE);
            edit.apply();
            startActivity(new Intent(this, IntroActivity.class));
        }

//        startActivity(new Intent(this, IntroActivity.class));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_otona, R.id.nav_saved)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (!checkInternetConnection()){
            showSnackBar();
        }
        
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void showSnackBar() {
        View view = findViewById(R.id.drawer_layout);
        Snackbar.make(this, view, "No Internet connection. Please enable internet data or wifi.", 6000)
                .setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.this.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                })
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            feedback();
        }
        if (id == R.id.action_search){
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    private void feedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custome_feedback_layout, null);
        builder.setView(view);
        initializeFeedbackViews(view);

        builder.setPositiveButton("Send", (dialog, which) -> {

            String feedback_message = your_feedbck.getText().toString();
            String feedback_email = your_email.getText().toString();

            if (!feedback_message.isEmpty()){

                FeedbackSpacecraft feedbackSpacecraft = new FeedbackSpacecraft(feedback_message, feedback_email);
                FirebaseDatabase.getInstance().getReference().child("feedback").push().setValue(feedbackSpacecraft).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Feedback sent! Thank you for your feedback/suggestion.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Input field can't be empty.", Toast.LENGTH_LONG).show();
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void initializeFeedbackViews(View view) {
        your_feedbck = view.findViewById(R.id.your_feedbck);
        your_email = view.findViewById(R.id.your_email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
//
//        if (id == R.id.nav_exit){
//            this.finish();
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}