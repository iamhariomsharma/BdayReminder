package com.heckteck.birthy.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.heckteck.birthy.R;

import androidx.annotation.Nullable;

public class NotifyActivity extends Activity {

    Button btn_wish;
    ImageView cancelDialog;
    String name;
    Dialog wishDialog;
    TextView wishingName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wishDialog = new Dialog(this);
        wishDialog.setContentView(R.layout.wishing_ui);
        cancelDialog = wishDialog.findViewById(R.id.cancelDialog);
        wishingName = wishDialog.findViewById(R.id.tv_wishingName);
        btn_wish = wishDialog.findViewById(R.id.btn_sendWishes);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                wishDialog.dismiss();
                finish();
            }
        });
        btn_wish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(NotifyActivity.this, MainActivity.class);
                intent.putExtra("NAME", name);
                intent.putExtra("SEND_DIALOG", "sendWishesDialog");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        wishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wishDialog.show();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            name = intent.getStringExtra("NAME");
            wishingName.setText("It's " + name + "'s Birthday Today!");
        }
    }

    protected void onDestroy() {
        wishDialog.dismiss();
        super.onDestroy();
    }

//    public boolean onTouchEvent(MotionEvent motionEvent) {
//        finish();
//        return super.onTouchEvent(motionEvent);
//    }
}
