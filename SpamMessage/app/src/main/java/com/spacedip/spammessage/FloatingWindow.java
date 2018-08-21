package com.spacedip.spammessage;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Nabeel on 8/21/2018.
 */

public class FloatingWindow extends Service {

    private WindowManager wm;
    private LinearLayout ll;
    private Button stop;
    private Button touch;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        stop = new Button(this);
        touch = new Button(this);

        ViewGroup.LayoutParams stopParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        stop.setText("Stop");
        stop.setLayoutParams(stopParams);

        ViewGroup.LayoutParams touchParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        touch.setText("Touch");
        touch.setLayoutParams(touchParams);

        LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setBackgroundColor(Color.argb(66,256,00,0));
        ll.setLayoutParams(llParameters);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(400,150,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        parameters.x = 0;
        parameters.y = 0;

        parameters.gravity = Gravity.CENTER | Gravity.CENTER;

        ll.addView(stop);
        ll.addView(touch);
        wm.addView(ll,parameters);

        ll.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams updatedParameters = parameters;
            int x,y;
            float touchedX, touchedY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        touchedX = motionEvent.getRawX();
                        touchedY = motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x =  (int) (x + (motionEvent.getRawX() - touchedX));
                        updatedParameters.y =  (int) (y + (motionEvent.getRawY() - touchedY));

                        wm.updateViewLayout(ll,updatedParameters);

                        break;
                }
                return false;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wm.removeView(ll);
                stopSelf();
            }
        });
    }
}
