package org.apdu.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApduResponse {
    RESPONSE_SUCCESS (new byte[]{(byte)0x90, (byte)0x00 }),

    //второй байт должен выводить длину значения ответа, но в данной реализации он выводит одно и то же значение,
    // так как оно не участвует в задании.
    RESPONSE_SELECT_SUCCESS (new byte[] {(byte)0x9F, (byte)0x07}),

    RESPONSE_CLA_ERROR (new byte[]{(byte)0x6E, (byte)0x00}),

    RESPONSE_INS_ERROR (new byte[]{(byte)0x6D, (byte)0x00}),

    RESPONSE_LENGTH_ERROR (new byte[]{(byte)0x67, (byte)0x00});


    private final byte[] value;
}
