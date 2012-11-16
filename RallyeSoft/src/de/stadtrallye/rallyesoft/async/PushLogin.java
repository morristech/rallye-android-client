package de.stadtrallye.rallyesoft.async;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.stadtrallye.rallyesoft.communications.RallyePull;
import de.stadtrallye.rallyesoft.model.MapNode;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class PushLogin extends AsyncTask<Void, Void, int[]> {
	
	private RallyePull pull;
	private Context context;
	private String id;
	
	public PushLogin(RallyePull pull, Context context, String id) {
		this.pull = pull;
		this.context = context;
		this.id = id;
	}

	@Override
	protected int[] doInBackground(Void... params) {
		JSONArray js = pull.pushLogin(id, 2, "test");
		if (js == null)
			return null;
		int l = js.length();
		try {
			int[] res = new int[l];
			JSONObject next;
			for (int i=0; i<l; ++i) {
				next = js.getJSONObject(i);
				res[i] = next.getInt("chatroom");
			}
			
			return res;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(int[] result) {
		Toast.makeText(context, "Logged in!", Toast.LENGTH_SHORT).show();
	}

}