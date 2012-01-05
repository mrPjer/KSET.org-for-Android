package org.kset.android;

import org.kset.android.fragments.AboutKsetFragment;
import org.kset.android.fragments.NewsListFragment;
import org.kset.android.fragments.VideoStreamFragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.ActionBar.Tab;
import android.support.v4.app.ActionBar.TabListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Window;

public class MainActivity extends FragmentActivity implements TabListener {
	private ActionBar mActionBar;
	private Resources mResources;

	private Fragment[] mFragments = new Fragment[] { new NewsListFragment(),
			new VideoStreamFragment(), new AboutKsetFragment()};

	private int[] mTabLabels = new int[] { R.string.tab_news,
			R.string.tab_stream, R.string.tab_about_kset };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		mResources = getResources();

		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (int i : mTabLabels) {
			mActionBar.addTab(mActionBar.newTab().setText(
					mResources.getString(i)).setTabListener(this));
		} 
		setContentView(R.layout.main);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.main_root,
				mFragments[tab.getPosition()]);
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		ft.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}