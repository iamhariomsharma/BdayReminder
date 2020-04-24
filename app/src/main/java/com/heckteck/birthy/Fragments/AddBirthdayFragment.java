package com.heckteck.birthy.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.Notifications.NotificationReceiver;
import com.heckteck.birthy.R;
import com.heckteck.birthy.ViewModels.AddBirthdayViewModel;
import com.yalantis.ucrop.UCrop;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;


public class AddBirthdayFragment extends Fragment {

    private static final int CONTACTS_REQUEST_CODE = 104;
    private static final int CONTACT_PIKE_CODE = 105;
    private static final int DATE_PICKER_FRAGMENT_NO_YEAR = 2;
    private static final int DATE_PICKER_FRAGMENT_YEAR = 1;
    private static final int IMAGE_PIC_GALLERY_CODE = 103;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int TIME_PICKER_FRAGMENT = 3;

    private CircleImageView userImg;
    private EditText et_name, et_dob, et_phone, et_notes, et_timePicker;
    private CheckBox cb_isKnowYear;
    private FloatingActionButton fab_done;
    private Uri imgUri;
    private String name, dob, phone, notes, timeToWish, currentDateTime;
    private AddBirthdayViewModel addBirthdayViewModel;
    private Button btn_attachContact;
    int day, hour, minute, month;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    private boolean isYearKnow = false;
    boolean isEditMode;
    int birthdayId;
    Bundle bundle;
    private Birthday mBirthday = null;

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
        cb_isKnowYear = view.findViewById(R.id.checkKnowYear);
        btn_attachContact = view.findViewById(R.id.attachContact);

        addBirthdayViewModel = ViewModelProviders.of(getActivity()).get(AddBirthdayViewModel.class);

        bundle = getArguments();
        if (bundle != null) {
            birthdayId = bundle.getInt("BIRTH_ID");
            addBirthdayViewModel.getBirthday(birthdayId).observe(getActivity(), new Observer<Birthday>() {
                @Override
                public void onChanged(Birthday birthday) {
                    mBirthday = birthday;
                    et_name.setText(birthday.getName());
                    et_dob.setText(birthday.getBirthDate());
//                    cb_isKnowYear.setChecked(!birthday.isYearKnow());
                    isYearKnow = birthday.isYearKnow();
                    et_phone.setText(birthday.getPhoneNumber());
                    et_notes.setText(birthday.getNotes());
                    et_timePicker.setText(birthday.getTimeToWish());
                    imgUri = Uri.parse(birthday.getUserImg());

                    if (birthday.getUserImg().equals("null")) {
                        userImg.setImageResource(R.drawable.ic_userimg);
                    } else {
                        userImg.setImageURI(Uri.parse(birthday.getUserImg()));
                    }
                }
            });
        } else {
            Log.d("ADD_BDAY", "Bundle is null");
        }

//        bundle = getArguments();
//        if (bundle != null) {
//            birthdayId = bundle.getInt("BIRTH_ID");
//            isEditMode = bundle.getBoolean("isEditMode");
//            addBirthdayViewModel.getBirthday(birthdayId).observe(getActivity(), new Observer<Birthday>() {
//                @Override
//                public void onChanged(Birthday birthday) {
//
//                    et_name.setText(birthday.getName());
//                    et_dob.setText(birthday.getBirthDate());
//                    cb_isKnowYear.setChecked(birthday.isYearKnow());
//                    et_phone.setText(birthday.getPhoneNumber());
//                    et_notes.setText(birthday.getNotes());
//                    et_timePicker.setText(birthday.getTimeToWish());
//
//                    if (birthday.getUserImg().equals("null")) {
//                        userImg.setImageResource(R.drawable.ic_userimg);
//                    } else {
//                        userImg.setImageURI(Uri.parse(birthday.getUserImg()));
//                    }
//                }
//            });
//        } else {
//            Log.e("ADDBDAY", "Bundle is null");
//            isEditMode = false;
//        }

//        if (isEditMode) {
//
//        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        CharSequence formattedTime = DateFormat.format("hh:mm a", calendar);
        et_timePicker.setText(formattedTime);

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askGalleryPermissions();
            }
        });

        cb_isKnowYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isYearKnow = !isChecked;
            }
        });

