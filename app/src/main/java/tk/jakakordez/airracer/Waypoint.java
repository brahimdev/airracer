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
    Boolean done;
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

        gl.glLoadIdentity();
        gl.glLoadMatrixf(worldMatrix, 0);
        body.Draw(gl);
    }

    public void Check(Vector2 player){
        if(player.Distance(location) < radius){
            done = true;
            // Todo: sound
        }
    }
}
