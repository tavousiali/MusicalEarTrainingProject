package com.tavous.eartraining.musicaleartraining;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import java.util.concurrent.Callable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LessonActivity extends BaseNavigationActivity {

    //Task
    private MediaPlayer mp;
    TextView tvNoteName;
    Thread thread;
    Button start, stop;
    boolean isStop = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContent(R.layout.activity_session);
        super.onCreateBase(false);
        super.onCreate(savedInstanceState);

        tvNoteName = (TextView) findViewById(R.id.NoteName);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    public class ExecuteLessonRunnable implements Runnable {

        @Override
        public void run() {

            InputStream lesson = null;
            try {
                lesson = getAssets().open("lessons/lesson1.json");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String lessonContent = inputStreamToString(lesson);

                Gson gson = new Gson();
                Lesson s = gson.fromJson(lessonContent, Lesson.class);

                String[] noteList = s.NoteList.split(",");
                final int numberOfFirstShowNotes = s.NumberOfFirstShowNotes;
                int i = 0;
                for (final String note : noteList) {
                    if (!isStop) {
                        try {

                            final int finalI = i;
                            tvNoteName.post(new Runnable() {
                                public void run() {
                                    Animation anim_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

                                    if (finalI < numberOfFirstShowNotes)
                                        tvNoteName.setText(GetNoteText(note));
                                    else
                                        tvNoteName.setText("");
                                    tvNoteName.setBackgroundColor(Color.parseColor(GetColorCode(note)));
                                    tvNoteName.setTextColor(Color.parseColor("#ffffff"));
                                    tvNoteName.startAnimation(anim_fade_in);

                                    if (mp != null) {
                                        mp.release();
                                        mp = null;
                                    }
                                    mp = MediaPlayer.create(LessonActivity.this, GetNoteSoundId(note));
                                    mp.start();
                                }
                            });

                            i++;
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                stop();
            } catch (Exception e) {
            }
        }

    }

    private String GetColorCode(String noteName) {
        String ColorCode;
        switch (noteName) {
            case "A":
            case "a":
                ColorCode = "#3C00FF";
                break;
            case "B":
            case "b":
                ColorCode = "#FF00FD";
                break;
            case "C":
            case "c":
            case "C2":
            case "c2":
                ColorCode = "#FF0000";
                break;
            case "D":
            case "d":
                ColorCode = "#DB7B00";
                break;
            case "E":
            case "e":
                ColorCode = "#E4ED00";
                break;
            case "F":
            case "f":
                ColorCode = "#81D700";
                break;
            case "G":
            case "g":
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
            case "a":
                SoundId = R.raw.a;
                break;
            case "B":
            case "b":
                SoundId = R.raw.b;
                break;
            case "C":
            case "c":
                SoundId = R.raw.c;
                break;
            case "D":
            case "d":
                SoundId = R.raw.d;
                break;
            case "E":
            case "e":
                SoundId = R.raw.e;
                break;
            case "F":
            case "f":
                SoundId = R.raw.f;
                break;
            case "G":
            case "g":
                SoundId = R.raw.g;
                break;
            case "C2":
            case "c2":
                SoundId = R.raw.c2;
                break;
            default:
                SoundId = R.raw.a;
        }

        return SoundId;
    }

    private String GetNoteText(String noteName) {
        String NoteText;
        switch (noteName) {
            case "A":
            case "a":
                NoteText = "A";
                break;
            case "B":
            case "b":
                NoteText = "B";
                break;
            case "C":
            case "c":
            case "C2":
            case "c2":
                NoteText = "C";
                break;
            case "D":
            case "d":
                NoteText = "D";
                break;
            case "E":
            case "e":
                NoteText = "E";
                break;
            case "F":
            case "f":
                NoteText = "F";
                break;
            case "G":
            case "g":
                NoteText = "G";
                break;
            default:
                NoteText = "A";
        }

        return NoteText;
    }

    private String inputStreamToString(InputStream lesson) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(lesson, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lesson.close();
        }

        return writer.toString();
    }

    @Override
    public void onBackPressed() {
        isStop = true;

        super.onBackClicked(new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenuBase(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        isStop = true;

        return super.onOptionsItemSelectedBase(item, true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onNavigationItemSelected(item, this);
        return true;
    }

    private void start() {
        thread = new Thread(new ExecuteLessonRunnable());
        thread.start();

        start.post(new Runnable() {
            public void run() {
                start.setVisibility(View.GONE);
            }
        });
        stop.post(new Runnable() {
            public void run() {
                stop.setVisibility(View.VISIBLE);
            }
        });
    }

    public void stop() {
        isStop = true;
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
