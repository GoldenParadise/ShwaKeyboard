package com.peter.shwakeyboard;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class ShwaKeyboardView extends KeyboardView {

	private final GestureDetector gestureDetector;
	private int width, height;
	
	public ShwaKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetector = new GestureDetector(context, new GestureListener());
		// TODO Auto-generated constructor stub
	}

	public ShwaKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		gestureDetector = new GestureDetector(context, new GestureListener());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
        
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
		paint.setTextSize(112);
		paint.setTextAlign(Paint.Align.CENTER);

        paint.setTypeface(custom_font);

        List<Key> keys = getKeyboard().getKeys();
        for(Key key: keys) {
        	width = key.width;
        	height = key.height;
        	if (TextUtils.isEmpty(key.label) && key.icon == null){
        		String label;
        		char character;
        		if (!key.sticky){
	        		if (key.codes[0] == 0xE215 || key.codes[0] == 0xE219 || key.codes[0] == 0xE21D || key.codes[0] == 0xE230){
//	        			paint.setColor(Color.BLUE);
	        			paint.setColor(getResources().getColor(R.color.key_text_blue));
	        		}else{
	        			if (ShwaIME.state[ShwaIME.idx] != 0)
	        				paint.setColor(Color.BLACK);
	        			else
//	        				paint.setColor(Color.BLUE);
	        				paint.setColor(getResources().getColor(R.color.key_text_blue));
	        		}
	        		
	        		if (key.codes[0] == 0xE215 || key.codes[0] == 0xE219 || key.codes[0] == 0xE21D || key.codes[0] == 0xE230){
	        			character = (char)(key.codes[0] - 0x200);
	        		}else{
	        			character = (char)key.codes[0];
	        		}
	        		label = new String();
	        		label += character;
        		}else{
        			if (key.codes[0] == -0x5) character = (char)(9003);
        			else if (key.codes[0] == 0xE200) character = (char)(0x2302);
        			else if (key.codes[0] == 0xE201) character = (char)(0x21BB);
        			else character = (char)(0x21B5);
        			label = new String();
	        		label += character;
	        		paint.setColor(Color.RED);
        		}
        		
        		Rect bounds = new Rect();
        		
        		paint.getTextBounds("aPz", 0, 3, bounds);
        		
        		int x = (key.width)/2;
        		int y = (key.height + bounds.height())/2;
        			
        		canvas.drawText(label, x + key.x, y + key.y, paint);
        	}
        
        }
        paint.setARGB(250, 250, 250, 250);
        paint.setTextAlign(Align.RIGHT);
        if (width != 0 && height != 0){
        	canvas.drawRoundRect(new RectF(7.0f, 7.0f, (float)(width * 4 - 7), (float)(height * 3 - 7)), 14.0f, 14.0f, paint);
        	paint.setColor(Color.BLACK);
        	paint.setTextSize(75);
        	StaticLayout sLayout = StaticLayoutWithMaxLines.create(ShwaIME.previewText, 0, ShwaIME.previewText.length(), paint, 1000000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true, TruncateAt.START, width * 4 - 25, 1);
        	canvas.save();
        	canvas.translate(width * 4 - 20, 25);
        	sLayout.draw(canvas);
        	//canvas.restore();
        }
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		// TODO Auto-generated method stub
		boolean isInside = false;
		for (Key key : getKeyboard().getKeys()){
			if (key.isInside((int)me.getX(), (int)me.getY())){
				isInside = true;
				break;
			}
		}
		if (!isInside) gestureDetector.onTouchEvent(me);
		return super.onTouchEvent(me);
	}
	
	 private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
//                            onSwipeRight();
                        	Log.e("Nikolay", "Right");
                        } else {
                        	Log.e("Nikolay", "Left");
//                            onSwipeLeft();
                        }
                    }
                    result = true;
                } 
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
//                            onSwipeBottom();
                        	Log.e("Nikolay", "Bottom");
                        } else {
//                            onSwipeTop();
                        	Log.e("Nikolay", "top");
                        	Log.e("Nikolay", "Width = " + width + " Height = " + height);
                        }
                    }
                    result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

	
}
