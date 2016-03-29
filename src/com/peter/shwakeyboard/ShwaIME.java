package com.peter.shwakeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class ShwaIME extends InputMethodService implements OnKeyboardActionListener {

	private ShwaKeyboardView shwaKeyboardView;
	private Keyboard shwaKeyboard;
	public static int state[];
	public static int idx;
	public static String previewText="";
	
	private int keyxmls[] = {R.xml.shwa_keyboard_0, R.xml.shwa_keyboard_1, R.xml.shwa_keyboard_2, R.xml.shwa_keyboard_3, R.xml.shwa_keyboard_4, R.xml.shwa_keyboard_5
			, R.xml.shwa_keyboard_6, R.xml.shwa_keyboard_7, R.xml.shwa_keyboard_8, R.xml.shwa_keyboard_9, R.xml.shwa_keyboard_10, R.xml.shwa_keyboard_11, R.xml.shwa_keyboard_12
			, R.xml.shwa_keyboard_13, R.xml.shwa_keyboard_14, R.xml.shwa_keyboard_15, R.xml.shwa_keyboard_16, R.xml.shwa_keyboard_17, R.xml.shwa_keyboard_18, R.xml.shwa_keyboard_19
			, R.xml.shwa_keyboard_20, R.xml.shwa_keyboard_21, R.xml.shwa_keyboard_22, R.xml.shwa_keyboard_23, R.xml.shwa_keyboard_24};
	
	private int home_layout_keycodes[] = {0xE070, 0xE0A8, 0xE0D0, 0xE0F8, 0xE07C, 0xE0A0, 0xE0C8, 0xE0F0, 0xE078, 0xE098, 0xE0C0, 0xE0E8, 0xE074, 0xE090, 0xE0B8, 0xE0E0, 0xE080, 0xE088, 0xE0B0, 0xE0D8};
	@Override
	public void onPress(int primaryCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRelease(int primaryCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
		// TODO Auto-generated method stub
		super.onStartInputView(info, restarting);
		showStatusIcon(R.drawable.statusbar_icon);
		previewText = "";
	}
	
	

	@Override
	public void onStartInput(EditorInfo attribute, boolean restarting) {
		// TODO Auto-generated method stub
		super.onStartInput(attribute, restarting);
	}

	@Override
	public void onFinishInputView(boolean finishingInput) {
		// TODO Auto-generated method stub
		super.onFinishInputView(finishingInput);
		hideStatusIcon();
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		// TODO Auto-generated method stub
		InputConnection ic = getCurrentInputConnection();
	    playClick(primaryCode);
	    boolean keyBoardChange = false;
	    switch(primaryCode){
		    case Keyboard.KEYCODE_DELETE :
		        ic.deleteSurroundingText(1, 0);
		        if (!TextUtils.isEmpty(previewText)) previewText = previewText.substring(0, previewText.length() - 1);
		        break;
//		    case Keyboard.KEYCODE_DONE:
//		        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
//		        break;
		    case 0xE215 :
		    	if ( state[idx] != 1){
		    		idx++;
		    		state[idx]=1;
			    	updateKeyBoard();
		    	}
		    	break;
		    case 0xE219 :
		    	if ( state[idx] != 2){
		    		idx++;
		    		state[idx]=2;
			    	updateKeyBoard();
		    	}
		    	break;
		    case 0xE21D :
		    	if ( state[idx] != 3){
		    		idx++;
		    		state[idx]=3;
			    	updateKeyBoard();
		    	}
		    	break;
		    case 0xE230 :
		    	if ( state[idx] != 4){
		    		idx++;
		    		state[idx]=4;
			    	updateKeyBoard();
		    	}
		    	break;
		    case 0xE200 :
		    	if (idx != 0){
			    	idx = 0;
			    	state[idx] = 0;
			    	keyBoardChange = true;
		    	}
		    	break;
		    case 0xE201 :
		    	if (idx != 0 && state[idx] != 0){
		    		idx --;
		    		keyBoardChange = true;
		    	}
		    	break;
		    default:
		    	if (state[idx] == 0){
		    		int layoutIdx = getLayoutIdx(primaryCode);
		    		if (layoutIdx != 0){
		    			state[++idx] = layoutIdx;
		    			keyBoardChange = true;
		    		}else{
		    			char code = (char)primaryCode;
				        ic.commitText(String.valueOf(code),1);
				        if (primaryCode != 0x000A) previewText = previewText + code;
				        else previewText = "";
		    		}
		    	}else{
		    		if (primaryCode != 0x000A && state[idx] > 4){
		    			idx = 0;
		    			state[idx] = 0;
		    			keyBoardChange = true;
		    		}
	    			char code = (char)primaryCode;
	    			ic.commitText(String.valueOf(code),1);
	    			if (primaryCode != 0x000A) previewText = previewText + code;
	    			else previewText = "";
		    	}
	    }
	    if (keyBoardChange)
	    	updateKeyBoard();
	    shwaKeyboardView.invalidate();
	}

	@Override
	public void onText(CharSequence text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public View onCreateInputView() {
		// TODO Auto-generated method stub
		shwaKeyboardView = (ShwaKeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
		shwaKeyboard = new Keyboard(this, R.xml.shwa_keyboard_0);
		shwaKeyboardView.setKeyboard(shwaKeyboard);
		shwaKeyboardView.setOnKeyboardActionListener(this);
		shwaKeyboardView.setPreviewEnabled(false);
		state = new int[20];
		idx = 0;
		
		return shwaKeyboardView;
	}
	
	/** 
	 * Play sound effects when user press key.  
	 */
	private void playClick(int keyCode){
	    AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
	    switch(keyCode){
		    case Keyboard.KEYCODE_DONE:
		    case 10: 
		        am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
		        break;
		    case Keyboard.KEYCODE_DELETE:
		        am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
		        break;              
		    default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
	    }       
	}
	
	private void updateKeyBoard(){
		shwaKeyboard = new Keyboard(this, keyxmls[state[idx]]);
		shwaKeyboardView.setKeyboard(shwaKeyboard);
	}
	
	private int getLayoutIdx(int primarycode){
		int result = 0;
		for ( int i = 0; i < home_layout_keycodes.length; i++ ){
			if (home_layout_keycodes[i] == primarycode){
				result = 5 + i;
				break;
			}
		}
		return result;
	}
}
