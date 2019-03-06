package com.doodee.voiceclicker.backend;

import com.doodee.voiceclicker.DooLog;

public class NetworkPacket {

    public static final byte INPUT_TYPE_KEYBOARD = 0;
    public static final byte INPUT_TYPE_MOUSE = 1;
    public static final byte MOUSE_ACTION_LEFT_CLICK = 2;
    public static final byte MOUSE_ACTION_RIGHT_CLICK = 3;
    public static final byte MOUSE_ACTION_MIDDLE_CLICK = 4;
    public static final byte MOUSE_ACTION_MOVE = 5;
    public static final byte MOUSE_ACTION_SCROLL = 6;
    public static final byte MOUSE_ACTION_LEFT_DOWN = 7;
    public static final byte MOUSE_ACTION_LEFT_UP = 8;
    public static final byte KEYBOARD_ACTION_KEYCHAR = 9;
    public static final byte KEYBOARD_ACTION_META = 10;
    public static final byte KEYBOARD_ACTION_EMOJI = 11;
    public static final byte KEYBOARD_ACTION_OTHERS = 12;

    /**
     * This will return buffer of an arrangement of such:
     * [input type(1 byte), action type(1 byte), length of data(1 byte), data max 4 bytes]
     *
     * @param inputType
     * @param actionType
     * @param complimentData
     * @return
     */
    public static byte[] getPacket(byte inputType, byte actionType, byte... complimentData) {
        switch (inputType) {
            case INPUT_TYPE_MOUSE:
                switch (actionType) {
                    case MOUSE_ACTION_LEFT_CLICK:
                        return new byte[]{inputType, actionType, (byte) complimentData.length};
                    case MOUSE_ACTION_RIGHT_CLICK:
                        return new byte[]{inputType, actionType, (byte) complimentData.length};
                    case MOUSE_ACTION_LEFT_DOWN:
                        return new byte[]{inputType, actionType, (byte) complimentData.length};
                    case MOUSE_ACTION_LEFT_UP:
                        return new byte[]{inputType, actionType, (byte) complimentData.length};
                    case MOUSE_ACTION_MIDDLE_CLICK:
                        return new byte[]{inputType, actionType, (byte) complimentData.length};
                    case MOUSE_ACTION_MOVE:
                        if (complimentData.length == 4) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0], complimentData[1], complimentData[2], complimentData[3]};
                        }
                        DooLog.d("Error input for MOUSE ACTION MOVE");
                        return null;
                    case MOUSE_ACTION_SCROLL:
                        if (complimentData.length == 4) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0], complimentData[1], complimentData[2], complimentData[3]};
                        }
                        DooLog.d("Error input for MOUSE ACTION SCROLL");
                        return null;
                }
                DooLog.d("MOUSE_ACTION Type not recognised");
                return null;
            case INPUT_TYPE_KEYBOARD:
                switch (actionType) {
                    case KEYBOARD_ACTION_EMOJI:
                        if (complimentData.length == 4) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0], complimentData[1], complimentData[2], complimentData[3]};
                        }
                        DooLog.d("Error input for KEYBOARD_ACTION_EMOJI");
                        return null;
                    case KEYBOARD_ACTION_KEYCHAR:
                        if (complimentData.length == 1) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0]};
                        }
                        DooLog.d("Error input for KEYBOARD_ACTION_KEYCHAR");
                        return null;
                    case KEYBOARD_ACTION_META:
                        if (complimentData.length == 1) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0]};
                        }
                        DooLog.d("Error input for KEYBOARD_ACTION_META");
                        return null;
                    case KEYBOARD_ACTION_OTHERS:
                        if (complimentData.length == 1) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0]};
                        } else if (complimentData.length == 2) {
                            return new byte[]{inputType, actionType, (byte) complimentData.length, complimentData[0], complimentData[1]};
                        }
                        DooLog.d("Error input for KEYBOARD_ACTION_OTHERS");
                        return null;
                }
                DooLog.d("KEYBOARD_ACTION Type not recognised");
                return null;
        }
        DooLog.d("INPUT_TYPE not recognised");
        return null;
    }
}
