package tk.jakakordez.airracer;

import javax.microedition.khronos.opengles.GL10;

import android.media.MediaPlayer;
import android.opengl.Matrix;

/**
 * Created by Jaka on 9.3.2015.
 */
public class Waypoint {
    Vector2 location;
    float radius, rotation;
    int stage;
    int body;
    float[] worldMatrix;
    public Waypoint(int mesh, Vector2 Location, float Radius){
        location = Location;
        radius = Radius;
        worldMatrix = new float[16];
        Matrix.setIdentityM(worldMatrix, 0);
        Matrix.translateM(worldMatrix, 0, location.X, 0, location.Y);

        body = mesh;
    }

    public void ApplyRotation(float rotation){
        this.rotation = rotation;
        Matrix.rotateM(worldMatrix, 0, rotation, 0, 1, 0);

    }

    public void Draw(MeshCollection collection, GL10 gl){
        gl.glMultMatrixf(worldMatrix, 0);
        collection.Draw(body, gl, stage);
    }

    public void Check(Vector2 player, MediaPlayer waypointSound){
        if(player.Distance(location) < radius && stage == 0){
            stage = 1;
            waypointSound.start();
        }
        if(player.Distance(location) < 10 && stage != 2){
            stage = 2;
        }
    }
}
