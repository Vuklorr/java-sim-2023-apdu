package org.apdu.api;

/**
 * Простой сим эмулятор, который больше похож на mock:
 * отправляется запрос и возвращается ответ.
 * <p>
 * Нужен для эмуляции взаимодействия с SIM - картой
 */
public interface SimEmulator {

    /**
     * Метод для обработки APDU команд.
     *
     * @param apdu - apdu команда (C-APDU)
     * @return - apdu ответ (R-APDU)
     */
    byte[] execute(byte[] apdu);

    /**
     * Метод, который возвращает данные ответа.
     * @return - данные (массив байтов)
     */
    byte[] getResponseData();
}
