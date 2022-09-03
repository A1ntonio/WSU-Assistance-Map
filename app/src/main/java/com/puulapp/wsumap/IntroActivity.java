package com.puulapp.wsumap;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation((view, percentage) -> view.setAlpha(percentage));
        addSlide(new SlideFragmentBuilder().backgroundColor(R.color.colorAccent)
                .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.INTERNET})
                .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_NETWORK_STATE
                        , Manifest.permission.ACCESS_COARSE_LOCATION})
                .buttonsColor(R.color.teal_200)
                .image(R.drawable.findyou)
                .title("FIND")
                .description("Find supporting information and location of cafe, dorm, office, parks, shops, toilets and buildings or facilities around Campus.")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimaryDark)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.findyou)
                .title("Navigate")
                .description("Navigate to where you are looking for.")
                .build());

    }
}
