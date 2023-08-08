package org.apdu.utils;

import lombok.extern.slf4j.Slf4j;

import javax.smartcardio.ResponseAPDU;

@Slf4j(topic = "SimUtils")
public class SimUtils {

    /**
     * Метод, который преобразует байты в hex.
     *
     * @param bytes входное значение байтов
     * @return строка в hex
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }

    /**
     * Метод для проверки, что вернулся response с ошибкой.
     *
     * @param response - R-APDU
     * @return false - если ошибки нет и true - если ошибка есть
     */
    public static boolean hasErrResponse(final byte[] response) {
        if (response[0] == (byte)0x9F) {
            log.info("Результат: " + byteToHexString(response));
            return false;
        } else {
            log.info("Ошибка выполнения команды: " + byteToHexString(response));
        }
        return true;
    }

    /**
     * Метод для проверки, что вернулся response с ошибкой.
     *
     * @param response - R-APDU
     * @return false - если ошибки нет и true - если ошибка есть
     */
    public static boolean hasErrResponse(final ResponseAPDU response) {
        if (response.getSW1() == (byte)0x9F) {
            System.out.println("Результат: " + Integer.toHexString(response.getSW()));
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
        }
        return true;
    }

    /**
     * Метод для проверки, что вернулся response с ошибкой при READ BINARY.
     *
     * @param response - R-APDU
     * @param data - данные ответа
     * @return false - если ошибки нет и true - если ошибка есть
     */
    public static boolean hasErrReadSmsCenter(final byte[] response, final byte[] data) {
        if (response[0] == (byte)0x90 && response[1] == (byte)0x00) {
            // Конвертируем данные из формата BCD в строку
            String smsCenterNumber = byteToHexString(data);
            String formattedSmsCenterNumber = formatPhoneNumber(smsCenterNumber);

            log.info("Результат: " + byteToHexString(data) + " " + byteToHexString(response));
            log.info("Текущий номер SMS-центра: " + formattedSmsCenterNumber);
            return false;
        } else {
            log.info("Ошибка выполнения команды: " + byteToHexString(response));
        }

        return true;
    }

    /**
     * Метод для проверки, что вернулся response с ошибкой при READ BINARY.
     *
     * @param response - R-APDU
     * @return false - если ошибки нет и true - если ошибка есть
     */
    public static boolean hasErrReadSmsCenter(final ResponseAPDU response) {
        if (response.getSW() == (byte)0x9000) {
            // Получаем данные ответа
            byte[] responseData = response.getData();
            // Конвертируем данные из формата BCD в строку
            String smsCenterNumber = SimUtils.byteToHexString(responseData);
            log.info("Текущий номер SMS-центра: " + smsCenterNumber);
            return false;
        } else {
            log.info("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
        }

        return true;
    }

    /**
     * Метод, который преобразует номер SMC центра в удобный на пользователя формат.
     * Из 2143658790F0 в +12345678900
     * @param number - номер SMS центра в формате Packed BCD
     * @return - номер SMS центра в другом формате
     */
    public static String formatPhoneNumber(String number) {
        // Удаление всех символов, кроме цифр
        String digits = number.replaceAll("\\D", "");

        // Форматирование номера
        StringBuilder stringBuilder = new StringBuilder("+");

        int i = 0;
        int j = 1;
        while (j < digits.length()) {
            stringBuilder.append(digits.charAt(j));
            stringBuilder.append(digits.charAt(i));

            i += 2;
            j += 2;
        }

        if(j == digits.length()) {
            stringBuilder.append(digits.charAt(i));
        }

        return stringBuilder.toString();
    }
}
