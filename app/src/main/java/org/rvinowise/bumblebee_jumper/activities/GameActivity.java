package org.rvinowise.bumblebee_jumper.activities;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


import org.rvinowise.bumblebee_jumper.BumblebeeEngine;

import game.engine.ads.Ads;


public class GameActivity extends Activity implements View.OnTouchListener {

    private GLSurfaceView glSurfaceView;
    private boolean renderer_set = false;

    private BumblebeeEngine engine;
    private Ads ads;


    public GameActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        engine = new BumblebeeEngine();
        engine.setContext(this);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(engine);

        setContentView(glSurfaceView);
        glSurfaceView.setOnTouchListener(this);


        //ads = new Ads(this);
        //ads.request_rewarded();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        engine.onResume();
        //ads.onResume();
        if (renderer_set) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        engine.onPause();
        //ads.onPause();
        if (renderer_set) {
            glSurfaceView.onPause();
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //ads.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        engine.change_resolution(config.screenWidthDp, config.screenHeightDp);

    }

    /*@Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.pu
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return engine.onTouch(v, event);
    }



}
