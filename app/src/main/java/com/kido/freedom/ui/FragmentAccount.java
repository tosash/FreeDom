package com.kido.freedom.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.kido.freedom.R;
import com.kido.freedom.adapters.CitiesAdapter;
import com.kido.freedom.model.Cities;
import com.kido.freedom.model.ServerRegistration;
import com.kido.freedom.model.ServerResponseMessage;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.Utils;
import com.kido.freedom.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment {

    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    private static final String API_POST_AVATAR64 = "/ChangePhotoBase64";
    private static final String API_POST_PROFILE = "/EditPrivateInfo";
    private static final String TAG = FragmentAccount.class.toString();
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private Spinner sItemsCity;
    private Spinner sItemsGender;
    private EditText sName;
    private EditText sBirthDay;
    private EditText sEmail;
    private EditText sPhone;
    private ImageView avatarFragment;
    private Bitmap profile_imageBitmap;
    private Bitmap setphoto;
    private boolean avatarChanged;
    private Button saveBtn;
    private Button recoveryBtn;


    public FragmentAccount() {
        // Required empty public constructor
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);
        fContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) this.getActivity();
        setProfilePhoto();
        ArrayList<Cities> cities = ((MainActivity) this.getActivity()).curUAccount.getCities();
        CitiesAdapter adapter = new CitiesAdapter(getActivity(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItemsCity = (Spinner) rootView.findViewById(R.id.spinnerCity);
        sItemsCity.setAdapter(adapter);
        sItemsCity.setSelection(cities.indexOf(((MainActivity) this.getActivity()).curUAccount.getIdCity()));
        sItemsGender = (Spinner) rootView.findViewById(R.id.spinnerGender);
        sItemsGender.setSelection(((MainActivity) this.getActivity()).curUAccount.getGender());
        sName = (EditText) rootView.findViewById(R.id.eName);
        sName.setText(((MainActivity) this.getActivity()).curUAccount.getName());
        sBirthDay = (EditText) rootView.findViewById(R.id.eBirthday);
        String sDate =
                (((MainActivity) this.getActivity()).curUAccount.getBirthday() == null ? "" :
                        ((MainActivity) this.getActivity()).curUAccount.getBirthday().toString());
        sBirthDay.setText(sDate);
        sEmail = (EditText) rootView.findViewById(R.id.eEmail);
        sEmail.setText(((MainActivity) this.getActivity()).curUAccount.getEmail());
        sPhone = (EditText) rootView.findViewById(R.id.ePhone);
        sPhone.setText(((MainActivity) this.getActivity()).curUAccount.getPhone());

        saveBtn = (Button) rootView.findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });
        return rootView;
// And to get the actual User object that was selected, you can do this.
//        User user = (User) ( (Spinner) findViewById(R.id.user) ).getSelectedItem();
    }

    public void setProfilePhoto() {
        avatarChanged = false;
        avatarFragment = (ImageView) rootView.findViewById(R.id.imageView);
        avatarFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (((BitmapDrawable) ((MainActivity) getActivity()).avatar.getDrawable()).getBitmap() != null) {
            Bitmap bitmap = ((BitmapDrawable) ((MainActivity) getActivity()).avatar.getDrawable()).getBitmap();
            avatarFragment.setImageBitmap(bitmap);
        }
    }

    private void selectImage() {

        final String[] options = {getResources().getString(R.string.cameraPhoto), getResources().getString(R.string.galleryPhoto), getResources().getString(R.string.cancel)};

        ListAdapter adapter = new ArrayAdapter<String>(
                fContext, R.layout.list_getphoto_row, options) {

            ViewHolder holder;

            public View getView(int position, View convertView,
                                ViewGroup parent) {
                final LayoutInflater inflater = (LayoutInflater) fContext.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_getphoto_row, null);

                    holder = new ViewHolder();
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    convertView.setTag(holder);
                } else {
                    // view already defined, retrieve view holder
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.title.setText(options[position]);
                holder.title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                holder.title.setGravity(Gravity.CENTER_HORIZONTAL);
                if (options[position].equals(getResources().getString(R.string.cancel))) {
                    holder.title.setBackgroundColor(Color.DKGRAY);
                    holder.title.setTextColor(Color.WHITE);
                } else {
                    holder.title.setBackgroundColor(Color.WHITE);
                    holder.title.setTextColor(Color.BLACK);
                }

                return convertView;
            }

            class ViewHolder {
                TextView title;
            }
        };


        TextView title = new TextView(fContext);
