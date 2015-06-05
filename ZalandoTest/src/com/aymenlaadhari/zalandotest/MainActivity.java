package com.aymenlaadhari.zalandotest;

import java.util.HashMap;

import model.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import app.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {

	private String apiGet = "http://localhost:8080/mazes";
	private String apiPost = "http://localhost:8080/mazes/maze-1/position";
	private ProgressDialog pDialog;
	TextView textDirection;
	String direction = "";
	Position position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage("Searching...");
		
		textDirection = (TextView)findViewById(R.id.textView1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// get mazes
		public void makeGetmazes(String from, String dest, String phrase) {
			String uri = String.format(apiGet, from, dest, phrase);
			pDialog.show();
			// showpDialog();
			

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
					null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							try {

								JSONArray array = response.getJSONArray("tuc");
								for (int i = 0; i < array.length(); i++) {

									JSONObject feedObjGlob = (JSONObject) array
											.get(i);
									JSONObject feedObj = feedObjGlob
											.getJSONObject("phrase");

														//
									//

								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							

							

							pDialog.hide();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							

							// hide the progress dialog
							pDialog.hide();
							Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
						}
					});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(jsonObjReq);
			

		}

		public void makePostmazes()
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("Content-Type", "application/json; charset=utf-8");
			params.put("x", "0");
			params.put("y", "0");

			JsonObjectRequest req = new JsonObjectRequest(apiPost, new JSONObject(
					params), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						JSONObject jsonObject  = new JSONObject();
						jsonObject = response.getJSONObject("from");
						position.setX(jsonObject.getString("x"));
						position.setY(jsonObject.getString("y"));

						Toast.makeText(getApplicationContext(), "data received",
								Toast.LENGTH_LONG).show();
						
						direction = response.getString("direction");
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
				}
			});

			// add the request object to the queue to be executed
			AppController.getInstance().addToRequestQueue(req);
			
		}
		
		
		public void onlocationChanged(Position position)
		{
			textDirection.setText(direction);
		}
		

}
