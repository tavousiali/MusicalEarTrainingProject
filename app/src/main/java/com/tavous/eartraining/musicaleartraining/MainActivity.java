package com.tavous.eartraining.musicaleartraining;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MainActivity extends BaseNavigationActivity {

    Button session1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContent(R.layout.activity_main);
        super.onCreateBase(true);
        super.onCreate(savedInstanceState);

//        session1 = (Button) findViewById(R.id.session1);
//        session1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LessonActivity.class);
//                startActivity(intent);
//            }
//        });

        List<Lesson> lessonsList = new ArrayList<>();
        try {
            String[] lessonsListName = getAssets().list("lessons");

            for (String lesson : lessonsListName) {
                Gson gson = new Gson();
                lessonsList.add(gson.fromJson(inputStreamToString(getAssets().open("lessons/" + lesson)), Lesson.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecyclerAdapterMainActivity(MainActivity.this, lessonsList));
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lesson.close();
        }

        return writer.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackClicked(new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }, "MainActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenuBase(menu, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelectedBase(item, false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onNavigationItemSelected(item, this);
        return true;
    }
}
