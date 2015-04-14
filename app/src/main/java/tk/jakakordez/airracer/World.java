package tk.jakakordez.airracer;

import android.app.Notification;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MotionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.microedition.khronos.opengles.GL10;

import tk.jakakordez.airracer.util.TextRender;

/**
 * Created by Jaka on 4.3.2015.
 */
public class World {
    public Airplane player;
    int scenery, score;
    long startTime;
    Waypoint[] waypoints;
    float[] viewMatrix;
    Boolean crash = false, crashSoundPlayed = false;
    MeshCollection meshCollector;
    Vector2[] trees;
    int treeMesh, wpMesh;
    MediaPlayer[] sounds;
    FinishHandler appCont;
    public World(AssetManager content, GL10 gl, MediaPlayer[] sounds, String path, FinishHandler finished){
        appCont = finished;
        GameMenu g;
        meshCollector = new MeshCollection(content);
        scenery = meshCollector.Import("abudhabi/abudhabi", gl);
        player = new Airplane("extra", meshCollector, gl);
        InitWaypoints(gl, path, content);
        InitTrees(content, gl);
        viewMatrix = new float[16];
        this.sounds = sounds;
        sounds[3].stop();
        sounds[0].start();
        startTime = System.currentTimeMillis();
    }

    private void InitWaypoints(GL10 gl, String path, AssetManager content){
        try{
            ArrayList<Waypoint> wps = new ArrayList<Waypoint>();
            wpMesh = meshCollector.Import("cone/cone", gl);
            String line = "";
            InputStream iS = content.open("paths/"+path+".dat");
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));

            while ((line = reader.readLine()) != null) {
                String[] l = line.split(";");
                wps.add(new Waypoint(wpMesh, new Vector2(Float.parseFloat(l[0]), Float.parseFloat(l[1])), 25));
            }
            waypoints = new Waypoint[wps.size()];
            waypoints = wps.toArray(waypoints);
            for(int i = 0; i < waypoints.length; i++){
                float angle = 0;
                /*if(i > 0)angle +=CartesianToPolar(Vector2.Sub(waypoints.get(i).location, waypoints.get(i-1).location)).X;
                if(i < waypoints.size()-1)angle +=CartesianToPolar(Vector2.Sub(waypoints.get(i+1).location, waypoints.get(i).location)).X;
                if(i > 0 && i < waypoints.size()-1) angle /= 2;*/
                if(i < waypoints.length-1) angle = (float)Math.toDegrees(CartesianToPolar(Vector2.Sub(waypoints[i+1].location, waypoints[i].location)).X);
                else angle = waypoints[i-1].rotation;
                angle += 180;
                waypoints[i].ApplyRotation(angle);
            }
        }
        catch(IOException e){}
    }

    private void InitTrees(AssetManager content, GL10 gl){
        trees = new Vector2[11];
        treeMesh = meshCollector.Import("tree/tree", gl);
        trees[0] = new Vector2(1286, -892);
        trees[1] = new Vector2(1305, 721);
        trees[2] = new Vector2(1381, -359);
        trees[3] = new Vector2(1301, -214);
        trees[4] = new Vector2(1775, -553);
        trees[5] = new Vector2(2191, -1800);
        trees[6] = new Vector2(2290, -1832);
        trees[7] = new Vector2(2377, -1981);
        trees[8] = new Vector2(1062, -414);
        trees[9] = new Vector2(2126, -556);
        trees[10] = new Vector2(2751, -1033);
    }

    public void Draw(GL10 gl, float[] acc){
        if(!crash) player.Controll(acc);
        Vector3 pos = ExtractTranslation(player.bodyOffset);

        gl.glLoadIdentity();
        GLU.gluLookAt(gl, player.cameraPosition.X, player.cameraPosition.Y, player.cameraPosition.Z, pos.X, pos.Y, pos.Z, 0, 1, 0);
        player.Draw(gl, meshCollector);

        gl.glLoadIdentity();
        GLU.gluLookAt(gl, player.cameraPosition.X, player.cameraPosition.Y, player.cameraPosition.Z, pos.X, pos.Y, pos.Z, 0, 1, 0);

        meshCollector.Draw(scenery, gl);
        for (int i = 0; i < waypoints.length; i++){
            int t = waypoints[i].Check(new Vector2(pos.X, pos.Z), sounds[1]);
            score+=t;
            if(t == 4 && i == waypoints.length-1){
                sounds[0].stop();
                appCont.finish(System.currentTimeMillis()-startTime, score);
            }
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, player.cameraPosition.X, player.cameraPosition.Y, player.cameraPosition.Z, pos.X, pos.Y, pos.Z, 0, 1, 0);
            waypoints[i].Draw(meshCollector, gl);
        }

        for (int i = 0; i < trees.length; i++){
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, player.cameraPosition.X, player.cameraPosition.Y, player.cameraPosition.Z, pos.X, pos.Y, pos.Z, 0, 1, 0);
            gl.glTranslatef(trees[i].X, 0.5f, trees[i].Y);
            meshCollector.Draw(treeMesh, gl);
        }

        if(pos.Y < 2) {
            if(!crashSoundPlayed)sounds[2].start();
            sounds[0].stop();
            crash = true;
            crashSoundPlayed = true;

        }
    }

    public static Vector3 ExtractTranslation(float[] m){
        return new Vector3(m[12], m[13], m[14]);
    }

    public static int LoadTexture(String path, GL10 gl, AssetManager content){
        try {
            int r[] = new int[1];
            gl.glGenTextures(1, r, 0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, r[0]);

            // Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);
/*
// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_REPEAT);*/

            Bitmap bmp = BitmapFactory.decodeStream(content.open(path));

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
            return r[0];
        }
        catch(IOException e){}
        return 0;
    }

    private static Vector2 CartesianToPolar(Vector2 t)
    {
        //return new Tocka(Math.Atan(t.x / t.y), Math.Sqrt((t.x * t.x) + (t.y * t.y)));

        Vector2 a = new Vector2((float)Math.atan(-t.Y/t.X), (float)Math.sqrt((t.X * t.X) + (t.Y * t.Y)));
        if (t.X < 0)a.X += Math.PI;
        else if (t.Y < 0) a.X += Math.PI * 2;
        return a;
    }

}
