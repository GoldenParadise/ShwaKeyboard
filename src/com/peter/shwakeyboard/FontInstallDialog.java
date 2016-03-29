package com.peter.shwakeyboard;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class FontInstallDialog extends Activity {

	File tmpDir, newFont;
	Process terminalProcess;
	DataOutputStream dataOStream;
	String fontDirectory="tmpFonts";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				fontInstall();
				finish();
			}
		};
		AlertDialog fontAlert = new AlertDialog.Builder(this).setTitle("Install ShwaFonts.").setMessage("You have to install Shwa Fonts before using ShwaIME.\nYour phone will be rebooted.").setPositiveButton("OK", clickListener).create();
		fontAlert.setCanceledOnTouchOutside(false);
		fontAlert.show();
	}
	
	private void fontInstall(){
			try {
			terminalProcess = Runtime.getRuntime().exec("su");
			dataOStream = new DataOutputStream(terminalProcess.getOutputStream());
			tmpDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fontDirectory);
			if (!tmpDir.exists()) tmpDir.mkdir();
			
			newFont = new File(tmpDir.getAbsolutePath() + "/Roboto-Regular.ttf");
			InputStream is = getAssets().open("Roboto-Regular.ttf");
		    int size = is.available();
		    byte[] buffer = new byte[size];
		    is.read(buffer);
		    is.close();
		    FileOutputStream fos = new FileOutputStream(newFont);
		    fos.write(buffer);
		    fos.close();
		    
		    dataOStream.writeBytes("mount -o remount /system\n");
		    dataOStream.writeBytes("cp " + tmpDir + "/Roboto-Regular.ttf /system/fonts/Roboto-Regular.ttf\n");
		    dataOStream.writeBytes("chmod 644 /system/fonts/Roboto-Regular.ttf\n");
		    dataOStream.writeBytes("reboot\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Nikoaly", "IOException" + e);
			e.printStackTrace();
		}
	}
	
}
