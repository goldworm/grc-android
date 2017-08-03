package com.goldworm.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.goldworm.grc.Default;
import com.goldworm.grc.R;
import com.goldworm.net.RemoteControl;
import com.goldworm.net.VirtualKey;

public class MouseImageView extends View {
	
	private static final String TAG = "KEYBOARD";
	
	private int range;
	private RemoteControl remoteControl;
	private boolean altOn = false;
	private boolean ctrlOn = false;
	private boolean shiftOn = false;
	private int[] keyCodes = new int [4];
	private String composingText = "";
	private InternalInputConnection inputConnection;
	private MousePanelController mousePanelController;
	
	private Paint paint = new Paint();
	private Rect rect = new Rect();
	
	public MouseImageView(Context context) {
		super(context);
		init(context);
	}

	public MouseImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MouseImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mousePanelController.setViewSize(w, h);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void init(Context context) {
		float scale = this.getResources().getDisplayMetrics().density;
		range = (int) (Default.VSCROLL_RANGE * scale + 0.5f);
		remoteControl = RemoteControl.getInstance();

		mousePanelController = new MousePanelController(context);
		mousePanelController.setViewSize(this.getMeasuredWidth(), this.getHeight());
		this.setOnTouchListener(mousePanelController);
	}
	
	public String getComposingText() {
		return this.composingText;
	}
	
	public void setComposingText(String text) {
		
		if( !composingText.equals(text) ) {
			this.composingText = text;
			this.invalidate();
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		final int width = getWidth();
		final int height = getHeight();
		
		drawVScrollRange(canvas, width, height);
		drawHScollRange(canvas, width, height);
		drawMousePadText(canvas, width, height);
		drawComposingText(canvas, width, height);
	}
	
	void drawVScrollRange(Canvas canvas, int width, int height) {
		paint.setARGB(255, 0, 0, 50);
		paint.setStyle(Paint.Style.FILL);
		rect.set(width-range, 0, width, height);
		canvas.drawRect(rect, paint);
	}
	
	void drawHScollRange(Canvas canvas, int width, int height) {
		paint.setARGB(128, 0, 100, 0);
		rect.set(0, height-range, width-range, height);
		canvas.drawRect(rect, paint);
	}
	
	void drawMousePadText(Canvas canvas, int width, int height) {
		paint.setARGB(255, 120, 120, 120);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(20.0f);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		String text = this.getResources().getString(R.string.mouse_pad);
		float x = width * 0.5f;
		float y = height * 0.5f;
		canvas.drawText(text, x, y, paint);
	}
	
	void drawComposingText(Canvas canvas, int width, int height) {
		paint.setARGB(255, 150, 150, 150);
		paint.setTextSize(40.0f);
		
		float x = width * 0.5f;

		canvas.drawText(composingText, x, 110, this.paint);
	}
	
	@Override
	public void clearFocus() {
		setComposingText("");
		super.clearFocus();
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if( event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
			Log.d(TAG, "onKeyPreIme()");
			setComposingText("");
//			return false;
		}
		
		return super.onKeyPreIme(keyCode, event);
	}

	final public void setAltKeyOn(boolean on) {
		altOn = on;
	}

	final public void setCtrlKeyOn(boolean on) {
		ctrlOn = on;
	}

	final private void controlKeyboard(int[] keyCodes, int index, int length) {
		int offset = 0;

		offset = getSpecialKeyCode(this.keyCodes, offset);
		
		for(int i=0; i<length; i++) {
			this.keyCodes[offset + i] = keyCodes[index + i];
		}
		
		int count = offset + length;		
		remoteControl.controlKeyboard(count, this.keyCodes);
	}
	
	/**
	 * Consider Alt and Ctrl keys when making a keycode.
	 * @param keyCodes: 0: count, 1: keyCode
	 */
	final private int getSpecialKeyCode(int[] keyCodes, int offset) {
		int j = 0;
		
		boolean[] metaKeyOn = { altOn, ctrlOn, shiftOn };
		final int size = metaKeyOn.length;
		
		final int[] toggleButtonKeyCodes = {
			VirtualKey.VK_ALT, VirtualKey.VK_CTRL, VirtualKey.VK_SHIFT,
		};
		
		for(int i = 0; i < size; i++) {
			if(metaKeyOn[i]) {
				keyCodes[offset + j] = toggleButtonKeyCodes[i];
				j++;
			}
		}

		return j;
	}
	
	@Override
	public boolean onCheckIsTextEditor() {
		Log.d(TAG, "onCheckIsTextEditor()");
		return true;
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		Log.d(TAG, "onCreateInputConnection()");
		
		if(inputConnection == null) {
			inputConnection = new InternalInputConnection(this);
		}
		
		return inputConnection;
	}
	
	class InternalInputConnection extends BaseInputConnection {

		MouseImageView mouseView;
		
		public InternalInputConnection(MouseImageView mouseView) {
			super(mouseView, false);
			this.mouseView = mouseView;
		}
	
		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			Log.d(TAG, "commitText(text:" + text + " pos:" + newCursorPosition + ")");
			
			if(text.length() == 0)
			{
				// for Samsung hangul keyboard
				text = mouseView.getComposingText();
				mouseView.setComposingText("");
			}
			
			byte[] textBytes = text.toString().getBytes();
			
			// If this is an ascii-code character
			if(textBytes.length == 1 && (textBytes[0] & 0x80) == 0) {
				
				String composingText = mouseView.getComposingText();
				if(composingText.length() > 0) {
					remoteControl.sendText(composingText);
				}
				
				int[] keyCodes = new int [2];
				int length = VirtualKey.convertASCIIToKeyCode((int) textBytes[0], keyCodes, 0);
				mouseView.controlKeyboard(keyCodes, 0, length);
			}
			else {
				remoteControl.sendText(text.toString());
			}
	
			mouseView.setComposingText("");
			return true;
		}
	
		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			Log.d(TAG, "sendKeyEvent(code:" + event.getKeyCode() + ")");
			
			if(event.getAction() == KeyEvent.ACTION_DOWN) {
				int count;
				int offset = 0;
	
				offset = getSpecialKeyCode(keyCodes, offset);
				count = VirtualKey.convertKeyCode(event, keyCodes, offset);
	
				if (count > 0) {
					count += offset;
					remoteControl.controlKeyboard(count, keyCodes);
				}
			}
			
			return true;
		}
	
		@Override
		public boolean setComposingText(CharSequence text, int newCursorPosition) {
			Log.d(TAG, "setComposingText(text:" + text + " pos:" + newCursorPosition + ")");
			mouseView.setComposingText(text.toString());
			return true;
		}
	}
}