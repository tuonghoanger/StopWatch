package com.bignerdranch.android.pendulum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    ImageButton start, laptime;
    //Number of centiseconds displayed on the stopwatch.
    private int centisecs = 0;
    //Is the stopwatch running?
    private boolean running, isResume;
    //Display Lap time
    RecyclerView recyclerView;
    TextAdapter setAdapter;
    //list of lap time
    List<String> listText = new ArrayList<>();
    private Chronometer timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTimer();

        start = findViewById(R.id.start_button);
        laptime = findViewById(R.id.reset_button);
        laptime.setVisibility(View.GONE);
        //Start the stopwatch running when the Start button is clicked.
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    running = true;
                    laptime.setImageDrawable(getResources().getDrawable(R.drawable.ic_laptime));
                    start.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    laptime.setVisibility(View.VISIBLE);
                }     //Stop the stopwatch running when the Pause button is clicked
                else {
                    running = false;
                    start.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                    laptime.setImageDrawable(getResources().getDrawable(R.drawable.ic_reset));
                }
            }
        });
        //Reset the stopwatch when the Reset button is clicked.
        laptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    Toast.makeText(MainActivity.this, "TRUST ME MAN!", Toast.LENGTH_SHORT).show();
                    centisecs = 0;
                    laptime.setVisibility(View.GONE);
                    listText.clear();
                    updateUI();
                } else {
                    String string = timeView.getText().toString();
                    listText.add(string);
                    updateUI();
                }
            }
        });

        //set view with lap time
        recyclerView = findViewById(R.id.lap_time);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();

    }

    //update UI of list
    public void updateUI() {
        if (setAdapter == null) {
            setAdapter = new TextAdapter(listText);
            recyclerView.setAdapter(setAdapter);
        } else {
            setAdapter.setList(listText);
            setAdapter.notifyDataSetChanged();
        }
    }

    //Sets the number of centiseconds on the timer.
    private void runTimer() {
        timeView = (Chronometer) findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (centisecs % 360000) / 6000;
                int secs = (centisecs % 6000) / 100;
                int centisec = (centisecs % 100);
                String time = String.format(Locale.getDefault(), "%02d:%02d,%02d", minutes, secs, centisec);
                timeView.setText(time);
                if (running) {
                    centisecs++;
                }
                handler.postDelayed(this, 10);
            }
        });
    }
//set up viewholder 
    public class TextHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextHolder(TextView v) {
            super(v);
            textView = v;
        }

        public void setText(String pos) {
            textView.setText(pos);
        }
    }
//set up adapter for recyclerview
    public class TextAdapter extends RecyclerView.Adapter<TextHolder> {
        List<String> listAdapter = new ArrayList<>();

        public TextAdapter(List<String> list) {
            listAdapter = list;
        }

        @NonNull
        @Override
        public TextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            float f = 37.97f;
            TextView text = new TextView(MainActivity.this);
            text.setTextSize(f);
            return new TextHolder(text);
        }

        @Override
        public void onBindViewHolder(@NonNull TextHolder holder, int position) {
            String string = "Lap " + (position + 1) + " : " + listAdapter.get(position);
            holder.setText(string);
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        public void setList(List<String> list) {
            listAdapter = list;
        }
    }
}
