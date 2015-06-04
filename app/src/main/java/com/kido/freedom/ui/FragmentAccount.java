package com.kido.freedom.ui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kido.freedom.R;
import com.kido.freedom.adapters.CitiesAdapter;
import com.kido.freedom.model.Cities;

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
    AlertDialog dialog;
    private Context fContext;
    private View rootView;
    private Spinner sItemsCity;
    private Spinner sItemsGender;
    private EditText sName;
    private EditText sBirthDay;
    private EditText sEmail;
    private EditText sPhone;
    private ImageView avatar;
    private Bitmap profile_imageBitmap;
    private Bitmap setphoto;

    public FragmentAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);
        fContext = getActivity().getApplicationContext();
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
        String sDate = (((MainActivity) this.getActivity()).curUAccount.getBirthday() == null ? "" :
                ((MainActivity) this.getActivity()).curUAccount.getBirthday().toString());
        sBirthDay.setText(sDate);
        sEmail = (EditText) rootView.findViewById(R.id.eEmail);
        sEmail.setText(((MainActivity) this.getActivity()).curUAccount.getEmail());
        sPhone = (EditText) rootView.findViewById(R.id.ePhone);
        sPhone.setText(((MainActivity) this.getActivity()).curUAccount.getPhone());
        return rootView;
// And to get the actual User object that was selected, you can do this.
//        User user = (User) ( (Spinner) findViewById(R.id.user) ).getSelectedItem();
    }

    public void setProfilePhoto() {
        avatar = (ImageView) rootView.findViewById(R.id.imageView);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Bitmap bitmap = ((BitmapDrawable) ((MainActivity) this.getActivity()).avatar.getDrawable()).getBitmap();
        if (bitmap != null) {
            avatar.setImageBitmap(bitmap);
        }
    }


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity) this.getActivity()));
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    avatar.setImageBitmap(bitmap);

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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = fContext.getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                avatar.setImageBitmap(thumbnail);
            }
        }
    }
}
