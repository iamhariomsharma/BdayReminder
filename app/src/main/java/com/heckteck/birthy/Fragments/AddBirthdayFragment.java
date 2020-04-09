package com.heckteck.birthy.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heckteck.birthy.Adapters.BirthdayAdapter;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.Notifications.NotificationReceiver;
import com.heckteck.birthy.R;
import com.heckteck.birthy.ViewModel.AddBirthdayViewModel;
import com.heckteck.birthy.ViewModel.BirthdayViewModel;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;


public class AddBirthdayFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CONTACTS_REQUEST_CODE = 104;
    private static final int CONTACT_PIKE_CODE = 105;
    private static final int DATE_PICKER_FRAGMENT_NO_YEAR = 2;
    private static final int DATE_PICKER_FRAGMENT_YEAR = 1;
    private static final int IMAGE_PIC_CAMERA_CODE = 102;
    private static final int IMAGE_PIC_GALLERY_CODE = 103;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int TIME_PICKER_FRAGMENT = 3;

    private CircleImageView userImg;
    private EditText et_name, et_dob, et_phone, et_notes, et_timePicker;
    private CheckBox isKnowYear;
    private FloatingActionButton fab_done;
    private Uri imgUri;
    private String currentPhotoPath;
    private String name, dob, phone, notes, timeToWish, currentDateTime;
    private AddBirthdayViewModel addBirthdayViewModel;
    private ImageButton btn_attachContact;
    int year, day, hour, minute, month;
//    BirthdayAdapter birthdayAdapter = new BirthdayAdapter();

    Calendar now = Calendar.getInstance();

    public AddBirthdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_birthday, container, false);

        userImg = view.findViewById(R.id.userImg);
        et_name = view.findViewById(R.id.et_username);
        et_dob = view.findViewById(R.id.et_birthdayPicker);
        et_phone = view.findViewById(R.id.et_phone);
        et_notes = view.findViewById(R.id.et_notes);
        et_timePicker = view.findViewById(R.id.et_timePicker);
        fab_done = view.findViewById(R.id.fab_addData);
        isKnowYear = view.findViewById(R.id.checkKnowYear);
        btn_attachContact = view.findViewById(R.id.attachContact);

        addBirthdayViewModel = ViewModelProviders.of(getActivity()).get(AddBirthdayViewModel.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        CharSequence formatedTime = DateFormat.format("hh:mm a", calendar);
        et_timePicker.setText(formatedTime);

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

//        et_dob.setKeyListener(null);
        et_dob.setInputType(InputType.TYPE_NULL);
        et_timePicker.setInputType(InputType.TYPE_NULL);

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isKnowYear.isChecked()) {
                    DatePickerFragment datePickerDialog = new DatePickerFragment();
                    datePickerDialog.setTargetFragment(AddBirthdayFragment.this, DATE_PICKER_FRAGMENT_NO_YEAR);
                    datePickerDialog.show(getFragmentManager(), "BIRTHDAY NO YEAR");
                } else {
                    DatePickerFragment datePickerDialog = new DatePickerFragment();
                    datePickerDialog.setTargetFragment(AddBirthdayFragment.this, DATE_PICKER_FRAGMENT_YEAR);
                    datePickerDialog.show(getFragmentManager(), "BIRTHDAY YEAR");
                }
            }
        });

        et_timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(AddBirthdayFragment.this, TIME_PICKER_FRAGMENT);
                timePicker.show(getParentFragmentManager(), "WISH TIME");
            }
        });

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString().trim();
                String dateOfBirth = et_dob.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dateOfBirth)) {
                    Toast.makeText(getActivity(), "Please fill a name and a date", Toast.LENGTH_LONG).show();
                } else {
                    insertBirthday();
                }

            }
        });

        btn_attachContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askContactsPermission();
            }
        });

        return view;
    }

    private void insertBirthday() {
        name = et_name.getText().toString().trim();
        phone = PhoneNumberUtils.formatNumber(et_phone.getText().toString().trim());
        notes = et_notes.getText().toString().trim();
        dob = et_dob.getText().toString().trim();
        timeToWish = et_timePicker.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int newMonth = month + 1;
        currentDateTime = day + "/" + newMonth + "/" + year;
        Log.d("DATE", currentDateTime);

        Birthday birthday = new Birthday("" + name,
                "" + dob,
                "" + phone,
                "" + notes,
                "" + timeToWish,
                "" + currentDateTime,
                "" + imgUri);

        addBirthdayViewModel.insert(birthday);
//        birthdayAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Birthday Inserted successfully", Toast.LENGTH_SHORT).show();
        getActivity().finish();

        startAlarm();

    }

    private void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DAY_OF_MONTH, day);
        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, 0);

        if (now.before(Calendar.getInstance())){
            now.add(Calendar.YEAR, 1);
        }

        int randomNumber = (int) ((new Date()).getTime() / 1000L % 2147483647L);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("TITLE", et_name.getText().toString());
        intent.putExtra("MSG", et_dob.getText().toString());
        intent.putExtra("PHONE", et_phone.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), randomNumber, intent, 0);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
        }
    }

    private void pickContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), CONTACT_PIKE_CODE);
    }


    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    askCameraPermissions();
                } else if (which == 1) {
                    askGalleryPermissions();
                }
            }
        });
        builder.create().show();
    }

    private void askGalleryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    private void askCameraPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void askContactsPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_REQUEST_CODE);
            } else {
                pickContact();
            }
        } else {
            pickContact();
        }
    }


    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent();
