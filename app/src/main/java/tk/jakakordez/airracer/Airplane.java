package tk.jakakordez.airracer;

import android.content.res.AssetManager;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jaka on 4.3.2015.
 */
public class Airplane {
    private Mesh body, propeller;
    float[] worldMatrix, propellerMatrix, bodyOffset, propellerOffset;
    public Airplane(String filename, AssetManager content){
        body = new Mesh(filename+"/body", content);
        propeller = new Mesh(filename+"/propeller", content);
        worldMatrix = new float[16];
        bodyOffset = new float[16];
        propellerOffset = new float[16];
        Matrix.setIdentityM(bodyOffset, 0);

        Matrix.translateM(bodyOffset, 0, 0, 0, -20);
        Matrix.rotateM(bodyOffset, 0, 180, 0, 1, 0);
        propellerMatrix = new float[16];
        Matrix.setIdentityM(propellerOffset, 0);
        Matrix.translateM(propellerOffset, 0, 0, 0.2f, 1.95f);
    }

    public void Draw(GL10 gl){

       // gl.glLoadIdentity();
        gl.glLoadMatrixf(bodyOffset, 0);
        body.Draw(gl);
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, bodyOffset, 0, propellerOffset, 0);

        gl.glMultMatrixf(propellerOffset, 0);
        propeller.Draw(gl);


    }

    public void Controll(float[] acc){
        Matrix.rotateM(bodyOffset, 0, (-acc[0]+7)*0.5f, 1, 0, 0);
        Matrix.rotateM(bodyOffset, 0, acc[1]*0.5f, 0, 0, 1);
        Matrix.translateM(bodyOffset, 0, 0, 0, 0.05f);
        Matrix.rotateM(propellerOffset, 0, 47, 0, 0, 1);
    }

    public void SetLookAt(){

    }
}
