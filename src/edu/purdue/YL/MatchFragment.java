package edu.purdue.YL;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This fragment is the "page" where the application display the log from the
 * server and wait for a match.
 *
 * @author YL
 */
public class MatchFragment extends Fragment implements OnClickListener {

	private static final String DEBUG_TAG = "DEBUG";

	/**
	 * Activity which have to receive callbacks.
	 */
	private StartOverCallbackListener activity;

	/**
	 * AsyncTask sending the request to the server.
	 */
	private Client client;

	/**
	 * Coordinate of the server.
	 */
	private String host;
	private int port;

	/**
	 * Command the user should send.
	 */
	private String command;

	private TextView logTextView;
	private TextView partnerTextView;
	private TextView fromTextView;
	private TextView toTextView;
	private TextView matchTextView;

	// Class methods
	/**
	 * Creates a MatchFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the start over
	 *            Button.
	 * @param host
	 *            address or IP address of the server.
	 * @param port
	 *            port number.
	 * 
	 * @param command
	 *            command you have to send to the server.
	 * 
	 * @return the fragment initialized.
	 */
	// TODO: you can add more parameters, follow the way we did it.
	// ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	public static MatchFragment newInstance(StartOverCallbackListener activity,
			String host, int port, String command) {
		MatchFragment f = new MatchFragment();

		f.activity = activity;
		f.host = host;
		f.port = port;
		f.command = command;

		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.match_fragment_layout, container,
				false);

		/**
		 * Register this fragment to be the OnClickListener for the startover
		 * button.
		 */
		view.findViewById(R.id.bu_start_over).setOnClickListener(this);

		this.logTextView = (TextView) view.findViewById(R.id.tv_log);
		this.partnerTextView = (TextView) view
				.findViewById(R.id.tv_partner_name);
		this.fromTextView = (TextView) view.findViewById(R.id.tv_from);
		this.toTextView = (TextView) view.findViewById(R.id.tv_to);
		this.matchTextView = (TextView) view.findViewById(R.id.tv_match1);

		/**
		 * Launch the AsyncTask
		 */
		this.client = new Client();
		this.client.execute("");

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		/**
		 * Close the AsyncTask if still running.
		 */
		this.client.close();

		/**
		 * Notify the Activity.
		 */
		this.activity.onStartOver();
	}

	class Client extends AsyncTask<String, String, String> implements Closeable {

		/**
		 * NOTE: you can access MatchFragment field from this class:
		 * 
		 * Example: The statement in doInBackground will print the message in
		 * the Eclipse LogCat view.
		 */

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... params) {

			String result = null;
			Socket s;
			// try {
			// s = new Socket(host, port);
			// publishProgress("Connected to server", host,
			// Integer.toString(port));
			// PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			//
			// BufferedReader in = new BufferedReader(new InputStreamReader(
			// s.getInputStream()));
			// Log.d(DEBUG_TAG, String.format(
			// "The Server at the address %s uses the port %d", host,
			// port));
			// Log.d(DEBUG_TAG, String.format(
			// "The Client will send the command: %s", command));
			//
			// // Send the command/message
			// out.println(command);
			// publishProgress("Command sent to server", command);
			// for (;;) {
			// result = in.readLine();
			// if (result.startsWith("RESPONSE: ")) {
			// out.println(":ACK");
			// publishProgress("Response recieved");
			// }
			// break;
			// }
			// in.close();
			// out.close();
			//
			// s.close();
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

			return "test done";
		}

		public void close() {
			// TODO: Clean up the client
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */

		// TODO: use the following method to update the UI.
		// ** DO NOT TRY TO CALL UI METHODS FROM doInBackground!!!!!!!!!! **

		/**
		 * Method executed just before the task.
		 */
		@Override
		protected void onPreExecute() {
			logTextView.setText(String.format("[%s] Valided form data\n",
					Calendar.getInstance().getTime()));

			partnerTextView.setText("");
			fromTextView.setText("");
			toTextView.setText("");
		}

		/**
		 * Method executed once the task is completed.
		 */
		@Override
		protected void onPostExecute(String result) {
			if (result.contains("MATCH")) {
				partnerTextView.setText(result);
				fromTextView.setText(result);
				toTextView.setText(result);
			}

		}

		/**
		 * Method executed when progressUpdate is called in the doInBackground
		 * function.
		 */
		@Override
		protected void onProgressUpdate(String... result) {
			if (result[0].equals("Connected to server")) {
				logTextView.append(String.format("[%s] Connected to server\n",
						Calendar.getInstance().getTime()));
			} else if (result[0].equals("Command sent to server")) {
				String command = result[1];
				logTextView.append(String.format(
						"[%s] Commnad \"%s\" sent to server\n", Calendar
								.getInstance().getTime(), command));
			} else if (result[0].equals("Response recieved")) {
				logTextView.append(String.format("[%s] Response recieved\n",
						Calendar.getInstance().getTime()));
			}
		}
	}

}
