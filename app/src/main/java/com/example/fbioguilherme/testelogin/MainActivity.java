package com.example.fbioguilherme.testelogin;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView state;
    private ProfilePictureView profilePictureView;
    private TextView infoUser;
    private String gender;
    private String id;
    private String loca;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        state = (TextView) findViewById(R.id.state);
        infoUser = (TextView) findViewById(R.id.name);

        profilePictureView = (ProfilePictureView) findViewById(R.id.image);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n"
                              /* +  "Auth Token: "
                                + loginResult.getAccessToken().getToken() + "\n"*/);

                profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());
                state.setText("Estado: on");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                                // Application code
                                try {
                                    String gender = object.getString("gender");
                                    String id = object.getString("id");
                                    String locale = object.getString("locale");
                                    String name = object.getString("name");
                                    String birth = object.getString("birthday");

                                    //set info
                                    infoUser.setText("Name: " + name + "\n"
                                    + "Genero: " + gender + "\n"
                                    + "Local: " + locale + "\n"
                                    + "Id: " + id + "\n"
                                    + "Data de nascimento: " + birth);

                                } catch (JSONException e) {
                                    Log.v("LoginActivity", "Erro a extrair informcao do json");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,locale,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    state.setText("Estado: off");
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
