package org.rvinowise.bumblebee_jumper.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import org.rvinowise.bumblebee_jumper.BuildConfig;
import org.rvinowise.bumblebee_jumper.BumblebeeEngine;
import org.rvinowise.bumblebee_jumper.R;

import static com.google.android.gms.games.GamesActivityResultCodes.RESULT_SIGN_IN_FAILED;


public class StartActivity extends FragmentActivity
implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
{
    GoogleApiClient googleApiClient;
    private boolean resolving_connection_failure = false;
    private boolean player_wants_signing_in = false;
    private boolean auto_signing_in = true;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private static final int GAME_REQUEST = 0;
    private int last_score = 0;

    final String TAG = "StartActivity";

    ImageView img_tutorial;
    TextView lab_hello;
    TextView lab_score;
    Button btn_sign_in;
    Button btn_sign_out;
    Button btn_show_leaderboard;
    LinearLayout lay_sign_in;
    LinearLayout lay_sign_out;

    enum Special_launch {
        first_launch,
        first_updated_launch,
        routine_launch
    }
    private Special_launch special_launch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check_first_run();
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

        img_tutorial = (ImageView) findViewById(R.id.img_tutorial);
        if (getSpecial_launch() == Special_launch.first_launch) {
            img_tutorial.setVisibility(View.VISIBLE);
        } else {
            img_tutorial.setVisibility(View.GONE);
        }

        lab_hello = (TextView) findViewById(R.id.lab_hello);
        lab_score = (TextView) findViewById(R.id.lab_score);
        lab_score.setVisibility(View.GONE);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);
        btn_sign_out = (Button) findViewById(R.id.btn_sign_out);
        btn_show_leaderboard = (Button) findViewById(R.id.btn_show_leaderboard);


        draw_disconnected_interface();
    }


    @Override
    protected void onStart() {
        super.onStart();
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
    @Override
    public void onClick(View v) {
        switch ( v.getId() )
        {
            case R.id.btn_sign_in:
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
        if (last_score > 0) {
            lab_hello.setText(getString(R.string.authorized_yout_score_is, get_player_name()));
        } else {
            lab_hello.setText(getString(R.string.lab_hello, get_player_name()));
        }
        btn_sign_in.setVisibility(View.GONE);
        btn_sign_out.setVisibility(View.VISIBLE);
        btn_show_leaderboard.setVisibility(View.VISIBLE);
    }
    private void draw_disconnected_interface() {
        if (last_score > 0) {
            lab_hello.setText(getString(R.string.yout_score_is));
        } else {
            lab_hello.setText(getString(R.string.sign_in_why));
        }
        btn_sign_in.setVisibility(View.VISIBLE);
        btn_sign_out.setVisibility(View.GONE);
        btn_show_leaderboard.setVisibility(View.GONE);
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
            if (resolveConnectionFailure(googleApiClient, connectionResult)) {
                //draw_connected_interface();
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

    public void on_btn_start_game_click(View v) {
        boolean test = is_signed_in();
        Intent game_intent = new Intent(StartActivity.this, GameActivity.class);
        startActivityForResult(game_intent, GAME_REQUEST);
    }

    /* callback of google play services (for leaderboard)  */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        draw_connected_interface();
        syncronize_score_with_cloud();
    }

    private void syncronize_score_with_cloud() {
        if (last_score > 0) {
            if (is_signed_in()) {
                Games.Leaderboards.submitScore(
                        googleApiClient, getString(R.string.leaderboard), last_score);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                on_result_sign_in(resultCode);
                break;
            case GAME_REQUEST:
                fetch_result_score_from_game(resultCode, data);
                img_tutorial.setVisibility(View.GONE);
                break;
        }
    }
    private void on_result_sign_in(int resultCode) {
        resolving_connection_failure= false;
        if (resultCode == RESULT_OK) {
            googleApiClient.connect();
        } else if (resultCode == RESULT_SIGN_IN_FAILED) {
            //info_dialog(this, getString(R.string.sign_in_problem)).show();
            Toast.makeText(this, getString(R.string.sign_in_problem), Toast.LENGTH_SHORT).show();
        }
    }
    private void fetch_result_score_from_game(int resultCode, Intent data) {
        try {
            switch (resultCode) {
                case RESULT_OK:
                case RESULT_CANCELED:
                    last_score = data.getIntExtra("score", 0);
                    if (last_score > 0) {
                        lab_score.setVisibility(View.VISIBLE);
                        lab_score.setText(String.valueOf(last_score));
                    } else {
                        lab_score.setVisibility(View.GONE);
                    }
                    // сохранить очки в облаке можно только после Коннекшена Гугл-айпи, а это НЕ сразу после возврата
                    // результата Активити игры
            }
        } catch (RuntimeException e) {
            last_score = 0;
        }
    }


    private boolean is_signed_in() {
        return (googleApiClient != null && googleApiClient.isConnected());
    }

    public void onShowLeaderboardsRequested(View v) {
        if (is_signed_in()) {
            Intent leaderboardIntent =
                    Games.Leaderboards.getLeaderboardIntent(googleApiClient, getString(R.string.leaderboard));
            startActivityForResult(leaderboardIntent, RC_UNUSED);
        } else {
            //info_dialog(this, getString(R.string.leaderboards_not_available)).show();
        }
    }
    public static Dialog info_dialog(Activity activity, String text) {
        return (new AlertDialog.Builder(activity)).setMessage(text)
                .setNeutralButton(android.R.string.ok, null).create();
    }

    private Special_launch getSpecial_launch() {
        return special_launch;
    }

    private void check_first_run() {

        final String PREFS_NAME = "prefs";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            special_launch = Special_launch.routine_launch;
        } else if (savedVersionCode == DOESNT_EXIST) {
            special_launch = Special_launch.first_launch;
        } else if (currentVersionCode > savedVersionCode) {
            special_launch = Special_launch.first_updated_launch;
        }
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}
