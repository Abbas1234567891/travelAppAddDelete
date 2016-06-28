package com.tcs.sqlitedatabaseexample;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mMobile, mPassword;

    private Button mSubmit, mChange;
    DatabaseHelper mDataBaseHelper = new DatabaseHelper(this);
    Context mContext = this;
    Toolbar mToolbar;
    Button buttonSetPortrait, buttonSetLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        final int orientation = display.getOrientation();


        buttonSetPortrait = (Button) findViewById(R.id.btnPortrait);
        buttonSetLandscape = (Button) findViewById(R.id.btnLandscap);

        buttonSetPortrait.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        });

        buttonSetLandscape.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

        });


        mMobile = (EditText) findViewById(R.id.edtMobile);
        mPassword = (EditText) findViewById(R.id.edtPassword);

        mSubmit = (Button) findViewById(R.id.btnSubmit);
        mChange = (Button) findViewById(R.id.btnChangePassword);

        mSubmit.setOnClickListener(this);
        mChange.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.lang_french:
                String languageToLoad = "fr_FR";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.acc_delete:
                Deletedata();
                break;
            case R.id.lang_english:
                String languageToLoad1 = "en";
                Locale locale1 = new Locale(languageToLoad1);
                Locale.setDefault(locale1);
                Configuration config1 = new Configuration();
                config1.locale = locale1;
                getBaseContext().getResources().updateConfiguration(config1, getBaseContext().getResources().getDisplayMetrics());
                Intent refreshActivity = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refreshActivity);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void Deletedata() {
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.prompts, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("Do you want to delete this account ?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Integer deleteRows = new DatabaseHelper(mContext).deleteData(userInput.getText().toString());
                                                if (deleteRows > 0)
                                                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();

                                                else
                                                    Toast.makeText(MainActivity.this, "Data is not Deleted", Toast.LENGTH_LONG).show();
                                                //finish();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("AlertDialogExample");
                                alert.show();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:


                DatabaseHelper databaseHelper=new DatabaseHelper(this);


                       new VerficationTask(MainActivity.this,mMobile.getText().toString(),mPassword.getText().toString(),databaseHelper).execute( );

                break;
            case R.id.btnChangePassword:
              /* Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);*/
                changePassword();


                break;

        }

    }

    public void changePassword() {
        LayoutInflater li = LayoutInflater.from(mContext);
        View changepasswordview = li.inflate(R.layout.changepasswordview, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        alertDialogBuilder.setView(changepasswordview);

        final EditText inputMobile = (EditText) changepasswordview
                .findViewById(R.id.edtMobile);
        final EditText inputoldpassword = (EditText) changepasswordview
                .findViewById(R.id.edtOldPassword);
        final EditText inputnew= (EditText) changepasswordview
                .findViewById(R.id.edtNewPassword);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("Do you want to Change the Password ?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                             int changePass = new DatabaseHelper(mContext).changePass(inputMobile.getText().toString(), inputoldpassword.getText().toString());
                                                if (changePass == 0) {
                                                    /*String mNewPassword = mContext.inputnewpassword.getText().toString();*/
                                                    String mNewPassword=inputnew.getText().toString();
                                                    if (!mNewPassword.equals("") && isValidPassword(mNewPassword)) {
                                                        int j = new DatabaseHelper(mContext).updateRecord(inputMobile.getText().toString(), mNewPassword);

                                                        Toast.makeText(MainActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Enter valid new Password", Toast.LENGTH_SHORT).show();
                                                    }


                                                } else {

                                                    Toast.makeText(MainActivity.this, "Password doesn't  change ", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("AlertDialogExample");
                                alert.show();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


    }


    public void insertData() {

        ContentValues cv = new ContentValues();
        cv.put(DataTable.MOBILE, mMobile.getText().toString());
        cv.put(DataTable.PASSWORD, mPassword.getText().toString());
        long id = new DatabaseHelper(this).insert(cv);
        if (id > 0) {
            Toast.makeText(MainActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }

    }

   public boolean isValidPassword(String password) {
        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }



}
