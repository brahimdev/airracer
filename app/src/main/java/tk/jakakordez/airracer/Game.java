package tk.jakakordez.airracer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Game extends Activity {
    MediaPlayer[] sounds;
    OpenGLRenderer glRenderer;
    int width;
    Point size;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onCreate(savedInstanceState);

        size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        sounds = new MediaPlayer[4];
        sounds[0] = MediaPlayer.create(this, R.raw.airplane);
        sounds[0].setLooping(true);

        sounds[1] = MediaPlayer.create(this, R.raw.finish);
        sounds[2] = MediaPlayer.create(this, R.raw.crash);
        sounds[3] = MediaPlayer.create(this, R.raw.loading);
        sounds[3].setLooping(true);
        sounds[3].start();

        GLSurfaceView view = new GLSurfaceView(this);
        glRenderer = new OpenGLRenderer(getResources().getAssets(), (SensorManager)getSystemService(SENSOR_SERVICE), sounds);
        view.setRenderer(glRenderer);
        setContentView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        glRenderer.currentWorld.player.TouchData(e, size.x);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sounds[0].stop();
        sounds[0].release();
        sounds[1].release();
        sounds[2].release();
    }
}