//        et_dob.setKeyListener(null);
        et_dob.setInputType(InputType.TYPE_NULL);
        et_timePicker.setInputType(InputType.TYPE_NULL);

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_isKnowYear.isChecked()) {
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
//                name = et_name.getText().toString().trim();
//                dob = et_dob.getText().toString().trim();
                if (TextUtils.isEmpty(et_name.getText().toString()) || TextUtils.isEmpty(et_dob.getText().toString())) {
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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


        DateTime currentDate = convertToDateTime(currentDateTime);
        DateTime nextBirthday = convertToDateTime(dob);
        DateTime nextBirthdayTime = nextBirthday.withYear(year);
        if (nextBirthdayTime.isBefore(currentDate) || nextBirthdayTime == currentDate) {
            nextBirthdayTime = nextBirthday.withYear(year + 1);
        }
        Period dateDifferencePeriod = displayBirthdayResult(nextBirthdayTime, currentDate);
        int daysLeft = dateDifferencePeriod.getDays();
        Date birthDate = nextBirthdayTime.toDate();
        Date realBirthdayDate = nextBirthday.toDate();

        Birthday birthday = new Birthday("" + name,
                "" + dob,
                daysLeft,
                "" + phone,
                "" + notes,
                "" + timeToWish,
                "" + currentDateTime,
                "" + imgUri,
                birthDate,
                realBirthdayDate,
                isYearKnow);

        if (mBirthday == null) {
            addBirthdayViewModel.insert(birthday);
            Toast.makeText(getActivity(), "Birthday Added", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            birthday.setId(mBirthday.getId());
            addBirthdayViewModel.updateBirthday(birthday);
            Toast.makeText(getActivity(), "Birthday Updated", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }


        startAlarm();

    }

    private DateTime convertToDateTime(String stringToConvert) {
        String[] newStringArray = convertStingToArray(stringToConvert);
        int year = Integer.parseInt(newStringArray[2].trim());
        int month = Integer.parseInt(newStringArray[1].trim());
        int day = Integer.parseInt(newStringArray[0].trim());
        LocalDate mLocalDate = new LocalDate(year, month, day);
        return mLocalDate.toDateTime(LocalTime.fromDateFields(mLocalDate.toDate()));
    }

    private String[] convertStingToArray(String stringToConvert) {
        return stringToConvert.split("/");
    }

    private Period displayBirthdayResult(DateTime dateToday, DateTime birthdayDate) {
        return new Period(birthdayDate, dateToday, PeriodType.days());
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

        if (now.before(Calendar.getInstance())) {
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

            case IMAGE_PIC_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imgUri = data.getData();
//                    userImg.setImageURI(imgUri);
                    if (imgUri != null) {
                        startCrop(imgUri);
                    }
                }
                break;

            case DATE_PICKER_FRAGMENT_YEAR:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultDate = bundle.getString("RETURNED_DATE_YEAR", "error");
                    month = bundle.getInt("MONTH", 0);
                    day = bundle.getInt("DAY", 0);
                    et_dob.setText(resultDate);
                    isYearKnow = true;
                }
                break;

            case DATE_PICKER_FRAGMENT_NO_YEAR:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String showDob = bundle.getString("SHOW_DOB", "error");
                    et_dob.setText(showDob);
                    dob = bundle.getString("RETURNED_DATE_NO_YEAR", "error");
                    isYearKnow = false;
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

            case UCrop.REQUEST_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    Uri cropImgUri = UCrop.getOutput(data);

                    if (cropImgUri != null) {
                        userImg.setImageURI(cropImgUri);
                    }
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCrop(Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName += ".jpg";
        Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName));

        int random = new Random().nextInt();
        String destURI = null;
        if (destinationUri.toString().contains("jpg")) {
            String str = random + ".jpg";
            destURI = destinationUri.toString().replace(".jpg", str);
        } else if (destinationUri.toString().contains("png")) {
            String str = random + ".png";
            destURI = destinationUri.toString().replace(".png", str);
        }
        destinationUri = Uri.parse(destURI);

        UCrop uCrop = UCrop.of(uri, destinationUri);
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(150, 150);
        uCrop.withOptions(getCropOptions());
        uCrop.start(getContext(), this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(50);
        options.setHideBottomControls(false);
        options.setToolbarTitle("Crop Image");
        return options;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
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

}
