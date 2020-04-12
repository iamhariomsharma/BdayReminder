package com.heckteck.birthy.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heckteck.birthy.Adapters.GreetingsAdapter;
import com.heckteck.birthy.Models.Greeting;
import com.heckteck.birthy.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SendWishesFragment extends Fragment {

    private static final int CALL_REQUEST_CODE = 1;
    private static final int SMS_REQUEST_CODE = 2;

    private String activityToast;

    private boolean canDoCall = false;
    private boolean canSendByWhatsApp = false;
    private boolean canSendMessage = false;

    private List<Greeting> greetingList;

    private Greeting myGreeting;
    private String phoneNumber;
    private TextView sendCall;
    private TextView sendMessage;
    private TextView sendMore;
    private TextView sendWhatsApp;
    private Dialog shareDialog;

    private void asKCallPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
            } else {
                makePhoneCall();
            }
        } else {
            makePhoneCall();
        }
    }

    private void askMessagePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_CODE);
            } else {
                sendSMS();
            }
        } else {
            sendSMS();
        }
    }

    private void makePhoneCall() {
        String callPhone = "tel:" + phoneNumber;
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(callPhone)));
    }

    private void sendSMS() {
        String phone = String.format("smsto: %s", phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(phone));
        intent.putExtra("sms_body", myGreeting.getGreeting());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(getActivity(), "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareByAll() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, myGreeting.getGreeting());
        startActivity(Intent.createChooser(intent, "Share Greetings using:"));
    }

    public SendWishesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_wishes, container, false);

        greetingList = new ArrayList<>();
        greetingList.add(new Greeting(getResources().getString(R.string.app_name)));
//        greetingList.add(new Greeting(getResources().getString(2131820619)));
//        greetingList.add(new Greeting(getResources().getString(2131820620)));
//        greetingList.add(new Greeting(getResources().getString(2131820621)));
//        greetingList.add(new Greeting(getResources().getString(2131820622)));
//        greetingList.add(new Greeting(getResources().getString(2131820623)));
//        greetingList.add(new Greeting(getResources().getString(2131820624)));
//        greetingList.add(new Greeting(getResources().getString(2131820625)));
//        greetingList.add(new Greeting(getResources().getString(2131820626)));
//        greetingList.add(new Greeting(getResources().getString(2131820618)));
        RecyclerView recyclerView = view.findViewById(R.id.greetings_rv);
        recyclerView.setHasFixedSize(true);
        GreetingsAdapter greetingsAdapter = new GreetingsAdapter(getActivity(), greetingList);
        recyclerView.setAdapter(greetingsAdapter);
        greetingsAdapter.setOnItemClickListener(new GreetingsAdapter.OnItemClickListener() {
            public void onItemClick(Greeting greeting) {

                myGreeting = greeting;
//                SendWishesFragment.access$002(SendWishesFragment.this, greeting);
                if (canSendByWhatsApp) {
                    PackageManager packageManager = getActivity().getPackageManager();
                    String str = phoneNumber.replace("+", "").replace(" ", "");
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                        String stringBuilder = "https://api.whatsapp.com/send?phone=" +
                                str +
                                "&text=" +
                                URLEncoder.encode(greeting.getGreeting(), "UTF-8");
                        intent.setData(Uri.parse(stringBuilder));
                        SendWishesFragment.this.startActivity(intent);
                    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
                        Toast.makeText(SendWishesFragment.this.getActivity(), "WhatsApp not Installed!", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    }
                } else if (canSendMessage) {
                    askMessagePermission();
                } else {
                    shareByAll();
                }
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            phoneNumber = bundle.getString("PHONE");
            activityToast = bundle.getString("Activity1");
            String str = activityToast;
            if (str != null && str.equals("activity1")) {
                shareDialog = new Dialog(getActivity());
                shareDialog.setContentView(R.layout.popup_wishes);
                sendWhatsApp = shareDialog.findViewById(R.id.sendWhatsApp);
                sendCall = shareDialog.findViewById(R.id.sendCall);
                sendMessage = shareDialog.findViewById(R.id.sendMessage);
                sendMore = shareDialog.findViewById(R.id.sendMore);
                sendWhatsApp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        canSendByWhatsApp = true;
                        shareDialog.dismiss();
                    }
                });
                sendCall.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param1View) {
                        asKCallPermission();
                        shareDialog.dismiss();
                        Toast.makeText(getActivity(), "Choose greetings you want to send", Toast.LENGTH_SHORT).show();

                    }
                });
                sendMessage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param1View) {
                        canSendMessage = true;
                        shareDialog.dismiss();
                        Toast.makeText(getActivity(), "Choose greetings you want to send", Toast.LENGTH_SHORT).show();
                    }
                });
                sendMore.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        shareByAll();
                        shareDialog.dismiss();
                        Toast.makeText(getActivity(), "Click to send greetings", Toast.LENGTH_LONG).show();
                    }
                });
                shareDialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
                shareDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                shareDialog.show();
                Toast.makeText(getActivity(), "Coming from notification", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), phoneNumber, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Coming from Dialog", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.menuBtn_search);
        if (item != null){
            item.setVisible(false);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                makePhoneCall();
            } else {
                Toast.makeText(getActivity(), "Call Permission is required!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SMS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                sendSMS();
            } else {
                Toast.makeText(getActivity(), "SMS Permission is required!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
