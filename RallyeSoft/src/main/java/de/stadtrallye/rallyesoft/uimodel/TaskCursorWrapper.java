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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import de.rallye.model.structures.Task;
import de.stadtrallye.rallyesoft.R;
import de.stadtrallye.rallyesoft.model.tasks.ITaskManager;

/**
 * Wrap a CursorAdapter and insert Header-Views
 * Translate between the "external" positions including headers and "internal" positions as they are inside the cursor
 */
public class TaskCursorWrapper extends BaseAdapter implements SectionIndexer {

	private final ITaskManager tasks;
	private final TaskCursorAdapter adapter;
	private final LayoutInflater inflator;
//	private final Context context;
	private int headerPos = -1;

	public TaskCursorWrapper(Context context, TaskCursorAdapter adapter, ITaskManager tasks) {
		this.adapter = adapter;
		this.tasks = tasks;
//		this.context = context;

		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		findHeaderPositions();
	}

	private void findHeaderPositions() {
		headerPos = tasks.getLocationSpecificTasksCount();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return !isHeaderPosition(position);
	}

	@Override
	public int getCount() {
		int count = adapter.getCount();
		return (headerPos > 0)? count+2 : count+1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (isHeaderPosition(position))
			return 1;
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return adapter.getItem(translatePosition(position));
	}

	public Task getTask(int position) {
		return adapter.getTask(translatePosition(position));
	}

	@Override
	public long getItemId(int position) {
		return (isHeaderPosition(position))? -1 * numHeaderPosition(position) : adapter.getItemId(translatePosition(position));
	}

	public int translatePosition(int position) {
		return position - numHeaderPosition(position);
	}

	private int numHeaderPosition(int position) {
		int i = 0;
		if (position > 0)
			i++;
		if (position > headerPos && headerPos > 0)
			i++;
		return i;
	}

	private boolean isHeaderPosition(int position) {
		return (position == 0 || position == headerPos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!isHeaderPosition(position)) {
			return adapter.getView(translatePosition(position), convertView, parent);
		} else {
			TextView v = (convertView == null)? (TextView) inflator.inflate(android.R.layout.preference_category, null) : (TextView) convertView;

			int r;

			switch (numHeaderPosition(position)+1) {
				case 1:
					r = R.string.locspecific_tasks;
					break;
				case 2:
				default:
					r = R.string.ubiquitous_tasks;
					break;
			}

			v.setText(r);
			return v;
		}
	}

	public void changeCursor(Cursor cursor) {
		findHeaderPositions();
		adapter.changeCursor(cursor);
	}

	@Override
	public Object[] getSections() {
		return new String[]{"1", "2"};
	}

	@Override
	public int getPositionForSection(int section) {
		switch (section) {
			case 0:
				return 0;
			case 1:
			default:
				return headerPos;
		}
	}

	@Override
	public int getSectionForPosition(int position) {
		return numHeaderPosition(position)-1;
	}

	public TaskCursorAdapter getAdapter() {
		return adapter;
	}

	public void close() {
		adapter.close();
	}
}
