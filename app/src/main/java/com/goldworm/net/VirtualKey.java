package com.goldworm.net;

import android.util.Log;
import android.view.KeyEvent;

final public class VirtualKey {
	public static final int VK_0 = '0';
	public static final int VK_1 = '1';
	public static final int VK_2 = '2';
	public static final int VK_3 = '3';
	public static final int VK_4 = '4';
	public static final int VK_5 = '5';
	public static final int VK_6 = '6';
	public static final int VK_7 = '7';
	public static final int VK_8 = '8';
	public static final int VK_9 = '9';
	
	public static final int VK_F1 = 0x70;
	public static final int VK_F2 = 0x71;
	public static final int VK_F3 = 0x72;
	public static final int VK_F4 = 0x73;
	public static final int VK_F5 = 0x74;
	public static final int VK_F6 = 0x75;
	public static final int VK_F7 = 0x76;
	public static final int VK_F8 = 0x77;
	public static final int VK_F9 = 0x78;
	public static final int VK_F10 = 0x79;
	public static final int VK_F11 = 0x7A;
	public static final int VK_F12 = 0x7B;
	
	public static final int VK_ENTER = 0x0D;
	public static final int VK_STAR = 0x6A;
	
	public static final int VK_LEFT		= 0x25;
	public static final int VK_UP		= 0x26;
	public static final int VK_RIGHT	= 0x27;
	public static final int VK_DOWN		= 0x28;
	public static final int VK_SPACE	= 0x20;
	public static final int VK_PAGEUP	= 0x21;
	public static final int VK_PAGEDOWN	= 0x22;
	
	public static final int VK_NUMPAD0	= 0x60;
	
	public static final int VK_SHIFT	= 0x10;
	public static final int VK_CTRL		= 0x11;
	public static final int VK_ALT		= 0x12;
	public static final int VK_PAUSE	= 0x13;
	public static final int VK_HANGUL	= 0x15;
	public static final int VK_CONVERT	= 0x1C; // IME convert
	public static final int VK_LWIN		= 0x5B;
	public static final int VK_END		= 0x23;
	public static final int VK_HOME		= 0x24;
	public static final int VK_INSERT	= 0x2D;
	public static final int VK_DELETE	= 0x2E;
	public static final int VK_BACK		= 0x08;
	public static final int VK_TAB		= 0x09;
	public static final int VK_ESCAPE	= 0x1B;
	public static final int VK_OEM_1	= 0xBA;	// ;:
	public static final int VK_OEM_PLUS = 0xBB; // +=
	public static final int VK_OEM_COMMA = 0xBC; // ,<
	public static final int VK_OEM_MINUS = 0xBD; // -_
	public static final int VK_OEM_PERIOD = 0xBE; // .>
	public static final int VK_OEM_2	= 0xBF;	// /?
	public static final int VK_OEM_3	= 0xC0;	// `~
	public static final int VK_OEM_4	= 0xDB;	// [{
	public static final int VK_OEM_5	= 0xDC;	// \|
	public static final int VK_OEM_6	= 0xDD; // ]}
	public static final int VK_OEM_7	= 0xDE; // '"
//	public static final int VK_OEM_8	= 0xDF; // 
	public static final int VK_PROCESSKEY = 0xE5; // IME(?)

	public static final int VK_MULTIPLY = 0x6A;
	public static final int VK_ADD = 0x6B;
	public static final int VK_SUBTRACT = 0x6D;
	public static final int VK_DIVIDE = 0x6F;
	
	private static int[] keyMapper;
	private static int[] asciiCodeMapper;
	
	static {
		keyMapper = new int [256];
		
		int i;

		int size = keyMapper.length;
		for(i=0; i<size; i++) {
			keyMapper[i] = -1;
		}
		
		for(i=KeyEvent.KEYCODE_0; i <= KeyEvent.KEYCODE_9; i++) {
			keyMapper[i] = VK_0 + i - KeyEvent.KEYCODE_0;
		}
		
		for(i=KeyEvent.KEYCODE_A; i <= KeyEvent.KEYCODE_Z; i++) {
			keyMapper[i] = 'A' + i - KeyEvent.KEYCODE_A;
		}

		keyMapper[KeyEvent.KEYCODE_ENTER] = VK_ENTER;
		keyMapper[KeyEvent.KEYCODE_DEL] = VK_BACK;
		keyMapper[KeyEvent.KEYCODE_SPACE] = VK_SPACE;
		keyMapper[KeyEvent.KEYCODE_STAR] = VK_STAR;
		keyMapper[KeyEvent.KEYCODE_POUND] = VK_SHIFT | (VK_3 << 8);
		keyMapper[KeyEvent.KEYCODE_AT] = VK_SHIFT | (VK_2) << 8;
		keyMapper[KeyEvent.KEYCODE_SLASH] = VK_OEM_2;
		keyMapper[KeyEvent.KEYCODE_GRAVE] = VK_OEM_3;
		keyMapper[KeyEvent.KEYCODE_COMMA] = VK_OEM_COMMA;
		keyMapper[KeyEvent.KEYCODE_PERIOD] = VK_OEM_PERIOD;
		keyMapper[KeyEvent.KEYCODE_SEMICOLON] = VK_OEM_1;
		keyMapper[KeyEvent.KEYCODE_APOSTROPHE] = VK_OEM_7;
		keyMapper[KeyEvent.KEYCODE_BACKSLASH] = VK_OEM_5;
		keyMapper[KeyEvent.KEYCODE_MINUS] = VK_OEM_MINUS;
		keyMapper[KeyEvent.KEYCODE_EQUALS] = VK_OEM_PLUS;
		keyMapper[KeyEvent.KEYCODE_PLUS] = VK_ADD;
		keyMapper[KeyEvent.KEYCODE_LEFT_BRACKET] = VK_OEM_4;
		keyMapper[KeyEvent.KEYCODE_RIGHT_BRACKET] = VK_OEM_6;
	}
	
