package tk.jakakordez.airracer;

import android.content.Intent;
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


public class Finish extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView txt3 = (TextView)findViewById(R.id.textView3);
        Intent inte = getIntent();
        SharedPreferences scores = getSharedPreferences("Scores", 0);
        long time = scores.getLong("time", 0);
        int score = scores.getInt("score", 0);
        txt3.setText((inte.getLongExtra("Time", 0)>time?"New best time: ":"Time: ")+convertTime(inte.getLongExtra("Time", 0))+"\n"+(inte.getIntExtra("Score", 0)>score?"New best score: ":"Score: ")+Integer.toString(inte.getIntExtra("Score", 0)));
        SharedPreferences.Editor edit = scores.edit();
        edit.clear();
        if(inte.getLongExtra("Time", 0)>time)edit.putLong("time", inte.getLongExtra("Time", 0));
        if(inte.getIntExtra("Score", 0)>score)edit.putInt("score", inte.getIntExtra("Score", 0));
        edit.commit();
        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GameMenu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    String convertTime(long time){
        return Integer.toString((int)(time/1000/60))+" min "+Integer.toString((int)(time/1000%60))+" s";
    }

}
