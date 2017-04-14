package game.engine;


import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class Human_control {

    private boolean touched = false;

    Human_control() {

    }


    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==ACTION_DOWN) {
            touched = true;
            return true;
        } else if (event.getAction()==ACTION_UP) {
            touched = false;
            return true;
        }
        return false;
    }

    public boolean isTouched() {
        return touched;
    }
}