	static {
		int i;
		asciiCodeMapper = new int [127];
		final int shiftKeyMask = 0x100;
		
		// space key
		asciiCodeMapper[' '] = VK_SPACE;
		
		// alphabet
		int alphabet;
		for(i=0; i<26; i++) {
			alphabet = 'A' + i;
			asciiCodeMapper[alphabet] = shiftKeyMask | alphabet;
			asciiCodeMapper[alphabet+32] = alphabet; 
		}
		
		// number keys
		final int[] symbolsOnNumberKey = { ')', '!', '@', '#', '$', '%', '^', '&', '*', '(' };
		for(i=0; i<10; i++) {
			asciiCodeMapper['0' + i] = VK_0 + i;
			asciiCodeMapper[symbolsOnNumberKey[i]] = shiftKeyMask | (VK_0 + i);
		}
		
		// other symbols
		final int[][] symbols = {
			{'`', '~'}, {'-', '_'}, {'=', '+'}, {'\\', '|'},
			{'[', '{'}, {']', '}'},
			{';', ':'}, {'\'', '"'},
			{',', '<'}, {'.', '>'}, {'/', '?'}
		};
		final int[] keyCodesForSymbols = {
			VK_OEM_3, VK_OEM_MINUS, VK_OEM_PLUS, VK_OEM_5,
			VK_OEM_4, VK_OEM_6,
			VK_OEM_1, VK_OEM_7,
			VK_OEM_COMMA, VK_OEM_PERIOD, VK_OEM_2
		};
		for(i=0; i<keyCodesForSymbols.length; i++) {
			asciiCodeMapper[symbols[i][0]] = keyCodesForSymbols[i];
			asciiCodeMapper[symbols[i][1]] = shiftKeyMask | keyCodesForSymbols[i];
		}
	}
	
	private static int convertKeyCode(int meta, int keyCode) {
		int vkCode = -1;
		
		switch(meta) {
		case 0:
			vkCode = keyMapper[keyCode];
			break;
		case 1:	// SHIFT
			if(keyCode == KeyEvent.KEYCODE_PERIOD) {
				vkCode = VK_SHIFT | (VK_OEM_1 << 8);
			}
			else {
				vkCode = VK_SHIFT | ((keyMapper[keyCode] & 0xFF) << 8);
			}
			break;
		case 2: // ALT
			if(keyCode == KeyEvent.KEYCODE_B) {
				vkCode = VK_SHIFT | (VK_OEM_COMMA << 8);
			}
			else if(keyCode == KeyEvent.KEYCODE_N) {
				vkCode = VK_SHIFT | (VK_OEM_PERIOD << 8);
			}
			break;
		case 3: // ALT + SHIFT
			break;
		}
		
		return vkCode;
	}

	public static int convertKeyCode(KeyEvent event, int[] keyCodes, int offset) {
		int keyCode = event.getKeyCode();
		int meta = event.getMetaState();

		int vkCode = convertKeyCode(meta, keyCode);
		if(vkCode < 0) {
			return -1;
		}
	
		int i;
		int j;
		final int mask = 0xFF;

		for(i=0; i<4; i++) {
			keyCode = vkCode & mask;
			if(keyCode == 0) {
				break;
			}
			
			// If same key already exists, skip it.
			for(j=0; j<offset; j++) {
				if(keyCodes[j] == keyCode) {
					Log.d("KEYBOARD", "skip a keycode: " + keyCode);
					break;
				}
			}
			
			if(j == offset) {
				keyCodes[offset + i] = keyCode;
			}
			
			vkCode >>= 8;
		}
		
		return i;
	}
	
	public static int convertASCIIToKeyCode(int asciiCode, int[] keyCodes, int offset) {
		int keyCode = asciiCodeMapper[asciiCode];
		boolean shiftOn = ((keyCode & 0x100) != 0);
		keyCode &= 0xFF;
		
		int length = 1;
		if(shiftOn) {
			keyCodes[offset] = VK_SHIFT;
			offset++;
			length++;
		}
		
		keyCodes[offset] = keyCode;
		
		return length;
	}
}
