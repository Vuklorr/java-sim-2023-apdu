package org.apdu.sim;

import lombok.Data;

@Data
public class Sim {
    private final byte[] AID = { (byte) 0xA0, 0x00, 0x00, 0x00, 0x62, 0x03, 0x01, 0x0C };
    // +1(234)567-89-00
    // Номер хранится в 2-х форматах: ACII и Packed BCD

    // ACII: на основе стадарта - 3GPP 31.102
    private byte[] smsCenterNumberACII = { (byte) 0x2B, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34
            , (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x30, (byte) 0x30};

    // Packed BCD: на основе стадарта - 3GPP TS 23.038
    private byte[] smsCenterNumberBCD = { (byte) 0x50, (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87
            , (byte) 0x09, (byte) 0xF0};
}
