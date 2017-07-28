package com.goldworm.net;

/**
 * Created by goldworm on 2017-07-28.
 */

public class Constants {
    // COMMAND TYPE
    public static final int CMD_GET_APP_LIST = 0;
    public static final int CMD_SET_APP = 1;
    public static final int CMD_SHORTCUT = 2;
    public static final int CMD_SYSTEM_CONTROL = 3;
    public static final int CMD_SERVER = 4;
    public static final int CMD_MOUSE = 5;
    public static final int CMD_KEYBOARD = 6;
    public static final int CMD_TEXT = 7;
    public static final int CMD_FILE_BROWSE = 8;
    public static final int CMD_EXIT = -1;

    // SERVER COMMAND SUBTYPE
    public static final int SERVER_AUTHENTICATE = 0;
    public static final int SERVER_DETECTION = 1;

    // SHORTCUT COMMAND SUBTYPE
    public static final int SHORTCUT_KEY = 0;
    public static final int SHORTCUT_APP_LIST = 1;
    public static final int SHORTCUT_TITLE = 2;
    public static final int SHORTCUT_OPEN = 3;

    // Shortcut Action
    public static final int ACTION_JUMP_BACKWARD = 0;
    public static final int ACTION_JUMP_FORWARD = 1;
    public static final int ACTION_PREV = 2;
    public static final int ACTION_NEXT = 3;
    public static final int ACTION_PLAY_AND_PAUSE = 4;
    public static final int ACTION_PAUSE = 5;
    public static final int ACTION_STOP = 6;
    public static final int ACTION_FULL_SCREEN = 7;
    public static final int ACTION_DOWN = 8;
    public static final int ACTION_UP = 9;
    public static final int ACTION_MUTE = 10;
    public static final int ACTION_SUBTITLE = 11;
    public static final int ACTION_LEFT = 12;
    public static final int ACTION_RIGHT = 13;
    // trump
    public static final int ACTION_CLOVER = 14;
    public static final int ACTION_HEART = 15;
    public static final int ACTION_DIAMOND = 16;
    public static final int ACTION_SPADE = 17;
    //subtitle
    public static final int ACTION_SUBTITLE_BACKWARD = 18;
    public static final int ACTION_SUBTITLE_RESET = 19;
    public static final int ACTION_SUBTITLE_FORWARD = 20;
    //audio
    public static final int ACTION_AUDIO_BACKWARD = 21;
    public static final int ACTION_AUDIO_RESET = 22;
    public static final int ACTION_AUDIO_CHANGE = 23;
    public static final int ACTION_AUDIO_FORWARD = 24;
    // playstation
    public static final int ACTION_CIRCLE= 25;
    public static final int ACTION_CROSS = 26;
    public static final int ACTION_RECTANGLE = 27;
    public static final int ACTION_TRIANGLE = 28;

    public static final int ACTION_POWER = 0x0100;
    public static final int ACTION_FOCUS = 0x0200;
    public static final int ACTION_SHUTDOWN = 0x0300;
    public static final int ACTION_RESTART = 0x0400;
    public static final int ACTION_PING = 0x0500;
    public static final int ACTION_VOLUME_UP = 0x0600;
    public static final int ACTION_VOLUME_DOWN = 0x0700;
    public static final int ACTION_HIDE = 0x0800;
    public static final int ACTION_SHOW_HIDE = 0x1000;

    // Mouse Action
    public static final int ACTION_MOVE = 0x00000001;
    public static final int ACTION_LBUTTONDOWN = 0x00000002;
    public static final int ACTION_MBUTTONDOWN = 0x00000004;
    public static final int ACTION_RBUTTONDOWN = 0x00000008;
    public static final int ACTION_LBUTTONUP = 0x00000020;
    public static final int ACTION_MBUTTONUP = 0x00000040;
    public static final int ACTION_RBUTTONUP = 0x00000080;
    public static final int ACTION_LBUTTONCLICK = 0x00000022;
    public static final int ACTION_MBUTTONCLICK = 0x00000044;
    public static final int ACTION_RBUTTONCLICK = 0x00000088;
    public static final int ACTION_VSCROLL = 0x00001000;
    public static final int ACTION_HSCROLL = 0x00002000;
    public static final int ACTION_MAGNIFIER_START = 0x01000000;
    public static final int ACTION_MAGNIFIER_STOP = 0x02000000;

    // System SubType
    public static final int SYSTEM_SHUTDOWN = 0;
    public static final int SYSTEM_REBOOT = 1;
    public static final int SYSTEM_LOGOFF = 2;
    public static final int SYSTEM_HIBERNATE = 3;
    public static final int SYSTEM_LOCK_SCREEN = 4;
    public static final int SYSTEM_GET_VOLUME = 5;
    public static final int SYSTEM_MONITOR = 6;
    public static final int SYSTEM_SET_VOLUME = 7;
    public static final int SYSTEM_SUSPEND = 8;
    public static final int SYSTEM_NON_SLEEPING_MODE = 9;

    // FileBrowse SubType
    public static final int FILE_BROWSE_CD = 0;
    public static final int FILE_BROWSE_DELETE = 1;
    public static final int FILE_BROWSE_LIST = 2;
    public static final int FILE_BROWSE_CWD = 3;
    public static final int FILE_BROWSE_RENAME = 4;
}
