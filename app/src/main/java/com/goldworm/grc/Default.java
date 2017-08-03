package com.goldworm.grc;

public class Default {
	// Related to admob
	public static final String ADMOB_PUBLISHER_ID = "a14d2490810459d";
	
	public static final int KEY_DOWN = 0x01000000;
	public static final int KEY_UP = 0x02000000;
	public static final int KEY_CLICK = 0x03000000;
	
	// Overall
	public static final String KEY_DELAY_DEFAULT = "500";
	public static final String KEY_PERIOD_DEFAULT = "100";
	public static final String CONNECTION_TIMEOUT_DEFAULT = "1500";
	public static final String INITIAL_TAB_DEFAULT = "1";
	
	// Keyboard
	public static final boolean KEYBOARD_ACTIVATION = true;
	// KEYBOARD(0), MESSAGE(1), MOUSE(2), INPUT_METHOD(3), PREV_MODE(4)
	public static final String KEYBOARD_MODE_DEFAULT = "4";
	public static final int[] KEYBOARD_MODE_NAME = {
		R.string.keyboard,
		R.string.message,
		R.string.mouse,
		R.string.input_method,
	};
	
	// Shortcut
	public static final boolean AUTOMATIC_FOCUS = true;
	
	// Mouse
	public static final int WHEEL_THRESHOLD_DEFAULT = 5;
	public static final int MOUSEMOVE_THRESHOLD_DEFAULT = 5;
	
	// Unit: dp
	public static final int VSCROLL_RANGE = 40;
	
	// FileBrowserActivity
	public static final int DIRECTORY_TEXT_COLOR = 0xFF99BBDD;
	public static final int DRIVE_TEXT_COLOR = 0xFFDDBB99;
	public static final int FILE_TEXT_COLOR = 0xFFDDDDDD;
}
