package com.example.hex_a_snake;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
    public static String readTextFileFromRawResource(
            final Context context,
            final int resourceId )
    {
        BufferedReader bufferedReader = createBufferedReader(context, resourceId);
        String nextLine;
        final StringBuilder body = new StringBuilder();

        try
        {
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                body.append(nextLine);
                body.append('\n');
            }

            bufferedReader.close();
        }
        catch (IOException e) {
            return null;
        }

        return body.toString();
    }

    public static BufferedReader createBufferedReader(Context context, int resourceID){
        return new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceID)));
    }
}
