package com.amalulla.conversando;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPageAdapter  extends FragmentPagerAdapter {
 
    // List of fragments which are going to set in the view pager widget
    List<Fragment> fragments;
 
    /**
     * Constructor
     * 
     * @param fm
     *            interface for interacting with Fragment objects inside of an
     *            Activity
     */
    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }
 
    /**
     * Add a new fragment in the list.
     * 
     * @param fragment
     *            a new fragment
     */
    public void addFragment(Fragment fragment) {
       
    	 
         final Bundle args = new Bundle();
         args.putInt("number", fragments.size()+1);
         fragment.setArguments(args);
         
    	
    	this.fragments.add(fragment);
        
    }
 
    @Override
    public Fragment getItem(int arg0) {
        return this.fragments.get(arg0);
    }
 
    @Override
    public int getCount() {
        return this.fragments.size();
    }
 
}