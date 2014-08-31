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

package de.stadtrallye.rallyesoft.net;

import de.rallye.model.structures.PictureSize;

public final class Paths {

	public static final String GROUPS = "groups";
	public static final String STATUS = "system/status";
	public static final String CHATS = "chatrooms";
	public static final String MAP_NODES = "map/nodes";
	public static final String MAP_EDGES = "map/edges";
	public static final String MAP_CONFIG = "map/config";
	public static final String PICS = "pics";
	public static final String AVATAR = "avatar";
	public static final String INFO = "system/info";
	public static final String SERVER_PICTURE = "system/picture";
	public static final String USERS = "users";
	public static final String TASKS = "tasks";
	public static final String SUBMISSIONS = "tasks/all";

	/**
	 * return relative path to a picture
	 * @param picId PictureID
	 * @param size approximate Size of the requested Picture
	 * @return a RallyeServer compliant relative path (without the base URL)
	 */
	public static String getPic(int picId, PictureSize size) {
		return PICS +"/"+ picId +"/"+ size.toShortString();
	}

	public static String getAvatar(int groupID) {
		return GROUPS +"/"+ groupID +"/"+ AVATAR;
	}
}
