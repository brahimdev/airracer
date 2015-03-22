package tk.jakakordez.airracer;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.Matrix;

/**
 * Created by Jaka on 9.3.2015.
 */
public class Waypoint {
    Mesh body;
    Vector2 location;
    float radius;
    int stage;
    float[] worldMatrix;
    public Waypoint(Mesh Body, Vector2 Location, float Radius){
        body = Body;
        location = Location;
        radius = Radius;
        worldMatrix = new float[16];
        Matrix.setIdentityM(worldMatrix, 0);
        Matrix.translateM(worldMatrix, 0, location.X, 0, location.Y);
    }

    public void Draw(GL10 gl){
        gl.glMultMatrixf(worldMatrix, 0);
        body.Draw(gl, stage);
    }

    public void Check(Vector2 player){
        if(player.Distance(location) < radius && stage == 0){
            stage = 1;
            // Todo: sound
        }
        if(player.Distance(location) < 10 && stage != 2){
            stage = 2;
        }
    }
}
