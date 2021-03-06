package edu.purdue.acoll;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SubmitCallbackListener,
		StartOverCallbackListener {

	private static final String DEBUG_TAG = "DEBUG";
	private static final String ERROR_TAG = "ERROR";

	/**
	 * The ClientFragment used by the activity.
	 */
	private ClientFragment clientFragment;

	/**
	 * The ServerFragment used by the activity.
	 */
	private ServerFragment serverFragment;

	/**
	 * UI component of the ActionBar used for navigation.
	 */
	private Button left;
	private Button right;
	private TextView title;

	// mine
	private String host;
	private int port;
	private String name;
	private String from;
	private String to;
	private int type;

	/**
	 * Called once the activity is created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);

		this.clientFragment = ClientFragment.newInstance(this);
		this.serverFragment = ServerFragment.newInstance();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.fl_main, this.clientFragment);
		ft.commit();
	}

	/**
	 * Creates the ActionBar: - Inflates the layout - Extracts the components
	 */
	@SuppressLint("InflateParams")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);

		// Set up the ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		// Extract the UI component.
		this.title = (TextView) actionBarLayout.findViewById(R.id.tv_title);
		this.left = (Button) actionBarLayout.findViewById(R.id.bu_left);
		this.right = (Button) actionBarLayout.findViewById(R.id.bu_right);
		this.right.setVisibility(View.INVISIBLE);

		return true;
	}

	/**
	 * Callback function called when the user click on the right button of the
	 * ActionBar.
	 * 
	 * @param v
	 */
	public void onRightClick(View v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(this.getResources().getString(R.string.client));
		this.left.setVisibility(View.VISIBLE);
		this.right.setVisibility(View.INVISIBLE);
		ft.replace(R.id.fl_main, this.clientFragment);
		ft.commit();
	}

	/**
	 * Callback function called when the user click on the left button of the
	 * ActionBar.
	 * 
	 * @param v
	 */
	public void onLeftClick(View v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(this.getResources().getString(R.string.server));
		this.left.setVisibility(View.INVISIBLE);
		this.right.setVisibility(View.VISIBLE);
		ft.replace(R.id.fl_main, this.serverFragment);
		ft.commit();

	}

	public void getFormData() {
		// Client info
		Log.d(DEBUG_TAG, "MainActivity.onSubmit(): Getting client info");
		name = this.clientFragment.getName();
		type = this.clientFragment.getType();
		from = this.clientFragment.getFrom();
		to = this.clientFragment.getTo();

		// Server info
		Log.d(DEBUG_TAG, "MainActivity.onSubmit(): Getting server info");
		host = this.serverFragment.getHost(getResources().getString(
				R.string.default_host));
		port = this.serverFragment.getPort(Integer.parseInt(getResources()
				.getString(R.string.default_port)));
	}

	public boolean isFormValid() {
		Log.d(DEBUG_TAG,
				"MainActivity.onSubmit(): Performing sanity check on user input");
		// Client stuff
		Log.d(DEBUG_TAG, "MainActivity.onSubmit(): Checking client input");
		if (name == null || name.equals("")) {
			invalidateForm(getString(R.string.err_name_empty));
			return false;
		} else if (name.contains(",")) {
			invalidateForm(getString(R.string.err_name_comma));
			return false;
		} else if (from == null) {
			invalidateForm("From is empty");
			return false;
		} else if (from.equals("*")) {
			invalidateForm("From is *");
			return false;
		} else if (to == null) {
			invalidateForm("To is empty");
			return false;
		} else if (to.equals(from)) {
			invalidateForm("To is the same as From");
			return false;
		} else if (type != 0 && type != 1 && type != 2) {
			invalidateForm("Type is not an accepted type");
			return false;
		} else if (to.equals("*") && type != 2) {
			invalidateForm(getString(R.string.err_type));
			return false;
		}
		// Server stuff
		Log.d(DEBUG_TAG, "MainActivity.onSubmit(): Checking server input");
		if (host.equals(null) || host.equals("")) {
			invalidateForm("Host is empty");
			return false;
		} else if (host.contains(" ")) {
			invalidateForm(getString(R.string.err_host));
			return false;
		} else if (port < 1 || port > 65535) {
			invalidateForm(getString(R.string.err_port));
			return false;
		}
		return true;
	}

	public void invalidateForm(String errMsg) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage(errMsg);
		alertDialog.show();
		Log.d(ERROR_TAG, String.format("ERROR: %s", errMsg));
	}

	/**
	 * Callback function called when the user click on the submit button.
	 */
	@Override
	public void onSubmit() {
		Log.d(DEBUG_TAG, "MainActivity.onSubmit(): called");
		getFormData();
		if (!isFormValid()) {
			return;
		}

		String command = String.format(Locale.US, "%s,%s,%s,%d", name, from,
				to, type);

		// String command = this.getResources()
		// .getString(R.string.default_command);

		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(getResources().getString(R.string.match));
		this.left.setVisibility(View.INVISIBLE);
		this.right.setVisibility(View.INVISIBLE);

		// TODO: You may want additional parameters here if you tailor
		// the match fragment
		MatchFragment frag = MatchFragment.newInstance(this, host, port,
				command);

		ft.replace(R.id.fl_main, frag);
		ft.commit();
	}

	/**
	 * Callback function call from MatchFragment when the user want to create a
	 * new request.
	 */
	@Override
	public void onStartOver() {
		onRightClick(null);
	}

}
