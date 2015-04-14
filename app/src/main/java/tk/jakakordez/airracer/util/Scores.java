package tk.jakakordez.airracer.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import tk.jakakordez.airracer.R;

public class Scores extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SharedPreferences scores = getSharedPreferences("Scores", 0);
        long time = scores.getLong("time", 0);
        int score = scores.getInt("score", 0);
        TextView txtTime = (TextView)findViewById(R.id.txtTime);
        TextView txtScore = (TextView)findViewById(R.id.txtScore);
        txtTime.setText(convertTime(time));
        txtScore.setText(Integer.toString(score));

        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String convertTime(long time){
        return Integer.toString((int)(time/1000/60))+" min "+Integer.toString((int)(time/1000%60))+" s";
    }
}
