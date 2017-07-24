package com.bye.hey.iptv;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().trim().length() > 0) {
                    if (password.getText().toString().trim().length() > 0) {
                        startiptvapp(username.getText().toString(), password.getText().toString());
                    } else
                        Toast.makeText(LoginActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(LoginActivity.this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void startiptvapp(String username, String password) {
        try {
            Intent intent = new Intent();
            intent.setClassName(_IPTV_CORE_PACKAGE_NAME, _IPTV_CORE_CLASS_NAME);

            // Set your playlist url and uncomment the lines below
            String playlistUrl = "http://diogen.online:25461/get.php?username=" + username + "&password=" + password + "&type=m3u_plus&output=ts";
            intent.setData(Uri.parse(playlistUrl));

            // If "package" extra is set, IPTV Core will be able to show your app name as a title
            intent.putExtra("package", getPackageName());

            // EPG URL can be set either by "url-tvg" parameter in your playlist or by the following extra (supported since IPTV Core 3.3)
            // intent.putExtra("url-tvg", "<EPG URL>");

            startActivity(intent);
            finish();
        } catch (ActivityNotFoundException e) {
            // IPTV core app is not installed, let's ask the user to install it.
            showIptvCoreNotFoundDialog();
        }
    }

    public void showIptvCoreNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_core_not_installed_title);
        builder.setMessage(R.string.dialog_core_not_installed_message);
        builder.setPositiveButton(R.string.dialog_button_install,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        try {
                            // try to open Google Play app first
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + _IPTV_CORE_PACKAGE_NAME)));
                        } catch (ActivityNotFoundException e) {
                            // if Google Play is not found for some reason, let's open browser
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + _IPTV_CORE_PACKAGE_NAME)));
                        }
                    }
                });
        builder.setNegativeButton(R.string.dialog_button_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        // if cancelled - just close the app
                        finish();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }


    private static final String _IPTV_CORE_PACKAGE_NAME = "ru.iptvremote.android.iptv.core";
    private static final String _IPTV_CORE_CLASS_NAME = _IPTV_CORE_PACKAGE_NAME + ".ChannelsActivity";
}


