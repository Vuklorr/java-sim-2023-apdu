package org.apdu.sim;

import lombok.Data;

/**
 * Класс, который эмулирует SIM-карту, для взаимодействия с ней.
 * idCurrentDir - id текущей директории в SIM - карте
 * smsCenterNumberBCD - номер смс центра.
 */
@Data
public class DefaultSim {

    /**
     * IMSI - международный идентификатор мобильного абонента.
     * Он находится в файле EFimsi, который состоит из 9 байт:
     * 1 байт - показывает сколько цифр в IMSI
     * 2 - 9 байт - номер SMS центра (незначимые цифры помечаются FF).
     * Номер хранится в формате Packed BCD (binary coded decimal):
     * каждый десятичный разряд числа записывается в виде его четырёхбитного двоичного кода
     * +1(234)567-89-00 -> 21 43 65 87 90 F0
     */
    private byte[] imsiDataBCD;

    public DefaultSim(byte[] smsCenterNumberBCD) {
        this.imsiDataBCD = smsCenterNumberBCD;
    }

}
