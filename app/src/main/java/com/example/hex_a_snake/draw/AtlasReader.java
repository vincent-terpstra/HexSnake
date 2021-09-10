package com.example.hex_a_snake.draw;

import android.content.Context;

import com.example.hex_a_snake.FileHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AtlasReader {
    public AtlasReader(Context context, int resourceID){
        this.regions = new HashMap<>();
        BufferedReader bufferedReader = FileHelper.createBufferedReader(context, resourceID);
                new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceID)));
        String nextLine;

        try
        {
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                regions.put(nextLine, new TextureRegion(nextLine, bufferedReader.readLine().substring(6)));
            }

            bufferedReader.close();
        }
        catch (IOException e) {
            throw new RuntimeException("ERROR reading Atlas");
        }
    }

    private final Map<String, TextureRegion> regions;


    class TextureRegion{
        public final String name;
        public final float u, u2, v, v2;
        public TextureRegion(String name, String region){
            this.name = name;
            final float imageWidth = 1024f;
            String[] values = region.split(", ");

            u  = Integer.parseInt(values[0]) / imageWidth;
            v = Integer.parseInt(values[1]) / imageWidth;
            u2 = u + Integer.parseInt(values[2]) / imageWidth;
            v2 = v + Integer.parseInt(values[3]) / imageWidth;
        }
    }

    private TextureRegion getRegion(String name){
        return regions.get(name);
    }

    public float [] CreateCoordinates(String name, float dx, float dy){

        TextureRegion textureRegion = this.getRegion(name);
        float u = textureRegion.u;
        float v = textureRegion.v;
        float u2 = textureRegion.u2;
        float v2 = textureRegion.v2;

        return new float[]{
                dx,  dy, u, v,
                dx, -dy, u, v2,
                -dx, -dy, u2, v2,
                -dx,  dy, u2, v
        };
    }

}
