package org.apdu.ef;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс, который эмулирует SIM-карту, для взаимодействия с ней.
 * Данные sim карты находятся в файле EF smsp. Y = 0;
 */
@Data
@AllArgsConstructor
public class DefaultEfSmsP {

    /**
     * Каждый бит отвечает за присутствие или отсутствие того, или иного параметра
     * 0 - присутствует
     * 1 - отсутствует
     */
    private byte parameterIndicator;

    /**
     * Адрес (номер телефона) получателя (12 байт)
     */
    private byte[] destinationAddress;

    /**
     * Состоит из 12 байт.
     * Первый равен количеству байт номера.
     * Второй тип номера (+7 -> 0x91)
     * Следующие 10 байт номер.
     * Номер хранится в формате Packed BCD (binary coded decimal):
     * каждый десятичный разряд числа записывается в виде его четырёхбитного двоичного кода
     * +1(234)567-89-00 -> 21 43 65 87 90 F0 FF FF FF FF
     */
    private byte[] serviceCentreAddress;

    /**
     * Байт идентификатора протокола.
     */
    private byte protocolId;

    /**
     * Схема кодирования данных.
     * 0x00 - 7битный алфавит
     */
    private byte dataCodingScheme;

    /**
     * Байт периода действия (время жизни сообщения)
     */
    private byte validPeriod;
}
