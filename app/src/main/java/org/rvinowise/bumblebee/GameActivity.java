package org.rvinowise.bumblebee;

import android.app.Activity;
import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


import game.engine.Engine;
import game.engine.ads.Ads;

import static android.view.MotionEvent.ACTION_DOWN;


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
        engine.getViewport().set_view_dimension(
                config.screenWidthDp, config.screenHeightDp);
    }

    /*@Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.pu
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==ACTION_DOWN) {
            //ads.can_show_rewarded();
            return true;
        }
        return false;
    }



}
