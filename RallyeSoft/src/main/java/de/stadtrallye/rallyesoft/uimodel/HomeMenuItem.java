/*
 * Copyright (c) 2014 Jakob Wenzel, Ramon Wirsch.
 *
 * This file is part of RallySoft.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.stadtrallye.rallyesoft.uimodel;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/**
 * Wrapper to imitate the relevant parts of the sherlock MenuItem needed for DrawerLayout to detect a homeAsUp press
 */
public class HomeMenuItem implements MenuItem {

//	private com.actionbarsherlock.view.MenuItem base;

//	public HomeMenuItem(com.actionbarsherlock.view.MenuItem base) {
//		this.base = base;
//	}

	@Override
	public int getItemId() {
//		return base.getItemId();
		return android.R.id.home;
	}

	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean collapseActionView() {
		return false;
	}

	@Override
	public boolean expandActionView() {
		return false;
	}

	@Override
	public ActionProvider getActionProvider() {
		return null;
	}

	@Override
	public View getActionView() {
		return null;
	}

	@Override
	public char getAlphabeticShortcut() {
		return 0;
	}

	@Override
	public int getGroupId() {
		return 0;
	}

	@Override
	public Drawable getIcon() {
		return null;
	}

	@Override
	public Intent getIntent() {
		return null;
	}

	@Override
	public ContextMenu.ContextMenuInfo getMenuInfo() {
		return null;
	}

	@Override
	public char getNumericShortcut() {
		return 0;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public SubMenu getSubMenu() {
		return null;
	}

	@Override
	public CharSequence getTitle() {
		return null;
	}

	@Override
	public CharSequence getTitleCondensed() {
		return null;
	}

	@Override
	public boolean hasSubMenu() {
		return false;
	}

	@Override
	public boolean isActionViewExpanded() {
		return false;
	}

	@Override
	public boolean isCheckable() {
		return false;
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
		return null;
	}

	@Override
	public android.view.MenuItem setActionView(View view) {
		return null;
	}

	@Override
	public android.view.MenuItem setActionView(int resId) {
		return null;
	}

	@Override
	public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
		return null;
	}

	@Override
	public android.view.MenuItem setCheckable(boolean checkable) {
		return null;
	}

	@Override
	public android.view.MenuItem setChecked(boolean checked) {
		return null;
	}

	@Override
	public android.view.MenuItem setEnabled(boolean enabled) {
		return null;
	}

	@Override
	public android.view.MenuItem setIcon(Drawable icon) {
		return null;
	}

	@Override
	public android.view.MenuItem setIcon(int iconRes) {
		return null;
	}

	@Override
	public android.view.MenuItem setIntent(Intent intent) {
		return null;
	}

	@Override
	public android.view.MenuItem setNumericShortcut(char numericChar) {
		return null;
	}

	@Override
	public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
		return null;
	}

	@Override
	public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
		return null;
	}

	@Override
	public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
		return null;
	}

	@Override
	public void setShowAsAction(int actionEnum) {

	}

	@Override
	public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
		return null;
	}

	@Override
	public android.view.MenuItem setTitle(CharSequence title) {
		return null;
	}

	@Override
	public android.view.MenuItem setTitle(int title) {
		return null;
	}

	@Override
	public android.view.MenuItem setTitleCondensed(CharSequence title) {
		return null;
	}

	@Override
	public android.view.MenuItem setVisible(boolean visible) {
		return null;
	}
}
