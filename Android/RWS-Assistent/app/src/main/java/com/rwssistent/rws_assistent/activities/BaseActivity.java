package com.rwssistent.rws_assistent.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.rwssistent.rws_assistent.R;
import com.rwssistent.rws_assistent.utils.MyAdapter;

/**
 * Created by Samme on 3/08/2015.
 */
/**
 * Parent activity. Extend this activity to use the DrawerLayout
 */
public class BaseActivity extends ActionBarActivity implements OnItemClickListener {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when pressing the back button. Does what you expect + a custom animation
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    /**
     * setContentView is used by child activities to set their layout in the frameLayout.
     *
     * @param layoutResID = the ID of the corresponding layout, for example: R.id.activity_print_new
     */
    @Override
    public void setContentView(int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        setContentView(drawerLayout);
        FrameLayout frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout,
                R.mipmap.ic_drawer, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerListener);


        this.setListView();
        this.setActionBar();

        setContentView(drawerLayout);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);

    }

    /**
     * Syncs the state of the DrawerListener
     *
     * @param savedInstanceState the state of the child instance
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    /**
     * Used to determine if the button is pressed to open the drawer
     *
     * @param item = the Menu item which is selected
     * @return true if it's the DrawerListener, or super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerListener.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    /**
     * Called when an item in the ListView inside the DrawerLayout is pressed.
     *
     * @param position the position of the selected item, starts at '0'
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        selectItem(position);
        switch (position) {
            case 0:
                // Open home activity if current Activity isn't the same.
                if (!this.getClass().getSimpleName().equals("MainActivity")) {
                    this.startActivity(new Intent(this, MainActivity.class));
                    this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
                break;
        }
        //Slows down the progress to reduce drawer-lag when closing
        drawerLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                drawerLayout.closeDrawers();
            }
        }, 150);
    }


    // ============================================
    // Public Methods
    // ============================================


    public void selectItem(int position) {
        listView.setItemChecked(position, true);
    }

    // ============================================
    // Private Methods
    // ============================================
    private void setListView() {
        MyAdapter myAdapter = new MyAdapter(this);
        listView = (ListView) findViewById(R.id.left_drawer);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ADEF"));
        listView.setSelector(colorDrawable);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
    }

    private void setActionBar() {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ADEF"));
        ActionBar currentActionBar = getSupportActionBar();
        currentActionBar.setDisplayHomeAsUpEnabled(true);
        currentActionBar.setHomeButtonEnabled(true);
        currentActionBar.setBackgroundDrawable(colorDrawable);
        currentActionBar.setIcon(R.mipmap.ic_rws_white);



    }

}

