package com.example.hex_a_snake;


import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.hex_a_snake.board.HexGameControl;
import com.example.hex_a_snake.draw.HexGameSnake;

public class MainActivity extends Activity {
    private HexGameControl hexGame;
    private Runnable hideNavigation;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
       // final ConfigurationInfo config = activityManager.getDeviceConfigurationInfo();

        final HexGameSnake renderer = new HexGameSnake(this);
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        hexGame = new HexGameControl(this, renderer, size);

        hideNavigation = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                hexGame.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
            }
        };


        hexGame.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                // Note that system bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls
                    try {
                       HexGameSnake.resetControl();
                        new Handler().postDelayed(hideNavigation, 3000);
                    } catch (Exception ex){}
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                }
            }
        });

        setContentView(hexGame);
        hideNavigation.run();

        renderer.setSize(size.x, size.y);
    }

    @Override
    protected void onResume(){
        super.onResume();

        hexGame.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();

        hexGame.onPause();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus)
            hideNavigation.run();
    }
}