package org.apdu.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApduCommand {
    SELECT_MF_APDU (new byte[]{(byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00, (byte)0x02,
            (byte)0x3F, (byte)0x00}),

    SELECT_DF_TELECOM_APDU(new byte[]{(byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00, (byte)0x02,
            (byte)0x7F, (byte)0x10}),

    SELECT_EF_SMSP_APDU(new byte[]{(byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00, (byte)0x02,
            (byte)0x6F, (byte)0x42}),

    READ_RECORD_SMS_CENTER_APDU(new byte[]{(byte)0xA0, (byte)0xB2, (byte)0x03, (byte)0x04, (byte)0x06}),

    UPDATE_RECORD_SMS_CENTER_APDU(new byte[]{(byte)0xA0, (byte)0xDC , (byte)0x03, (byte)0x04, (byte)0x06,
            (byte)0x07, (byte)0x91, (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6}),

    READ_BINARY_SMS_CENTER_APDU(new byte[]{(byte)0xA0, (byte)0xB0, (byte)0x00, (byte)0x01, (byte)0x06}),

    UPDATE_BINARY_SMS_CENTER_APDU(new byte[]{(byte)0xA0, (byte)0xD6 , (byte)0x00, (byte)0x01, (byte)0x06,
            (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6});

    private final byte[] value;
}