//        galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), IMAGE_PIC_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        String displayName;
        switch (requestCode) {

            case CONTACT_PIKE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex("display_name"));
                        String id = cursor.getString(cursor.getColumnIndex("_id"));
                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number"))) == 1) {
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri1 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                            String stringBuilder = "contact_id = " +
                                    id;
                            Cursor cursor1 = contentResolver.query(uri1, null, stringBuilder, null, null);
                            while (cursor1.moveToNext()) {
                                String str2 = cursor1.getString(cursor1.getColumnIndex("data1"));
                                Log.d("CONTACT: ", displayName);
                                Log.d("CONTACT: ", str2);
                                et_phone.setText(str2.trim());
                                et_name.setText(displayName);
                            }
                        }
                    }
                }
                break;

            case IMAGE_PIC_CAMERA_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(currentPhotoPath);
                    imgUri = Uri.fromFile(file);
                    userImg.setImageURI(imgUri);
                }
                break;

            case IMAGE_PIC_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imgUri = data.getData();
                    userImg.setImageURI(imgUri);
                }
                break;

            case DATE_PICKER_FRAGMENT_YEAR:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultDate = bundle.getString("RETURNED_DATE_YEAR", "error");
                    month = bundle.getInt("MONTH", 0);
                    day = bundle.getInt("DAY", 0);
                    et_dob.setText(resultDate);
                }
                break;

            case DATE_PICKER_FRAGMENT_NO_YEAR:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String showDob = bundle.getString("SHOW_DOB", "error");
                    et_dob.setText(showDob);
                    dob = bundle.getString("RETURNED_DATE_NO_YEAR", "error");
                }
                break;

            case TIME_PICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    timeToWish = bundle.getString("RETURNED TIME", "error");
                    et_timePicker.setText(timeToWish);
                    hour = bundle.getInt("HOUR", 0);
                    minute = bundle.getInt("MINUTE", 0);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(getActivity(), "Camera Permission is required to Use Camera", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickImageFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case CONTACTS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact();
                } else {
                    Toast.makeText(getActivity(), "Contact permission is required", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.heckteck.birthy.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, IMAGE_PIC_CAMERA_CODE);
            }
        }
    }

    private String getZodiacSign(int paramInt1, int paramInt2) {
        String str = "";
        if (paramInt2 == 0) {
            if (paramInt1 < 20) {
                str = "Capricorn";
            } else {
                str = "Aquarius";
            }
        } else if (paramInt2 == 1) {
            if (paramInt1 < 20) {
                str = "Aquarius";
            } else {
                str = "Pisces";
            }
        } else if (paramInt2 == 2) {
            if (paramInt1 < 20) {
                str = "Pisces";
            } else {
                str = "Aries";
            }
        } else if (paramInt2 == 3) {
            if (paramInt1 < 20) {
                str = "Aries";
            } else {
                str = "Taurus";
            }
        } else if (paramInt2 == 4) {
            if (paramInt1 < 20) {
                str = "Taurus";
            } else {
                str = "Gemini";
            }
        } else if (paramInt2 == 5) {
            if (paramInt1 < 20) {
                str = "Gemini";
            } else {
                str = "Cancer";
            }
        } else if (paramInt2 == 6) {
            if (paramInt1 < 20) {
                str = "Cancer";
            } else {
                str = "Leo";
            }
        } else if (paramInt2 == 7) {
            if (paramInt1 < 20) {
                str = "Leo";
            } else {
                str = "Virgo";
            }
        } else if (paramInt2 == 8) {
            if (paramInt1 < 20) {
                str = "Virgo";
            } else {
                str = "Libra";
            }
        } else if (paramInt2 == 9) {
            if (paramInt1 < 20) {
                str = "Libra";
            } else {
                str = "Scorpio";
            }
        } else if (paramInt2 == 10) {
            if (paramInt1 < 20) {
                str = "Scorpio";
            } else {
                str = "Sagittarius";
            }
        } else if (paramInt2 == 11) {
            if (paramInt1 < 20) {
                str = "Sagittarius";
            } else {
                str = "Capricorn";
            }
        }
        return str;
    }

}
