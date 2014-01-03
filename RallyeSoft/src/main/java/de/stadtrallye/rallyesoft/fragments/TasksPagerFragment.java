package de.stadtrallye.rallyesoft.fragments;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TitlePageIndicator;

import java.util.List;
import java.util.Map;

import de.rallye.model.structures.Submission;
import de.stadtrallye.rallyesoft.R;
import de.stadtrallye.rallyesoft.common.Std;
import de.stadtrallye.rallyesoft.model.IModel;
import de.stadtrallye.rallyesoft.model.ITasks;
import de.stadtrallye.rallyesoft.uimodel.IModelActivity;
import de.stadtrallye.rallyesoft.uimodel.ITasksMapControl;
import de.stadtrallye.rallyesoft.uimodel.TaskPagerAdapter;

import static de.stadtrallye.rallyesoft.uimodel.Util.getDefaultMapOptions;

/**
 * Fragment to show the details of tasks, similar to the mail-view in GMail
 * Enhanced by a GMap
 */
public class TasksPagerFragment extends Fragment implements ITasks.ITasksListener {

	private static final String THIS = TasksPagerFragment.class.getSimpleName();

	private IModel model;
	private ViewPager pager;
	private TaskPagerAdapter fragmentAdapter;
	private ITasks tasks;
//	private ITasksMapControl mapControl;
	private TitlePageIndicator indicator;
	private byte size = 0;
	private ViewGroup details;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(THIS, "we are in "+getClass().getName());
		Log.v(THIS, "got result: " + resultCode);
		if (resultCode== Activity.RESULT_OK) {

			Log.v(THIS, "need to update submissions");
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@TargetApi(11)
	private void setLayoutTransition(ViewGroup vg) {
		vg.setLayoutTransition(new LayoutTransition());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tasks_pager, container, false);

		details = (ViewGroup) v.findViewById(R.id.task_details);
		pager = (ViewPager) v.findViewById(R.id.tasks_pager);
		indicator = (TitlePageIndicator) v.findViewById(R.id.pager_indicator);

		pager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin));

		if (android.os.Build.VERSION.SDK_INT >= 11)
			setLayoutTransition((ViewGroup) v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		try {
			model = ((IModelActivity) getActivity()).getModel();
			tasks = model.getTasks();
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString() + " must implement IModelActivity");
		}

		FragmentManager fm = getChildFragmentManager();
		Fragment mapFragment = fm.findFragmentByTag(TasksMapFragment.TAG);
		if (mapFragment == null) {

			mapFragment = new TasksMapFragment();

			Bundle args = getDefaultMapOptions(model);
			args.putBoolean(Std.TASK_MAP_MODE_SINGLE, true);
			mapFragment.setArguments(args);
			fm.beginTransaction().replace(R.id.map, mapFragment, TasksMapFragment.TAG).commit();
		}

		ITasksMapControl mapControl = (ITasksMapControl) mapFragment;

		fragmentAdapter = new TaskPagerAdapter(getChildFragmentManager(), getActivity(), tasks.getTasksCursor(), mapControl);
		pager.setAdapter(fragmentAdapter);
		indicator.setViewPager(pager); // Needs an adapter

		Bundle args = getArguments();
		if (args != null) {
			int id = args.getInt(Std.TASK_ID, -1);
			int tab = (id >= 0) ? tasks.getTaskPositionInCursor(id) : 0;
			pager.setCurrentItem(tab);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		tasks.addListener(this);
		setSize(size);
	}

	@Override
	public void onStop() {
		super.onStop();

		tasks.removeListener(this);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem resize = menu.add(Menu.NONE, R.id.resize_menu, 40, R.string.resize);
		resize.setIcon(R.drawable.ic_center_light);
		resize.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.resize_menu:
				setSize((byte)((size+1) % 3));
				return true;
			default:
				Log.d(THIS, "No hit on menu item " + item);
				return false;
		}
	}

	private void setSize(byte newSize) {
		switch (newSize) {
			case 1:
				details.setVisibility(View.GONE);
				break;
			case 2:
				details.setVisibility(View.VISIBLE);
				getView().findViewById(R.id.map).setVisibility(View.GONE);
				break;
			case 0:
				details.setVisibility(View.VISIBLE);
				getView().findViewById(R.id.map).setVisibility(View.VISIBLE);
		}
		size = newSize;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		model = null; // Just to be sure, since we load the model anyway in onActivityCreated
		tasks = null;
		fragmentAdapter = null;
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//
//	}

	@Override
	public void taskUpdate() {
		fragmentAdapter.changeCursor(tasks.getTasksCursor());
	}

	@Override
	public void submissionsUpdate(Map<Integer, List<Submission>> submissions) {

	}
}
