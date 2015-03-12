package tk.jakakordez.airracer;

import java.util.Vector;

/**
 * Created by Jaka on 4.3.2015.
 */
public class Vector3 {
    public float X, Y, Z;

    public Vector3(float X, float Y, float Z){
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public Vector3(){}

    public static Vector3 Zero(){
        return new Vector3(0, 0, 0);
    }
}
