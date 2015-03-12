package tk.jakakordez.airracer;

/**
 * Created by Jaka on 4.3.2015.
 */
public class Vector2 {
    public float X, Y;

    public Vector2(float X, float Y){
        this.X = X;
        this.Y = Y;
    }

    public static Vector2 Zero(){
        return new Vector2(0, 0);
    }

    public float Distance(Vector2 v1){
        return (float) Math.sqrt(Math.pow(X-v1.X, 2)+Math.pow(Y-v1.Y, 2));
    }
}
