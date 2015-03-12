package tk.jakakordez.airracer;

import android.content.res.AssetManager;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jaka on 4.3.2015.
 */
public class Mesh {
    BufferedObject[] obj;
    ArrayList<Material> materials;
    ArrayList<Integer> indexBufferSizes;
    private FloatBuffer vertexBuffer;
    ArrayList<ShortBuffer> indexBuffer;
    public Mesh(String filename, AssetManager manager){
        materials = new ArrayList<Material>();
        try {
            String file = "";

            InputStream iS = manager.open(filename+".mesh");
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));

            file = reader.readLine();

            loadMtlFile(filename, manager);
            String[] data = file.split(";");// File.ReadAllText(filename).Split(';');
            String[] vertices = data[0].split(" ");
            //String[] texcoords = data[1].split(" ");
            float[] SortedVertices = new float[vertices.length];
           // Vector2[] SortedTextureCoordinates = new Vector2[texcoords.length/2];
            for (int i = 0; i < vertices.length; i++)
            {
                SortedVertices[i] = Float.parseFloat(vertices[i]);
            }
            /*for (int i = 0; i < texcoords.length-1; i+=2)
            {
                SortedTextureCoordinates[i / 2] = new Vector2(Float.parseFloat(texcoords[i]), Float.parseFloat(texcoords[i + 1]));
            }*/
            int bufferSize;
            indexBuffer = new ArrayList<ShortBuffer>();
            indexBufferSizes = new ArrayList<Integer>();
            //GL.GenBuffers(Materials.Length, ElementArrays);
            for (int i = 0; i < data.length-2; i++)
            {
                if (data[i + 2] != "")
                {
                    String[] indicies = data[i + 2].split(" ");
                    short[] currentElements = new short[indicies.length];
                    for (int j = 0; j < indicies.length; j++)
                    {
                        currentElements[j] = Short.parseShort(indicies[j]);
                    }
                    indexBufferSizes.add(currentElements.length);//Misc.Push<int>(currentElements.Length, ref ElementArraySizes);
                    // short is 2 bytes, therefore we multiply the number if
                    // vertices with 2.

                    ByteBuffer ibb = ByteBuffer.allocateDirect(currentElements.length * 2);
                    ibb.order(ByteOrder.nativeOrder());
                    ShortBuffer ib = ibb.asShortBuffer();
                    ib.put(currentElements);
                    ib.position(0);

                    indexBuffer.add(ib);

                }
            }

            ByteBuffer vbb = ByteBuffer.allocateDirect(SortedVertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            vertexBuffer = vbb.asFloatBuffer();
            vertexBuffer.put(SortedVertices);
            vertexBuffer.position(0);
        }
        catch(IOException e){}
    }

    public void loadMtlFile(String filename, AssetManager content)
    {
        Material currentMaterial = null;
        try {
            InputStream iS = content.open(filename+".mtl");
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));

            String ln = null;
            while ((ln = reader.readLine()) != null) {
                String[] line = ln.split(" ");
                switch (line[0]) {
                    case "newmtl":
                        if (currentMaterial != null)
                            materials.add(currentMaterial);//Misc.Push<Material>(currentMaterial, ref materials);
                        currentMaterial = new Material(line[1]);
                        break;
                    case "Kd":
                        if (currentMaterial != null) {
                            currentMaterial.Brush = new Color4(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
                        }
                        break;
               /* case "map_Kd":
                    if(currentMaterial != null)
                    {
                        String[] flnm = filename.Replace('\\', '/').Split('/');
                        String result = "";
                        for (int j = 0; j < flnm.Length - 1; j++) result += flnm[j] + "/";
                        currentMaterial.Texture = (int)Misc.LoadTexture(result + line[1], 1);
                        currentMaterial.TexturePath = result + line[1];
                    }
                    break;*/
                }
            }
            if (currentMaterial != null)materials.add(currentMaterial);//Misc.Push<Material>(currentMaterial, ref materials);
        }
        catch(IOException e){}
    }

    public void Draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        for (int i = 0; i < indexBufferSizes.size(); i++)
        {

            if (materials.get(i).Texture != 0)
            {
                gl.glColor4x(255, 255, 255, 255);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, materials.get(i).Texture);
            }
            else
            {
                materials.get(i).Brush.set(gl);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            }

            gl.glDrawElements(GL10.GL_TRIANGLES, indexBufferSizes.get(i), GL10.GL_UNSIGNED_SHORT, indexBuffer.get(i));
        }
        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }
}
