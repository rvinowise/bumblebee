package org.rvinowise.bumblebee_jumper.activities.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import org.rvinowise.bumblebee_jumper.R;
import org.rvinowise.bumblebee_jumper.activities.Game_menu_activity;


public class Google_fragment extends Fragment
implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener

{


    private static final String TAG = "G_FRAG";
    GoogleApiClient googleApiClient;
    private boolean resolving_connection_failure = false;
    private boolean player_wants_signing_in = false;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    Button btn_sign_in;
    Button btn_sign_out;
    Button btn_show_leaderboard;

    Game_menu_activity game_menu_activity;


    Context context;
    @Override
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.google_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        init_layout();

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            game_menu_activity = (Game_menu_activity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Game_menu_activity");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("player_wants_signing_in", player_wants_signing_in);
        super.onSaveInstanceState(savedInstanceState);
    }
    public void restoreInstanceState(Bundle savedInstanceState) {
        player_wants_signing_in = savedInstanceState.getBoolean("player_wants_signing_in");
        can_connect_to_cloud();
    }

    private void init_layout() {

        btn_sign_in = (Button) getView().findViewById(R.id.btn_sign_in_google);
        btn_sign_in.setOnClickListener(this);
        btn_sign_out = (Button) getView().findViewById(R.id.btn_sign_out);
        btn_show_leaderboard = (Button) getView().findViewById(R.id.btn_show_leaderboard);

        draw_disconnected_interface();
    }

    private void can_connect_to_cloud() {
        if (player_wants_signing_in) {
            googleApiClient.connect();
            Log.d("ACTT", "can_connect_to_cloud connect()");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("ACTT", "onStop(): disconnecting");
        if (is_signed_in()) {
            submit_score();
            googleApiClient.disconnect();
        }
    }
    private boolean is_signed_in() {
        return (googleApiClient != null && googleApiClient.isConnected());
    }
    private void can_syncronize_score_with_cloud() {
        if (is_signed_in()) {
            submit_score();
        }
    }
    private void submit_score() {
        if (getLast_score() > 0) {
            Games.Leaderboards.submitScore(
                    googleApiClient, getString(R.string.leaderboard), getLast_score());
            Log.d("ACTT", "score suibmitted to cloud");
        }
    }
    private int getLast_score() {
        return game_menu_activity.getLast_score();
    }
    @Override
    public  void onResume() {
        super.onResume();
        can_connect_to_cloud();
    }


    /* control of player account */
    @Override
    public void onClick(View v) {
        switch ( v.getId() )
        {
            case R.id.btn_sign_in_google:
                onSignInButtonClicked(v);
                break;
        }
    }
    public void onSignInButtonClicked(View v) {
        player_wants_signing_in = true;
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {

        } else {
            googleApiClient.connect();
            resolving_connection_failure = false;
        }
    }

    public void onSignOutButtonClicked(View v) {
        player_wants_signing_in = false;

        Games.signOut(googleApiClient);
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
            resolving_connection_failure = false;
            draw_disconnected_interface();
        }
    }



    private String get_player_name() {
        Player player = Games.Players.getCurrentPlayer(googleApiClient);
        if (player == null) {
            Log.w(TAG, "Games.Players.getCurrentPlayer() is NULL!");
            return getString(R.string.player_anonimous);
        } else {
            return player.getDisplayName();
        }
    }

    private void draw_connected_interface() {
        btn_sign_in.setVisibility(View.GONE);
        btn_sign_out.setVisibility(View.VISIBLE);
        btn_show_leaderboard.setVisibility(View.VISIBLE);
    }
    private void draw_disconnected_interface() {
        btn_sign_in.setVisibility(View.VISIBLE);
        btn_sign_out.setVisibility(View.GONE);
        btn_show_leaderboard.setVisibility(View.GONE);
    }

    /* callback of google play services (for leaderboard)  */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        draw_connected_interface();
        can_syncronize_score_with_cloud();
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

        if (player_wants_signing_in) {
            resolving_connection_failure = true;
            if (resolveConnectionFailure(googleApiClient, connectionResult)) {
                return;
            } else {
                resolving_connection_failure = false;
            }
        }

        draw_disconnected_interface();
    }

    public boolean resolveConnectionFailure(
            GoogleApiClient client, ConnectionResult result
    ) {

        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(getActivity(), RC_SIGN_IN);
                return true;
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                client.connect();
                return false;
            }
        } else {
            // not resolvable... so show an error message
            new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.signing_other_error))
                    .setNeutralButton(android.R.string.ok, null).create();
            return false;
        }
    }

    public void onShowLeaderboardsRequested(View v) {
        if (is_signed_in()) {
            //Intent leaderboardIntent =
            //        Games.Leaderboards.getLeaderboardIntent(googleApiClient, getString(R.string.leaderboard));
            Intent leaderboardIntent =
                    Games.Leaderboards.getAllLeaderboardsIntent(googleApiClient);
            startActivityForResult(leaderboardIntent, RC_UNUSED);
        } else {
            //info_dialog(this, getString(R.string.leaderboards_not_available)).show();
        }
    }
}
