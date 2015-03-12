package tk.jakakordez.airracer;

/**
 * Created by Jaka on 4.3.2015.
 */
public class Material
{
    public Color4 Brush;
    public String Name, TexturePath;
    public int Texture;
    public Material(String name)
    {
        Name = name;
        Brush = new Color4();
        Texture = 0;
    }
}
