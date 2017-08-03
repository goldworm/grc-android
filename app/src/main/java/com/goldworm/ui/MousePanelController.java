package com.goldworm.ui;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.goldworm.grc.Default;
import com.goldworm.net.RemoteControl;
import com.goldworm.proto.Constants;

public class MousePanelController implements OnTouchListener {
	
	static final String TAG = "MOUSE";

	private RemoteControl remoteControl;
	private int viewWidth, viewHeight;
	private int mouseAction;
	private boolean mouseMove;
	private float startX;
	private float startY;
	private int wheelThreshold;
	private int mouseMoveTreshold;
	private int scrollRange;
	
	public MousePanelController(Context context) {
		remoteControl = RemoteControl.getInstance();
		wheelThreshold = Default.WHEEL_THRESHOLD_DEFAULT;
		mouseMoveTreshold = Default.MOUSEMOVE_THRESHOLD_DEFAULT;

		final float scale = context.getResources().getDisplayMetrics().density;
		scrollRange = (int) (Default.VSCROLL_RANGE * scale + 0.5f);
	}

	public void setViewSize(int width, int height) {
		viewWidth = width;
		viewHeight = height;
	}
	
	public void setWheelThreshold(int wheelThreshold) {
		this.wheelThreshold = wheelThreshold;
	}

	public void setMouseMoveTreshold(int mouseMoveTreshold) {
		this.mouseMoveTreshold = mouseMoveTreshold;
	}

	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction() & ~MotionEvent.ACTION_POINTER_INDEX_MASK;
				
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			onActionDown(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			onActionPointerDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			onActionMove(event);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			onActionPointerUp(event);
			break;
		case MotionEvent.ACTION_UP:
			onActionUp(event);
			break;
		}
		
		return true;
	}
	
	private void onActionDown(MotionEvent event) {
		startX = event.getX();
		startY = event.getY();
		
		mouseAction = checkMouseAction(event);
		Log.v(TAG, "action: " + mouseAction);
					
		mouseMove = false;
	}
	
	private void onActionPointerDown(MotionEvent event) {
		if(!mouseMove) {
			mouseAction = Constants.ACTION_RBUTTONCLICK;
		}
	}
	
	private void onActionMove(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		int moveX = (int) (x - startX);
		int moveY = (int) (y - startY);

		if(!mouseMove) {
			if( !isMouseMove(moveX, moveY) ) {
				return;
			}

			if(mouseAction == Constants.ACTION_LBUTTONCLICK) {
				mouseAction = Constants.ACTION_MOVE;
			}
			mouseMove = true;
		}
		
		if(mouseAction == Constants.ACTION_RBUTTONCLICK) {
			return;
		}
		
		int pointerCount = event.getPointerCount();
		if(pointerCount > 1) {
			mouseAction = Constants.ACTION_VSCROLL;
		}
		
		switch(mouseAction) {
		case Constants.ACTION_VSCROLL:
			moveY /= wheelThreshold;
			if(Math.abs(moveY) == 0) {
				return;
			}
			moveY *= -1;
			break;
		case Constants.ACTION_HSCROLL:
			moveX /= wheelThreshold;
			if(Math.abs(moveX) == 0) {
				return;
			}
			Log.d(TAG, "HSCROLL");
			break;
		case Constants.ACTION_MOVE:	// ACTION_MOVE, ACTION_LBUTTONDOWN
			moveX = accelerateMouse(moveX);
			moveY = accelerateMouse(moveY);
			break;
		}

		startX = x;
		startY = y;
		
		remoteControl.controlMouse(mouseAction, moveX, moveY);
	}
	
	private void onActionPointerUp(MotionEvent event) {
		if(!mouseMove) {
			remoteControl.controlMouse(Constants.ACTION_RBUTTONCLICK, 0, 0);
		}
	}
	
	private void onActionUp(MotionEvent event) {
		if(!mouseMove && mouseAction == Constants.ACTION_LBUTTONCLICK) {
			// Handles mouse click.
			remoteControl.controlMouse(mouseAction, 0, 0);
		}
	}
	
	private int checkMouseAction(MotionEvent event) {
		
		if(event.getPointerCount() > 1) {
			return Constants.ACTION_RBUTTONCLICK;
		}
		
		float x = event.getX();
		float y = event.getY();
		
		Log.d(TAG, "w:" + viewWidth + " h:" + viewHeight + " x:" + x + " y:" + y + " range:" + scrollRange);

		if(x > viewWidth - scrollRange) {
			return Constants.ACTION_VSCROLL;
		}
		if(y > viewHeight - scrollRange) {
			return Constants.ACTION_HSCROLL;
		}

		return Constants.ACTION_LBUTTONCLICK;
	}
	
	/**
	 * Some phones' touch pads are so sensitive that
	 * ACTION_MOVE event occurs just by touching.  
	 * @param moveX
	 * @param moveY
	 * @return
	 */
	private boolean isMouseMove(final int moveX, final int moveY) {
		int move = Math.abs(moveX) + Math.abs(moveY);
		return (move >= mouseMoveTreshold);
	}
	
	private int accelerateMouse(int move) {
		final int[] ratio = { 10, 11, 12, 13, 14 };
		
		int i = Math.min(4, Math.abs(move / 10));
		move = move * ratio[i] / 10;
		
		return move;
	}
}
