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

package de.stadtrallye.rallyesoft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import de.stadtrallye.rallyesoft.common.Std;
import de.stadtrallye.rallyesoft.geolocation.LocationManager;
import de.stadtrallye.rallyesoft.model.Server;
import de.stadtrallye.rallyesoft.model.pictures.IPictureManager;
import de.stadtrallye.rallyesoft.model.pictures.PictureManager;
import de.stadtrallye.rallyesoft.model.structures.Task;
import de.stadtrallye.rallyesoft.model.tasks.ITaskManager;
import de.stadtrallye.rallyesoft.storage.Storage;

/**
 * Created by Ramon on 04.10.13.
 */
public class SubmitNewSolutionActivity extends ActionBarActivity {

	public static final int REQUEST_CODE = 7;
	public static final int PICTURE_REQUEST_SOURCE = -100;

	private static final String THIS = SubmitNewSolutionActivity.class.getSimpleName();
	private static final String ARG_SUBMIT_TYPE = Std.SUBMIT_TYPE;
	private static final String ARG_TASK_ID = Std.TASK_ID;

	private ImageView imageView;
	private int type;

	private IPictureManager.IPicture picture;
	private EditText editText;
	private EditText editNumber;

	private LinearLayout tabPicture;
	private LinearLayout tabText;
	private LinearLayout tabNumber;
	private int taskID;
	private ITaskManager taskManager;
	private PictureManager pictureManager;
	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Layout, Title, ProgressCircle etc.
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setTitle(R.string.submit_new_solution);
		setContentView(R.layout.submit_new_solution);

//		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(true);

		// Initialize Model
		Storage.aquireStorage(getApplicationContext(), this);
		pictureManager = Storage.getPictureManager();
		taskManager = Server.getCurrentServer().acquireTaskManager(this);

		tabPicture = (LinearLayout) findViewById(R.id.tab_picture);
		tabText = (LinearLayout) findViewById(R.id.tab_text);
		tabNumber = (LinearLayout) findViewById(R.id.tab_number);

		imageView = (ImageView) findViewById(R.id.picture_submission);
		editText = (EditText) findViewById(R.id.text_submission);
		editNumber = (EditText) findViewById(R.id.number_submission);

		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				invalidateOptionsMenu();
			}
		};

		editText.addTextChangedListener(textWatcher);
		editNumber.addTextChangedListener(textWatcher);

		Intent intent = getIntent();
		type = intent.getIntExtra(ARG_SUBMIT_TYPE, 0);
		taskID = intent.getIntExtra(ARG_TASK_ID, -1);

		if ((type & Task.TYPE_PICTURE) == 0) {
			tabPicture.setVisibility(View.GONE);
		} else {
			imageView.setClickable(true);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					requestPicture();
				}
			});
		}
		if ((type & Task.TYPE_TEXT) == 0)
			tabText.setVisibility(View.GONE);
		if ((type & Task.TYPE_NUMBER) == 0)
			tabNumber.setVisibility(View.GONE);

		if (type == Task.TYPE_PICTURE) // If there is only a picture to send, open action selection right away
			imageView.post(new Runnable() {
				@Override
				public void run() {
					requestPicture();
				}
			});

		locationManager = new LocationManager(this, LocationManager.Mode.BURST);
	}

	@Override
	protected void onStart() {
		super.onStart();

		locationManager.connect();

	}

	@Override
	protected void onStop() {
		super.onStop();

		locationManager.disconnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem send = menu.add(Menu.NONE, R.id.send_menu, Menu.NONE, R.string.send);
		send.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		send.setIcon(R.drawable.ic_send_now_light);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem send = menu.findItem(R.id.send_menu);
		boolean enable = (type & Task.TYPE_PICTURE) == Task.TYPE_PICTURE && picture != null;
		enable |= ((type & Task.TYPE_TEXT) == Task.TYPE_TEXT && editText.getText().length() > 0);
		enable |= ((type & Task.TYPE_NUMBER) == Task.TYPE_NUMBER && editNumber.getText().length() > 0);
		send.setEnabled(enable);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.send_menu:
				Intent result = new Intent();//TODO this intent is outdated and not used
				if (picture != null)
					result.putExtra(Std.PIC, picture.getHash());
				String text = editText.getText().toString();
				if (text != null && text.length() > 0)
					result.putExtra(Std.TEXT, text);
				String number = editNumber.getText().toString();
				Integer intNumber = null;
				if (number != null && number.length() > 0) {
					result.putExtra(Std.NUMBER, number);
					intNumber = Integer.parseInt(number);
				}
				setResult(RESULT_OK, result);

				taskManager.submitSolution(taskID, type, picture, text, intNumber, locationManager.getLastLocation());
				finish();
				return true;
			case android.R.id.home: //Up button
//				NavUtils.navigateUpFromSameTask(this);
				return false;
			default:
				return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(THIS, "Received ActivityResult: Req: " + requestCode + ", res: " + resultCode);
		picture = pictureManager.onActivityResult(requestCode, resultCode, data);

		if (picture != null) {
			ImageLoader.getInstance().displayImage(picture.getUri(), this.imageView);
			invalidateOptionsMenu();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Server.getCurrentServer().acquireTaskManager(this);
		Storage.releaseStorage(this);

		locationManager = null;
	}

	private void requestPicture() {
		Intent intent = pictureManager.startPictureTakeOrSelect(PictureManager.getSourceHint(PictureManager.SOURCE_SUBMISSION, "Task "+ taskID));//TODO read taskDescription
		startActivityForResult(intent, PictureManager.REQUEST_CODE);
	}
}
