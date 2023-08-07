package org.apdu.sim;

import lombok.Data;

@Data
public class MySim {

    private byte idCurrentDir;
    // Packed BCD (binary coded decimal) - каждый десятичный разряд числа записывается в виде его четырёхбитного двоичного кода
    // 12345678900
    private byte[] smsCenterNumberBCD = {(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87
            , (byte) 0x09, (byte) 0xF0};
}
