package org.rvinowise.bumblebee_jumper.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import org.rvinowise.bumblebee_jumper.R;


public class StartActivity extends FragmentActivity
implements GoogleApiClient.ConnectionCallbacks,
           GoogleApiClient.OnConnectionFailedListener
{
    GoogleApiClient googleApiClient;
    private boolean resolving_connection_failure = false;
    private boolean player_wants_signing_in = false;
    private boolean auto_signing_in = true;
    private static final int RC_SIGN_IN = 9001;

    final String TAG = "StartActivity";


    TextView lab_hello;
    Button btn_sign_in;
    Button btn_sign_out;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the Google API Client with access to Games
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        init_layout();
    }

    private void init_layout() {
        setContentView(R.layout.activity_start);
        lab_hello = (TextView) findViewById(R.id.lab_hello);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_out = (Button) findViewById(R.id.btn_sign_out);

        lab_hello.setText(R.string.lab_hello_anonimous);
        btn_sign_in.setVisibility(View.VISIBLE);
        btn_sign_out.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): connecting");
        //googleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop(): disconnecting");
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    /* control of player account */

    public void onSignInButtonClicked(View v) {
        player_wants_signing_in = true;
        googleApiClient.connect();
    }

    public void onSignOutButtonClicked(View v) {
        player_wants_signing_in = false;
        Games.signOut(googleApiClient);
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        lab_hello.setText(R.string.lab_hello_anonimous);
        btn_sign_in.setVisibility(View.VISIBLE);
        btn_sign_out.setVisibility(View.GONE);
    }


    /* callback of google play services (for leaderboard)  */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Player player = Games.Players.getCurrentPlayer(googleApiClient);
        String displayName;
        if (player == null) {
            Log.w(TAG, "Games.Players..getCurrentPlayer() is NULL!");
            displayName = "???";
        } else {
            displayName = player.getDisplayName();
        }
        lab_hello.setText(R.string.lab_hello + displayName);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (resolving_connection_failure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }

        if (player_wants_signing_in || auto_signing_in) {
            auto_signing_in = false;
            player_wants_signing_in = false;
            resolving_connection_failure = true;
            if (!resolveConnectionFailure(googleApiClient, connectionResult)) {
                resolving_connection_failure = false;
            }
        }

        // Sign-in failed, so show sign-in button on main menu
        lab_hello.setText(R.string.lab_hello_anonimous);
        btn_sign_in.setVisibility(View.VISIBLE);
        btn_sign_out.setVisibility(View.GONE);
    }

    public boolean resolveConnectionFailure(
            GoogleApiClient client, ConnectionResult result
                                                   ) {

        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, RC_SIGN_IN);
                return true;
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                client.connect();
                return false;
            }
        } else {
            // not resolvable... so show an error message
            new AlertDialog.Builder(this).setMessage(getString(R.string.signing_other_error))
                    .setNeutralButton(android.R.string.ok, null).create();
            return false;
        }
    }
}
