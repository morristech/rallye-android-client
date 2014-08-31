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

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.rallye.model.structures.GroupUser;
import de.rallye.model.structures.PictureSize;
import de.stadtrallye.rallyesoft.R;
import de.stadtrallye.rallyesoft.model.IChatroom;
import de.stadtrallye.rallyesoft.model.IModel;
import de.stadtrallye.rallyesoft.model.converters.CursorConverters;
import de.stadtrallye.rallyesoft.model.structures.ChatEntry;
import de.stadtrallye.rallyesoft.model.structures.ChatEntry.Sender;

/**
 * ListAdapter for Chats from Cursor
 */
public class ChatCursorAdapter extends CursorAdapter {

	private final LayoutInflater inflator;
	private final ImageLoader loader;
	private final DateFormat converter;
	private final GroupUser user;
	private final IModel model;

	private IChatroom chatroom;

	private CursorConverters.ChatCursorIds c;

	private class ViewMem {
		public ImageView img;
		public TextView msg;
		public TextView sender;
		public TextView time;
		public ImageView msg_img;
	}

	public ChatCursorAdapter(Context context, IModel model, IChatroom chatroom) {
		super(context, chatroom.getChatCursor(), false);

		c = CursorConverters.ChatCursorIds.read(getCursor());

		this.user = model.getUser();
		this.model = model;

		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.loader = ImageLoader.getInstance();

		this.converter = SimpleDateFormat.getDateTimeInstance();

		this.chatroom = chatroom;
	}

	@Override
	public int getViewTypeCount() {
		return 2; //TODO: implement header-like viewType to show "read until here/new from here"
	}

	@Override
	public int getItemViewType(int position) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		return isMe(cursor)? 1 : 0;
	}

	private boolean isMe(Cursor cursor) {
		ChatEntry.Sender s = ChatEntry.getSender(user, cursor.getInt(c.groupID), cursor.getInt(c.userID));
		boolean me = (s == Sender.Me);
		return me;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View v = inflator.inflate((isMe(cursor))? R.layout.chat_item_right : R.layout.chat_item_left, null);

		ViewMem mem = new ViewMem();
		mem.img = (ImageView) v.findViewById(R.id.sender_img);
		mem.sender = (TextView) v.findViewById(R.id.msg_sender);
		mem.msg = (TextView) v.findViewById(R.id.msg);
		mem.time = (TextView) v.findViewById(R.id.time_sent);
		mem.msg_img = (ImageView) v.findViewById(R.id.msg_img);

		v.setTag(mem);

		fillView(mem, cursor);

		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor cursor) {
		ViewMem mem = (ViewMem) v.getTag();

		fillView(mem, cursor);
	}

	private void fillView(ViewMem mem, Cursor cursor) {
		int groupID = cursor.getInt(c.groupID);
		int userID = cursor.getInt(c.userID);

		String userName = cursor.getString(c.userName);
		if (userName == null) {
			userName = String.valueOf(userID);
			model.onMissingUserName(userID);
		}

		String groupName = cursor.getString(c.groupName);
		if (groupName == null) {
			groupName = String.valueOf(groupID);
			model.onMissingGroupName(groupID);
		}

		mem.sender.setText(userName +" ("+ groupName +")");
		mem.msg.setText(cursor.getString(c.message));
		mem.time.setText(converter.format(new Date(cursor.getLong(c.timestamp) * 1000L)));

		int pictureID = cursor.getInt(c.pictureID);
		if (pictureID != 0) {
			mem.msg_img.setVisibility(View.VISIBLE);
			loader.displayImage(model.getUrlFromImageId(pictureID, PictureSize.Mini), mem.msg_img);
		} else {
			mem.msg_img.setVisibility(View.GONE);
//			loader.displayImage(null, mem.msg_img);
		}

		// ImageLoader jar
		// ImageLoader must apparently be called for _EVERY_ entry
		// When called with null or "" as URL, will display empty picture / default resource
		// Otherwise ImageLoader will not be stable and start swapping images
		loader.displayImage(model.getAvatarURL(groupID), mem.img);
//		loader.displayImage(null, (!me)? mem.img_r : mem.img_l);

		//Set read id on chatroom
		int id = cursor.getInt(c.id);
		chatroom.setLastReadId(id);
	}

	/**
	 * Unsafe to call if no valid cursor is present
	 * @param pos Chat Entry Position
	 * @return null, if none, else int > 0
	 */
	public Integer getPictureID(int pos) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(pos);
		return (cursor.isNull(c.pictureID))? null : cursor.getInt(c.pictureID);
	}

	public int getChatID(int pos) {
		Cursor cursor = getCursor();
		if (cursor.moveToPosition(pos))
			return (cursor.getInt(c.id));
		else
			return 0;
	}

	/**
	 * Find the position of a chatId
	 * @param chatId
	 * @return the position, 0 if not found
	 */
	public int findPos(int chatId) {
		if (CursorConverters.moveCursorToId(getCursor(), c.id,chatId))
			return getCursor().getPosition();
		return 0;
	}

	@Override
	public void changeCursor(Cursor cursor) {
		c = CursorConverters.ChatCursorIds.read(cursor);
		super.changeCursor(cursor);
	}
}
