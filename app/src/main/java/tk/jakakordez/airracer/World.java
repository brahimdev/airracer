package tk.jakakordez.airracer;

import android.content.res.AssetManager;
import android.opengl.GLU;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jaka on 4.3.2015.
 */
public class World {
    Airplane player;
    Mesh scenery;
    Waypoint[] waypoints;
    float[] viewMatrix;
    private Mesh stat;
    public World(AssetManager content){
        player = new Airplane("extra", content);
        //scenery = new Mesh("mesh/abudhabi", content);
        InitWaypoints(content);
        viewMatrix = new float[16];
        Matrix.setIdentityM(m, 0);
        stat = new Mesh("extra/body", content);
        m = new float[16];
        Matrix.setIdentityM(m, 0);
        Matrix.translateM(m, 0, 10, 0, -50);
        j = new float[16];
        Matrix.setIdentityM(j, 0);
    }

    private void InitWaypoints(AssetManager content){
        waypoints = new Waypoint[8];
        Mesh wpMesh = new Mesh("cone/cone", content);
        waypoints[0] = new Waypoint(wpMesh, new Vector2(10, 10), 5);
        waypoints[1] = new Waypoint(wpMesh, new Vector2(10, 20), 5);
        waypoints[2] = new Waypoint(wpMesh, new Vector2(10, 30), 5);
        waypoints[3] = new Waypoint(wpMesh, new Vector2(10, 40), 5);
        waypoints[4] = new Waypoint(wpMesh, new Vector2(10, 50), 5);
        waypoints[5] = new Waypoint(wpMesh, new Vector2(10, 60), 5);
        waypoints[6] = new Waypoint(wpMesh, new Vector2(10, 70), 5);
        waypoints[7] = new Waypoint(wpMesh, new Vector2(10, 80), 5);
    }
float f;
    float[] m, j;
    public void Draw(GL10 gl, float[] acc){
        gl.glLoadIdentity();
        //Matrix.invertM(viewMatrix, 0, player.worldMatrix, 0);
        //GLU.gluLookAt(gl, 0, 50, 0, 0, 0, 0, 0, 1, 0);
        player.Controll(acc);
        //gl.glLoadMatrixf(viewMatrix, 0);
        player.Draw(gl);
        f++;
        gl.glLoadIdentity();
        //Matrix.invertM(j, 0, player.worldMatrix, 0);
        gl.glMultMatrixf(m, 0);

        stat.Draw(gl);
        //scenery.Draw(gl);
       /* for (int i = 0; i < waypoints.length; i++){
            gl.glLoadMatrixf(viewMatrix, 0);
            waypoints[i].Draw(gl);
        }*/
    }
}
