/*
 * Copyright (c) 2014 Jakob Wenzel, Ramon Wirsch.
 *
 * This file is part of RallyeSoft.
 *
 * RallyeSoft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RallyeSoft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RallyeSoft. If not, see <http://www.gnu.org/licenses/>.
 */

package de.stadtrallye.rallyesoft.uimodel;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import de.stadtrallye.rallyesoft.R;
import de.stadtrallye.rallyesoft.common.Std;
import de.stadtrallye.rallyesoft.fragments.TaskDetailsFragment;
import de.stadtrallye.rallyesoft.model.tasks.ITaskManager;
import de.stadtrallye.rallyesoft.model.tasks.TaskManager;
import de.stadtrallye.rallyesoft.util.converters.CursorConverters;

/**
 * Adapter to use a Cursor to page through all Tasks in detail
 */
public class TaskPagerAdapter extends FragmentStatePagerAdapter {

//	private final Context context;
	private final ITasksMapControl mapControl;
	private final ITaskManager taskManager;
	private Cursor cursor;
	private final String ofStr; // Translation placeholder

	private CursorConverters.TaskCursorIds c;

	public TaskPagerAdapter(FragmentManager fm, Context context, ITaskManager taskManager, ITasksMapControl mapControl) {
		super(fm);
		this.cursor = taskManager.getTasksCursor();
		this.taskManager = taskManager;
//		this.context = context;
		this.mapControl = mapControl;

		this.ofStr = context.getString(R.string.of);

		c = CursorConverters.TaskCursorIds.read(cursor);
	}

	public void changeCursor(Cursor cursor) {
		Cursor old = this.cursor;
		if (old != null) {
			old.close();
		}
		c = CursorConverters.TaskCursorIds.read(cursor);
		this.cursor = cursor;
		this.notifyDataSetChanged();
	}

	public void close() {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);

		if (cursor != null) {
			cursor.moveToPosition(position);
			mapControl.setTask(taskManager.getTaskFromCursor(cursor));
		}
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new TaskDetailsFragment();
		Bundle args = new Bundle();
//		args.putSerializable(Std.TASK, CursorConverters.getTask(position, cursor, c));
		cursor.moveToPosition(position);
		args.putInt(Std.TASK_ID, cursor.getInt(c.id));
		f.setArguments(args);
		return f;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return (position+1) +" "+ ofStr +" "+ getCount();
	}

	@Override
	public int getCount() {
		return (cursor != null)? cursor.getCount() : 0;
	}

	public boolean isPosMatchingID(int pos, int taskID) {
		cursor.moveToPosition(pos);
		return taskID == cursor.getInt(c.id);
	}

	public int getPositionInCursor(int taskID) {
		return TaskManager.findTaskPositionInCursor(taskID, cursor, c);
	}
}
