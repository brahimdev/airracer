package tk.jakakordez.airracer;

import android.content.res.AssetManager;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jaka on 4.3.2015.
 */
public class Airplane {
    int body, propeller;
    float[] bodyOffset, propellerOffset, cameraOffset;
    Vector3 cameraPosition;
    public Airplane(String filename, MeshCollection content, GL10 gl){
        body = content.Import(filename+"/body", gl);
        propeller = content.Import(filename+"/propeller", gl);

        bodyOffset = new float[16];
        Matrix.setIdentityM(bodyOffset, 0);
        Matrix.translateM(bodyOffset, 0, 0, 100, -20);
        Matrix.rotateM(bodyOffset, 0, 180, 0, 1, 0);

        propellerOffset = new float[16];
        Matrix.setIdentityM(propellerOffset, 0);
        Matrix.translateM(propellerOffset, 0, 0, 0.2f, 1.95f);

        cameraOffset = new float[16];
        Matrix.setIdentityM(cameraOffset, 0);
        Matrix.translateM(cameraOffset, 0, 0, 1, -10);
    }

    public void Draw(GL10 gl, MeshCollection content){
        gl.glMultMatrixf(bodyOffset, 0);
        content.Draw(body, gl);

        gl.glMultMatrixf(propellerOffset, 0);
        content.Draw(propeller, gl);
    }

    public void Controll(float[] acc){

        Matrix.rotateM(bodyOffset, 0, (-acc[0]+6)*0.5f, 1, 0, 0);
        Matrix.rotateM(bodyOffset, 0, acc[1]*0.5f, 0, 0, 1);
        Matrix.rotateM(bodyOffset, 0, acc[1]*0.05f, 1, 0, 0);
        Matrix.translateM(bodyOffset, 0, 0, 0, 0.8f);
        Matrix.rotateM(propellerOffset, 0, 47, 0, 0, 1);

        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, bodyOffset, 0, cameraOffset, 0);
        cameraPosition = World.ExtractTranslation(temp);
    }

    public void SetLookAt(){

    }
}
