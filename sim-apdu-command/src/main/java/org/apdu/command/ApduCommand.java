package org.apdu.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApduCommand {
    SELECT_MF_APDU (new byte[]{(byte) 0xA0, (byte) 0xA4, (byte)0x00, (byte)0x00, (byte)0x02, (byte) 0x3F00}),
    SELECT_DF_TELECOM_APDU(new byte[]{(byte) 0xA0, (byte) 0xA4, (byte)0x00, (byte)0x00, (byte)0x02, (byte) 0x7F10}),
    SELECT_EF_SMS_111_APDU(new byte[]{(byte) 0xA0, (byte) 0xA4, (byte)0x00, (byte)0x00, (byte)0x02, (byte) 0x6F3C}),//FIXME last param
    READ_BINARY_SMS_C_APDU(new byte[]{(byte) 0xA0, (byte) 0xD6, (byte) 0x00, (byte) 0x00, (byte) 0x06,
            (byte) 0x79, (byte) 0x20, (byte) 0x13, (byte) 0x32, (byte) 0x23, (byte) 0x6}),
    UPDATE_BINARY_SMS_C_APDU(new byte[]{(byte) 0xA0, (byte) 0xD6, (byte) 0x00, (byte) 0x00, (byte) 0x06,
            (byte) 0x79, (byte) 0x20, (byte) 0x13, (byte) 0x32, (byte) 0x23, (byte) 0x6});

    private final byte[] value;
}
