package com.lightsoft.microwave.lightmanager;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lightsoft.microwave.lightmanager.fragments.RuleMakerFragment;
import com.lightsoft.microwave.lightmanager.fragments.RuleViewerFragment;
import com.lightsoft.microwave.lightmanager.visualize.SlideTabChangedListener;


public class RuleEditorActivity extends FragmentActivity implements RuleMakerFragment.OnFragmentInteractionListener, RuleViewerFragment.OnFragmentInteractionListener {
    FragmentTabHost ftb;
    SlideTabChangedListener slide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_editor);
        ftb = (FragmentTabHost) findViewById(R.id.tabHost);
        Log.i("myinfo", "TabHost finded");
        ftb.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        ftb.addTab(ftb.newTabSpec("maker").setIndicator("Maker"), RuleMakerFragment.class, null);
        ftb.addTab(ftb.newTabSpec("viewer").setIndicator("Viewer"), RuleViewerFragment.class, null);
        slide = new SlideTabChangedListener(this, ftb);
        ftb.setOnTabChangedListener(slide);

    }

    public SlideTabChangedListener getSlide() {
        return slide;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rule_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
