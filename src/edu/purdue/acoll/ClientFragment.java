package edu.purdue.YL;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * This fragment is the "page" where the user inputs information about the
 * request, he/she wishes to send.
 *
 * @author YL
 */
public class ClientFragment extends Fragment implements OnClickListener {

	private static final String DEBUG_TAG = "DEBUG";
	
	// UI elements
	private EditText name;
	private RadioGroup preferences;
	private Spinner from;
	private Spinner to;

	/**
	 * Activity which have to receive callbacks.
	 */
	private SubmitCallbackListener activity;

	/**
	 * Creates a ProfileFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the submit Button.
	 * 
	 *            ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	 * 
	 * @return the fragment initialized.
	 */
	// ** DO NOT CREATE A CONSTRUCTOR FOR ProfileFragment **
	public static ClientFragment newInstance(SubmitCallbackListener activity) {
		ClientFragment f = new ClientFragment();

		f.activity = activity;
		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "ClientFragment.onCreateView(): called");
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.client_fragment_layout,
				container, false);

		/**
		 * Register this fragment to be the OnClickListener for the submit
		 * Button.
		 */
		view.findViewById(R.id.submitButton).setOnClickListener(this);

		// TODO: import your Views from the layout here. See example in
		// ServerFragment.
		this.name = (EditText) view.findViewById(R.id.nameText);
		this.preferences = (RadioGroup) view
				.findViewById(R.id.PreferencesRadioGroup);
		this.from = (Spinner) view.findViewById(R.id.fromSpinner);
		this.to = (Spinner) view.findViewById(R.id.toSpinner);

		return view;
	}

	/**
	 * @return name provided by user
	 */
	public String getName() {
		if (this.name != null) {
			return this.name.getText().toString();
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @return 0: A user that has no preference of which types of users to
	 *         match.
	 * 
	 *         1: The user is a requester and wants to match with volunteers
	 *         only.
	 * 
	 *         2:The user is a volunteer and cannot match with other volunteers.
	 */
	public int getType() {
		// get the stuff
		int selectedId = preferences.getCheckedRadioButtonId();
		Log.d(DEBUG_TAG, "getType(): selectedId is " + selectedId);
		if (selectedId == R.id.radio0) {
			// Is requester
			return 1;
		} else if (selectedId == R.id.radio1) {
			// Is volunteer
			return 2;
		} else if (selectedId == R.id.radio2) {
			// No preference
			return 0;
		} else {
			// Assume no preference
			return 0;
		}
	}

	public String getFrom() {
		return this.from.getSelectedItem().toString();
	}

	public String getTo() {
		return this.to.getSelectedItem().toString();
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		this.activity.onSubmit();
	}
}
