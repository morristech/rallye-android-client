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
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RallyeSoft. If not, see <http://www.gnu.org/licenses/>.
 */

package de.stadtrallye.rallyesoft.fragments;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.rallye.model.structures.Group;
import de.rallye.model.structures.ServerInfo;
import de.stadtrallye.rallyesoft.R;
import de.stadtrallye.rallyesoft.model.IModel;
import de.stadtrallye.rallyesoft.uimodel.GroupListAdapter;
import de.stadtrallye.rallyesoft.uimodel.IConnectionAssistant;

/**
 * Page of ConnectionAssistant: choose a group to login to
 * If the Assistant already knows the group, highlight it
 */
public class AssistantGroupsFragment extends ListFragment implements IModel.IModelListener, AdapterView.OnItemClickListener {

	private IConnectionAssistant assistant;
	private GroupListAdapter groupAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		try {
			assistant = (IConnectionAssistant) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString() + " must implement IConnectionAssistant");
		}
	}

	private void restoreChoice(ListView list) {
		Integer pos = groupAdapter.findPosition(assistant.getGroup());

		if (pos != null) {
			list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			list.setItemChecked(pos, true);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		IModel model = assistant.getModel();
		groupAdapter = new GroupListAdapter(getActivity(), model.getAvailableGroups(), model);
		ListView list = getListView();
		setListAdapter(groupAdapter);

		restoreChoice(list);

		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		assistant.setGroup((int) id);
		assistant.next();
	}


	@Override
	public void onConnectionStateChange(IModel.ConnectionState newState) {

	}

	@Override
	public void onConnectionFailed(Exception e, IModel.ConnectionState fallbackState) {
		Toast.makeText(getActivity(), R.string.invalid_server, Toast.LENGTH_SHORT).show();
		assistant.back();
	}

	@Override
	public void onServerInfoChange(ServerInfo info) {

	}

	@Override
	public void onAvailableGroupsChange(List<Group> groups) {
		groupAdapter.changeGroups(groups);

		restoreChoice(getListView());
	}
}
