package com.peter.shwakeyboard;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

public class FontInstallService extends BroadcastReceiver {

	String fontDirectory="tmpFonts";
	File tmpFontsDir;
	
	File tmpDir, newFont;
	Process terminalProcess;
	DataOutputStream dataOStream;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean isShwaIMEEnabled = intent.getAction().equals(Intent.ACTION_INPUT_METHOD_CHANGED);
		isShwaIMEEnabled &= isInputMethodEnabled(context);
		if (isShwaIMEEnabled){
			if (newFont == null){
				newFont = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fontDirectory + "/Roboto-Regular.ttf");
			}
			if (newFont.exists()){
			}else{
//				if (!checkRootStatus()){
//					Intent rootDialogIntent = new Intent(context, RootWarningDialog.class);
//					rootDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(rootDialogIntent);
//					return;
//				}
//				Intent fontDialogIntent = new Intent(context, FontInstallDialog.class);
//				fontDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(fontDialogIntent);
				fontInstall(context);
			}
		}
			
		
	}
	
	public boolean isInputMethodEnabled(Context ctx) {
	    String id = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

	    ComponentName defaultInputMethod = ComponentName.unflattenFromString(id);

	    ComponentName myInputMethod = new ComponentName(ctx, ShwaIME.class);

	    return myInputMethod.equals(defaultInputMethod);
	}
	
	private boolean checkRootStatus(){
//		try {
//			Runtime.getRuntime().exec("su");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	
	private void fontInstall(Context context){
		try {
		terminalProcess = Runtime.getRuntime().exec("su");
		dataOStream = new DataOutputStream(terminalProcess.getOutputStream());
		tmpDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fontDirectory);
		if (!tmpDir.exists()) tmpDir.mkdir();
		
		newFont = new File(tmpDir.getAbsolutePath() + "/Roboto-Regular.ttf");
		InputStream is = context.getAssets().open("Roboto-Regular.ttf");
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
