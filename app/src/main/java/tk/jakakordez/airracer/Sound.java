package tk.jakakordez.airracer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.*;

/**
 * Created by Jaka on 17.3.2015.
 */
public class Sound extends Service implements MediaPlayer.OnCompletionListener {
    private final IBinder mBinder = new LocalBinder();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        MediaPlayer[] mediaPlayer=new MediaPlayer[3];
        public ServiceHandler(Looper looper) {
            super(looper);
            mediaPlayer[0] = MediaPlayer.create(getApplicationContext(), R.raw.airplane);
            mediaPlayer[0].setLooping(true);

            mediaPlayer[1] = MediaPlayer.create(getApplicationContext(), R.raw.crash);
            mediaPlayer[1].setLooping(true);

            mediaPlayer[2] = MediaPlayer.create(getApplicationContext(), R.raw.finish);
            mediaPlayer[2].setLooping(true);

        }
        @Override
        public void handleMessage(Message msg) {
            play(msg.arg1);
        }
        public void stopAll()
        {
            if (mediaPlayer[0].isPlaying()) {
                mediaPlayer[0].pause();


            }
            if (mediaPlayer[1].isPlaying()) {
                mediaPlayer[1].pause();

            }
            if (mediaPlayer[2].isPlaying()) {
                mediaPlayer[2].pause();
            }

        }
        public void play(int idx)
        {
            stopAll();

            mediaPlayer[idx].start();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("Audio",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }
    public void startPlay(int idx)
    {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = idx;
        mServiceHandler.sendMessage(msg);

    }
    public class LocalBinder extends Binder {
        public Sound getService() {
            return Sound.this;
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }

    public void onDestroy() {

    }

    public void onCompletion(MediaPlayer _mediaPlayer) {
        stopSelf();
    }

}
