package de.stadtrallye.rallyesoft.uimodel;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.rallye.model.structures.GroupUser;
import de.stadtrallye.rallyesoft.R;
import de.stadtrallye.rallyesoft.model.Chatroom.ChatCursor;
import de.stadtrallye.rallyesoft.model.IModel;
import de.stadtrallye.rallyesoft.model.structures.ChatEntry;
import de.stadtrallye.rallyesoft.model.structures.ChatEntry.Sender;

/**
 * Created by Ramon on 30.06.13
 */
public class ChatCursorAdapter extends CursorAdapter {

	private final LayoutInflater inflator;
	private final ImageLoader loader;
	private final DateFormat converter;
	private final GroupUser user;
	private final IModel model;

	private class ViewMem {
		public ImageView img_l;
		public ImageView img_r;
		public TextView msg;
		public TextView sender;
		public TextView time;
		public LinearLayout layout;
	}

	public ChatCursorAdapter(Context context, Cursor cursor, IModel model) {
		super(context, cursor, false);
		//TODO: maybe get column ids from cursor? (may need to refresh on cursor change?)

		this.user = model.getUser();
		this.model = model;

		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.loader = ImageLoader.getInstance();
		DisplayImageOptions disp = new DisplayImageOptions.Builder()
				.cacheOnDisc()
				.cacheInMemory() //TODO: Still unlimited Cache on Disk
				.showStubImage(R.drawable.stub_image)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.defaultDisplayImageOptions(disp)
				.build();
		loader.init(config);

		this.converter = SimpleDateFormat.getDateTimeInstance();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View v = inflator.inflate(R.layout.chat_item, null);

		ViewMem mem = new ViewMem();
		mem.layout = (LinearLayout) v.findViewById(R.id.chat_row);
		mem.img_l = (ImageView) v.findViewById(R.id.sender_img_l);
		mem.img_r = (ImageView) v.findViewById(R.id.sender_img_r);
		mem.sender = (TextView) v.findViewById(R.id.msg_sender);
		mem.msg = (TextView) v.findViewById(R.id.msg);
		mem.time = (TextView) v.findViewById(R.id.time_sent);

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
		int groupID = cursor.getInt(ChatCursor.groupID);
		int userID = cursor.getInt(ChatCursor.userID);

		ChatEntry.Sender s = ChatEntry.getSender(user, groupID, userID);
		boolean me = (s == Sender.Me);

		mem.layout.setGravity((me)? Gravity.RIGHT : Gravity.LEFT);
		mem.img_r.setVisibility((me)? View.VISIBLE : View.GONE);
		mem.img_l.setVisibility((me)? View.GONE : View.VISIBLE);

		String userName = cursor.getString(ChatCursor.userName);
		if (userName == null) {
			userName = String.valueOf(userID);
			model.onMissingUserName(userID);
		}

		String groupName = cursor.getString(ChatCursor.groupName);
		if (groupName == null) {
			groupName = String.valueOf(groupID);
			model.onMissingGroupName(groupID);
		}

		mem.sender.setText(userName +" ("+ groupName +")");
		mem.msg.setText(cursor.getString(ChatCursor.message));
		mem.time.setText(converter.format(new Date(cursor.getLong(ChatCursor.timestamp) * 1000L)));

		int pictureID = cursor.getInt(ChatCursor.pictureID);

		// ImageLoader jar
		// ImageLoader must apparently be called for _EVERY_ entry
		// When called with null or "" as URL, will display empty picture / default resource
		// Otherwise ImageLoader will not be stable and start swapping images
		loader.displayImage(model.getAvatarURL(groupID), (me)? mem.img_r : mem.img_l);
		loader.displayImage(null, (!me)? mem.img_r : mem.img_l);
	}

	public Integer getPictureID(int pos) {
		return null;
	}
}
