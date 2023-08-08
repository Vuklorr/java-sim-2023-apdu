package org.apdu.sim;

import lombok.Data;

/**
 * Класс, который эмулирует SIM-карту, для взаимодействия с ней.
 * idCurrentDir - id текущей директории в SIM - карте
 * smsCenterNumberBCD - номер смс центра.
 */
@Data
public class DefaultSim {

    private byte idCurrentDir;

    /**
     * Номер хранится в формате Packed BCD (binary coded decimal):
     * каждый десятичный разряд числа записывается в виде его четырёхбитного двоичного кода
     * +1(234)567-89-00 -> 21 43 65 87 90 F0
     */
    private byte[] smsCenterNumberBCD;

    public DefaultSim(byte[] smsCenterNumberBCD) {
        this.smsCenterNumberBCD = smsCenterNumberBCD;
    }

}
