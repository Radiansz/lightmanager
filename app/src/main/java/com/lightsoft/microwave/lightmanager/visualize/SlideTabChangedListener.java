package com.lightsoft.microwave.lightmanager.visualize;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by lightwave on 06.08.15.
 */
public class SlideTabChangedListener implements TabHost.OnTabChangeListener {

    Context cnt;
    TabHost host;
    GestureDetector gestureDetector;

    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public SlideTabChangedListener(Context cnt, TabHost host){
        this.host = host;
        this.cnt = cnt;
        gestureDetector = new GestureDetector(cnt, new SlideGestureDetector() );
        host.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !gestureDetector.onTouchEvent(event);

            }
        });

    }

    @Override
    public void onTabChanged(String tabId) {

    }

    class SlideGestureDetector extends GestureDetector.SimpleOnGestureListener{

        int maxTabs;

        public SlideGestureDetector(){
            maxTabs = host.getChildCount();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int curTab = host.getCurrentTab();
            float absX = Math.abs(velocityX);
            if(Math.abs(velocityY) < absX && absX > 30){
                if(velocityX/absX > 0){
                    curTab += 1;
                }
                else{
                    curTab -=1;
                }
            }
            if(curTab > maxTabs)
                curTab = maxTabs;
            else if (curTab < 0)
                curTab = 0;
            host.setCurrentTab(curTab);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
