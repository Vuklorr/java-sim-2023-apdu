package org.apdu.ef;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultEfImsi {
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
}
