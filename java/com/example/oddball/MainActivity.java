package com.example.oddball;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PApplet;

public class MainActivity extends AppCompatActivity
{
    public MediaPlayer themeGame;
    private PApplet sketch;
    AppDataManager appDataManager = new AppDataManager();

    public int storedHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        themeGame = MediaPlayer.create(getApplicationContext(), R.raw.thememain);
        themeGame.start();
        themeGame.setLooping(true);
        themeGame.setVolume((float)0.1, (float )0.1);

        sketch = new Sketch();
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (sketch != null)
        {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }
}