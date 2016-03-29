package com.peter.shwakeyboard;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class RootWarningDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		};
		AlertDialog rootAlert = new AlertDialog.Builder(this).setTitle("Must Root Your Phone").setMessage("You have to root your phone before using ShwaIME.\nIf you didn't root your phone, you can not use ShwaIME!").setPositiveButton("OK", clickListener).create();
		rootAlert.setCanceledOnTouchOutside(false);
		rootAlert.show();
	}
	
}
