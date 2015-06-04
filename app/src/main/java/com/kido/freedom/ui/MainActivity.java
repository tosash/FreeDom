package com.kido.freedom.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kido.freedom.R;
import com.kido.freedom.drawer.NavigationDrawerCallbacks;
import com.kido.freedom.drawer.NavigationDrawerFragment;
import com.kido.freedom.model.Balance;
import com.kido.freedom.model.Device;
import com.kido.freedom.model.ServerAccountResponse;
import com.kido.freedom.model.ServerBalance;
import com.kido.freedom.model.ServerProfileResponse;
import com.kido.freedom.model.ServerRegistration;
import com.kido.freedom.model.UserAccount;
import com.kido.freedom.model.UserProfile;
import com.kido.freedom.utils.CircularNetworkImageView;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks, Response.Listener<ServerRegistration>, Response.ErrorListener {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_PROFILE_ID = "profile_id";
    private final static String TAG = MainActivity.class.getSimpleName();
    private static String API_ROUTE = "/RegisterPhone";
    private static String API_GETPROFILE_INFO = "/ProfileInfo?profileId=";
    private static String API_GET_BALANCE = "/GetBalanceByProfileId";
    private static String API_GET_ACCOUNT = "/GetPrivateInfo?profileId=";
    public Device curDevice;
    public UserProfile curUser;
    public Balance curBalance;
    public UserAccount curUAccount;
    public CircularProgressView pDialog;
    public Context fContext = null;
    public Toolbar mToolbar;
    public TextView mCoins;
    public TextView mPoints;
    public CircularNetworkImageView avatar;
    protected String SENDER_ID_GCM = "389628942309";
    private GoogleCloudMessaging gcm = null;
    private String regid = null;
    private String profileId = null;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toast toast;
    private long lastBackPressTime = 0;

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fContext = getApplicationContext();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        initDesign();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.

        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer

        initDevice();
    }

    private void initDesign() {
        pDialog = (CircularProgressView) findViewById(R.id.progress_view);
        mCoins = (TextView) findViewById(R.id.textMoney);
        mPoints = (TextView) findViewById(R.id.textPoints);
        avatar = (CircularNetworkImageView) findViewById(R.id.imgAvatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentManager fragmentManager = getFragmentManager(); // For AppCompat use getSupportFragmentManager
                    fragmentManager.beginTransaction()
                            .add(R.id.container, new FragmentAccount())
//                            .replace(R.id.container, new FragmentAccount())
                            .addToBackStack(null)
                            .commit();
                    mNavigationDrawerFragment.closeDrawer();
                    Log.i(TAG, "Avatar is pressed, visible in LogCat");
                    ;
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

    }

    protected void initDevice() {
        curDevice = new Device();
        curUser = new UserProfile();
        curBalance = new Balance();
        curUAccount = new UserAccount();
        curDevice.setpModelAndVersionDevice(getDeviceName());
        curDevice.setpDeviceId(getDeviceId());
        initGCM();
        getUserBalance();
        getProfileValues();
        getAccount();
    }

    public void showProgressDialog() {
        if (!pDialog.isShown()) {
            pDialog.setVisibility(View.VISIBLE);
            pDialog.startAnimation();
        }
    }

    public void hideProgressDialog() {
        if (pDialog.isShown()) {
            pDialog.resetAnimation();
            pDialog.setVisibility(View.INVISIBLE);
        }
    }


    public void getProfileValues() {
        showProgressDialog();
        VolleySingleton.getInstance(fContext).addToRequestQueue(
                new GsonRequest<ServerProfileResponse>(Request.Method.GET,
                        API_GETPROFILE_INFO + curDevice.getProfileId(),
                        ServerProfileResponse.class,
                        null,
                        new Response.Listener<ServerProfileResponse>() {
                            @Override
                            public void onResponse(ServerProfileResponse response) {
                                hideProgressDialog();
                                curUser = response.getValue();
                                mNavigationDrawerFragment.setUserData(curUser, null);//BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
                                CircularNetworkImageView avatar = (CircularNetworkImageView) findViewById(R.id.imgAvatar);
                                avatar.setImageUrl(curUser.getUserImage(), VolleySingleton.getInstance(fContext).getImageLoader());
                            }
                        },
                        this,
                        null), TAG);
    }

    public void getAccount() {
        showProgressDialog();
        VolleySingleton.getInstance(fContext).addToRequestQueue(
                new GsonRequest<ServerAccountResponse>(Request.Method.GET,
                        API_GET_ACCOUNT + curDevice.getProfileId(),
                        ServerAccountResponse.class,
                        null,
                        new Response.Listener<ServerAccountResponse>() {
                            @Override
                            public void onResponse(ServerAccountResponse response) {
                                hideProgressDialog();
                                curUAccount = response.getValue();
                            }
                        },
                        this,
                        null), TAG);
    }

    public void getUserBalance() {
        showProgressDialog();
        JSONObject params = new JSONObject();
        try {
            params.put("ProfileId", curDevice.getProfileId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(fContext).addToRequestQueue(
                new GsonRequest<ServerBalance>(Request.Method.POST,
                        API_GET_BALANCE,
                        ServerBalance.class,
                        null,
                        new Response.Listener<ServerBalance>() {
                            @Override
                            public void onResponse(ServerBalance response) {
                                hideProgressDialog();
                                curBalance = response.getValue();
                                mCoins.setText(Double.toString(curBalance.getMoney()));
                                mPoints.setText(Integer.toString(curBalance.getPoints()));
                            }
                        },
                        this,
                        params), TAG);
    }

    private void makeRegisterPhone() {
        showProgressDialog();
        JSONObject params = new JSONObject();
        try {
            params.put("ModelAndVersionPhone", curDevice.getpModelAndVersionDevice());
            params.put("PhoneId", curDevice.getpDeviceId());
            params.put("PushNotificationToken", curDevice.getpPushNotificationToken());
            params.put("TypePhone", String.valueOf(curDevice.getpTypeDevice()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(fContext).addToRequestQueue(
                new GsonRequest<>(Request.Method.POST, API_ROUTE, ServerRegistration.class, null, this, this, params), TAG);
    }

    @Override
    public void onResponse(ServerRegistration answer) {
        hideProgressDialog();
        curDevice.setProfileId(answer.getValue());
        setPropertyProfileId(fContext, answer.getValue());
        initDevice();
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();
        Log.e(TAG, "err: " + error.toString());
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(fContext, "TimeOut Error", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(fContext, "AuthFailureError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(fContext, "ServerError", Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(fContext, "NetworkError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(fContext, "ParseError", Toast.LENGTH_LONG).show();
        }


        String json = null;

        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if (json != null) displayMessage(json);
                    break;
            }
            //Additional cases
        }
    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString) {
        Toast.makeText(fContext, toastString, Toast.LENGTH_LONG).show();
    }

    public String getDeviceName() {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private String getDeviceId() {
        String deviceId = "";
        final TelephonyManager mTelephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(fContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported - Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void initGCM() {


        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(fContext);
            profileId = getSavedProfileId(fContext);
            curDevice.setpPushNotificationToken(regid);
            curDevice.setProfileId(profileId);
            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                if (profileId.isEmpty()) {
                    makeRegisterPhone();
                }
            }

        } else {
            Log.d(TAG, "No valid Google Play Services APK found.");
        }
    }

    private String getSavedProfileId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String profileId = prefs.getString(PROPERTY_PROFILE_ID, "");
        if (profileId.isEmpty()) {
            Log.d(TAG, "Profile ID not found.");
            return "";
        }
        return profileId;
    }

    private void setPropertyProfileId(Context context, String profileId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        Log.i(TAG, "Saving ProfileId " + profileId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_PROFILE_ID, profileId);
        editor.commit();
    }


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);

//        prefs.edit().clear().commit();

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.d(TAG, "Registration ID not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.d(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }


    private void registerInBackground() {
        showProgressDialog();
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    regid = gcm.register(SENDER_ID_GCM);
                    Log.i(this.toString(), "regId = " + regid);
//                    hideProgressDialog();
                    curDevice.setpPushNotificationToken(regid);
                    makeRegisterPhone();
                    storeRegistrationId(fContext, regid);
                    msg = regid;

                } catch (IOException ex) {
//                    hideProgressDialog();
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                hideProgressDialog();
            }

            @Override
            protected void onCancelled() {
                hideProgressDialog();
            }

        }.execute(null, null, null);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch (position) {
            default:
            case 0:
                fragment = new FragmentAccount();
                break;
            case 1:
                fragment = new FragmentAccount();
                break;
        }
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {
//            super.onBackPressed();
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
                    toast = Toast.makeText(this, "Press back again to close this app", 4000);
                    toast.show();
                    this.lastBackPressTime = System.currentTimeMillis();
                } else {
                    if (toast != null) {
                        toast.cancel();
                    }
                    super.onBackPressed();
                }
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
