package com.tavous.eartraining.musicaleartraining;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Task
    private MediaPlayer mp;
    TextView tvNoteName;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvNoteName = (TextView) findViewById(R.id.NoteName);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Thread thread = new Thread(new ExecuteSessionRunnable());
        thread.start();

    }

    ;

    public class ExecuteSessionRunnable implements Runnable {

        @Override
        public void run() {

            InputStream session = getResources().openRawResource(R.raw.session1);

            try {
                String sessionContent = inputStreamToString(session);

                Gson gson = new Gson();
                Session s = gson.fromJson(sessionContent, Session.class);

                for (final Session.Note note : s.Note) {
                    try {

                        tvNoteName.post(new Runnable() {
                            public void run() {
                                Animation anim_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                tvNoteName.setText(note.Name);
                                tvNoteName.setBackgroundColor(Color.parseColor(GetColorCode(note.Name)));
                                tvNoteName.setTextColor(Color.parseColor("#ffffff"));
                                tvNoteName.startAnimation(anim_fade_in);

                                mp = MediaPlayer.create(MainActivity.this, GetNoteSoundId(note.Name));
                                mp.start();
                            }
                        });
                        Log.d("Ali", String.valueOf(note.Duration) + "___" + note.Name);
                        Thread.sleep(note.Duration);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

            } catch (Exception e) {
            }
        }

    }

    private String GetColorCode(String noteName) {
        String ColorCode;
        switch (noteName) {
            case "A":
                ColorCode = "#3C00FF";
                break;
            case "B":
                ColorCode = "#FF00FD";
                break;
            case "C":
                ColorCode = "#FF0000";
                break;
            case "D":
                ColorCode = "#DB7B00";
                break;
            case "E":
                ColorCode = "#E4ED00";
                break;
            case "F":
                ColorCode = "#81D700";
                break;
            case "G":
                ColorCode = "#00FFEA";
                break;
            default:
                ColorCode = "#000000";
        }

        return ColorCode;
    }

    private int GetNoteSoundId(String noteName) {
        int SoundId;
        switch (noteName) {
            case "A":
                SoundId = R.raw.a;
                break;
            case "B":
                SoundId = R.raw.b;
                break;
            case "C":
                SoundId = R.raw.c;
                break;
            case "D":
                SoundId = R.raw.d;
                break;
            case "E":
                SoundId = R.raw.e;
                break;
            case "F":
                SoundId = R.raw.f;
                break;
            case "G":
                SoundId = R.raw.g;
                break;
            default:
                SoundId = R.raw.a;
        }

        return SoundId;
    }

    private String inputStreamToString(InputStream session) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(session, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return writer.toString();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navigetion_icon) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