// You Can Customise your Title here
        title.setText(getResources().getString(R.string.titleGetPhoto));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);

        AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity) this.getActivity()));
        builder.setCustomTitle(title);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getResources().getString(R.string.cameraPhoto))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals(getResources().getString(R.string.galleryPhoto))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
                    bitmap = decodeSampledBitmapFromResource(f.getAbsolutePath(), 300, 300);
                    avatarFragment.setImageBitmap(bitmap);
                    avatarChanged = true;
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = fContext.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap thumbnail = decodeSampledBitmapFromResource(picturePath, 300, 300);
                Log.w("path of image from gallery......******************.........", picturePath + "");
                avatarFragment.setImageBitmap(thumbnail);
                avatarChanged = true;
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    // date validation using SimpleDateFormat
// it will take a string and make sure it's in the proper
// format as defined by you, and it will also make sure that
// it's a legal date


    public Boolean IsValidate() {
        Boolean rez = false;
        sName.setError(null);
        sEmail.setError(null);
        sBirthDay.setError(null);
        sPhone.setError(null);
        if (sName.getText().length() == 0) {
            sName.setError("Укажите имя!");
            sName.requestFocus();
        } else if (!isValidEmail(sEmail.getText())) {
            sEmail.setError("Укажите правильный email!");
            sEmail.requestFocus();
//        } else if (!(Pattern.compile(DATE_PATTERN).matcher(sBirthDay.getText().toString())).matches()) {
        } else if ((sBirthDay.getText().length() == 0) || (!Utils.isValidDate(sBirthDay.getText().toString()))) {
            sBirthDay.setError("Укажите дату!");
            sBirthDay.requestFocus();
        } else if (sPhone.getText().length() == 0) {
            sPhone.setError("Укажите номер телефона!");
            sPhone.requestFocus();
        } else {
            rez = true;
        }
        return rez;
    }


    public void saveProfileData() {
        if (IsValidate()) {
            JSONObject params;
            if (avatarChanged) {

                //Upload new avatar
                String sImg64 = BitMapToString(((BitmapDrawable) (avatarFragment.getDrawable())).getBitmap());
                ((MainActivity) getActivity()).showProgressDialog();
                params = new JSONObject();
                try {
                    params.put("ImageBase64", sImg64);
                    params.put("ProfileId", ((MainActivity) this.getActivity()).curDevice.getProfileId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                VolleySingleton.getInstance(fContext).addToRequestQueue(
                        new GsonRequest<ServerRegistration>(Request.Method.POST,
                                API_POST_AVATAR64,
                                ServerRegistration.class,
                                null,
                                new Response.Listener<ServerRegistration>() {
                                    @Override
                                    public void onResponse(ServerRegistration response) {
                                        ((MainActivity) getActivity()).hideProgressDialog();
                                        avatarChanged = false;
                                        String sResult = response.getValue();
                                        Log.i(TAG, "ResponseServer - Load new avatar: " + sResult);

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        ((MainActivity) getActivity()).hideProgressDialog();
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

                                    }
                                },
                                params), TAG);
            }
            //Edit Profile Data
            params = new JSONObject();
            try {
                params.put("BirthDate", (Utils.getFormatedTicksFromDate(sBirthDay.getText().toString())));
                params.put("CityId", ((Cities) sItemsCity.getSelectedItem()).getIdCity());
                params.put("Email", sEmail.getText().toString());
                params.put("Gender", sItemsGender.getSelectedItemPosition());
                params.put("Id", ((MainActivity) getActivity()).curDevice.getProfileId());
                params.put("IsInvisible", true);
                params.put("Name", sName.getText().toString());
                params.put("Phone", sPhone.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleySingleton.getInstance(fContext).addToRequestQueue(
                    new GsonRequest<ServerResponseMessage>(Request.Method.POST,
                            API_POST_PROFILE,
                            ServerResponseMessage.class,
                            null,
                            new Response.Listener<ServerResponseMessage>() {
                                @Override
                                public void onResponse(ServerResponseMessage response) {
                                    ((MainActivity) getActivity()).hideProgressDialog();

                                    String sResult = response.getMessage();
                                    Log.i(TAG, "ResponseServer - Load new profile data: " + sResult);
                                    Toast.makeText(fContext, sResult, Toast.LENGTH_SHORT).show();
                                    ((MainActivity) getActivity()).getProfileValues();
                                    ((MainActivity) getActivity()).getAccount();
//                                    getActivity().getFragmentManager().popBackStack();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    ((MainActivity) getActivity()).hideProgressDialog();
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

                                }
                            },
                            params), TAG);

        }
    }

    public void recoveryProfileData(View v) {
    }
}
