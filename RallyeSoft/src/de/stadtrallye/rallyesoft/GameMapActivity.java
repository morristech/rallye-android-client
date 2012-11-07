package de.stadtrallye.rallyesoft;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingMapActivity;

import de.stadtrallye.rallyesoft.fragments.ChatFragment;
import de.stadtrallye.rallyesoft.fragments.MapFragment;
import de.stadtrallye.rallyesoft.fragments.OverviewFragment;

public class GameMapActivity extends SlidingMapActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.title_maps);
		setContentView(R.layout.map);
		setBehindContentView(R.layout.dashboard_main);
		
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

//		ActionBar.Tab tab = getSupportActionBar().newTab();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setEnabled(false);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		sm.setBehindCanvasTransformer(new CanvasTransformer() {
//			public void transformCanvas(Canvas canvas, float percentOpen) {
//				float scale = (float) (percentOpen*0.25 + 0.75);
//				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
//			}
//		});
        
        ListView dashboard = (ListView) sm.findViewById(R.id.dashboard_list);
        ArrayAdapter<String> dashAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.dashboard_entries));
        dashboard.setAdapter(dashAdapter);
//        dashboard.setOnItemClickListener(this);
		
//		Context context = ab.getThemedContext();
//		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.tabs, R.layout.sherlock_spinner_item);
//        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		
		
//		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        ab.setListNavigationCallbacks(list, this);
//		ab.setSelectedNavigationItem(0);
	}
	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//		onSwitchTab(pos, id, TabEventSource.SlidingMenu);
//		getSlidingMenu().showAbove();
//		getSupportActionBar().setSelectedNavigationItem(pos);
//	}

//	@Override
//	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//		return onSwitchTab(itemPosition, itemId, TabEventSource.NavList);
//	}
	
	private enum TabEventSource { SlidingMenu, NavList };
	
//	private boolean onSwitchTab(int pos, long id, TabEventSource source) {
//		Fragment newFragment = null;
//		switch (pos) {
//		case 0: 
//			newFragment = new OverviewFragment();
//		break;
//		case 1:
//			newFragment = new MapFragment();
//		break;
//		case 3:
//			newFragment = new ChatFragment();
//			break;
//		default:
//			Toast.makeText(getApplicationContext(), getResources().getString(R.string.unsupported_link), Toast.LENGTH_SHORT).show();
//		return false;
//		}
//		
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		ft.replace(R.id.content_frame, newFragment);
//		ft.commit();
//		currentFragment = newFragment;
//		
//		return true;
//	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}