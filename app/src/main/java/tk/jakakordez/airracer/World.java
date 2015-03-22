package tk.jakakordez.airracer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import tk.jakakordez.airracer.util.TextRender;

/**
 * Created by Jaka on 4.3.2015.
 */
public class World {
    Airplane player;
    int scenery;
    Waypoint[] waypoints;
    float[] viewMatrix;
    Boolean crash = false;
    MeshCollection meshCollector;
    public World(AssetManager content, GL10 gl){
        GameMenu g;
        meshCollector = new MeshCollection(content);
        player = new Airplane("extra", meshCollector, gl);
        scenery = meshCollector.Import("abudhabi/abudhabi", gl);
        InitWaypoints(content, gl);
        viewMatrix = new float[16];
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

    private void InitWaypoints(AssetManager content, GL10 gl){
        waypoints = new Waypoint[10];
        Mesh wpMesh = new Mesh("cone/cone", content, gl);
        waypoints[0] = new Waypoint(wpMesh, new Vector2(2925, -2054), 20);
        waypoints[1] = new Waypoint(wpMesh, new Vector2(2263, -1619), 20);
        waypoints[2] = new Waypoint(wpMesh, new Vector2(1744, -1278), 20);
        waypoints[3] = new Waypoint(wpMesh, new Vector2(1653, -1073), 20);
        waypoints[4] = new Waypoint(wpMesh, new Vector2(1727, -902), 20);
        waypoints[5] = new Waypoint(wpMesh, new Vector2(1982, -847), 20);
        waypoints[6] = new Waypoint(wpMesh, new Vector2(2345, -1172), 20);
        waypoints[7] = new Waypoint(wpMesh, new Vector2(2544, -1446), 20);
        waypoints[8] = new Waypoint(wpMesh, new Vector2(2923, -1522), 20);
        waypoints[9] = new Waypoint(wpMesh, new Vector2(3096, -1838), 20);
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
            waypoints[i].Check(new Vector2(pos.X, pos.Z));
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, player.cameraPosition.X, player.cameraPosition.Y, player.cameraPosition.Z, pos.X, pos.Y, pos.Z, 0, 1, 0);
            waypoints[i].Draw(gl);
        }

        if(pos.Y < 2) crash = true;
    }
}
