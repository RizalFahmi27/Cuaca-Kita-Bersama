package com.android.exercise.cuacakita.custom_listener;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Rizal Fahmi on 18-Oct-16.
 */
public class LinearlayoutTouchListener implements View.OnTouchListener {

    float downY, upY;
    private Activity mainActivity;
    static final int MIN_DISTANCE = 330;

    public LinearlayoutTouchListener(Activity mainActivity){
        this.mainActivity = mainActivity;
    }

    private void onBottomToTop(float delta){
        Toast.makeText(mainActivity,"Distance : "+delta,Toast.LENGTH_SHORT).show();
    }

    private void onTopToBottom(float delta){
        Toast.makeText(mainActivity,"Distance : "+delta,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN: {
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upY = event.getY();

                float deltaY = downY - upY;

                if(Math.abs(deltaY) > MIN_DISTANCE){
                    if(deltaY < 0){
                        onBottomToTop(deltaY);
                        return true;
                    }
                    if(deltaY > 0){
                        onTopToBottom(deltaY);
                        return true;
                    }
                }
                else {
                    Log.d("Swipe","Your distance : "+Math.abs(deltaY));
                }

                return false;
            }
        }
        return false;
    }
}
