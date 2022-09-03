package com.puulapp.wsumap.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.puulapp.wsumap.DetailView;
import com.puulapp.wsumap.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class NotificationHandler {
    private Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public NotificationHandler(Context context){
        this.context = context;
    }

public void notification(String name, String image, String campus, String key, int id) {
    Log.d("notification: ", "notification sent");
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.mipmap.ic_launcher_round);
                builder.setColor(context.getResources().getColor(R.color.colorPrimary));
                builder.setContentTitle(name);
                builder.setContentText("New location added in " + campus +" campus. Tap this to view detail.");

                prefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean previouslyStarted = prefs.getBoolean("vibration", true);
                if (previouslyStarted){
                    builder.setVibrate(new long[]{1000, 1000});
                }

                builder.setLights(Color.RED, 3000, 3000);
                boolean previouslyStarted1 = prefs.getBoolean("sound", true);
                if (previouslyStarted1){
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(alarmSound);
                }
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                Intent intent = new Intent(context, DetailView.class);
                intent.putExtra("key", key);

                builder.setContentIntent(PendingIntent.getActivity(context, id, intent, 0));

                if (!image.isEmpty()) {
                    Picasso.get().load(image).placeholder(R.drawable.logo).networkPolicy(NetworkPolicy.OFFLINE).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            builder.setLargeIcon(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                }
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    // notificationId is a unique int for each notification that you must define

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        Log.d("notification: ", "version changed");
        String channelId = String.valueOf(id);
        NotificationChannel channel = new NotificationChannel(
                channelId,
                name,
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        builder.setChannelId(channelId);
    }

    notificationManager.notify(id, builder.build());


    }

}
