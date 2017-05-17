package game.engine;


public enum Fps_counter {
    ;

    private static final float etalon_framerate = 0.033f;
    //private static final float etalon_framerate = 0.088f;

    static final float fastest_framerate = 0.005f;

    static float last_physics_step_moment;
    static float last_delay = 0;
    static float time_since_last_step = 0;
    static float step_multiplier = 1;
    static float last_drawing_step_moment;
    static boolean next_step_needed = false;



    static public void drawing_step() {
        last_drawing_step_moment = System.nanoTime();
        time_since_last_step = (last_drawing_step_moment - last_physics_step_moment) / 1000000000f;
        if (time_since_last_step > fastest_framerate) {
            next_step_needed = true;
        }
    }

    static public float getStep_multiplier() {
        return step_multiplier;
    }

    static public boolean its_time_for_next_step() {
        return next_step_needed;
    }

    static public void physics_step() {
        next_step_needed = false;
        last_physics_step_moment = last_drawing_step_moment;
        step_multiplier = time_since_last_step / etalon_framerate;
    }

    static public void register_first_step_as_done() {
        last_physics_step_moment = System.nanoTime();
    }
}
