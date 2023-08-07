package org.apdu;

import org.apdu.command.ApduCommand;
import org.apdu.emulator.MySimEmulator;
import org.apdu.sim.MySim;

public class MyUpdateSmsC {
    public static void main(String[] args) {
        //Подключение к картридеру по протоколу T=0
        //взаимодействие с SIM картой
        MySim mySim = new MySim();
        MySimEmulator simEmulator = new MySimEmulator(mySim);
        //массив байтов, для получения R-APDU
        byte[] responseApdu;

        System.out.print("Переход к MF. ");
        responseApdu = simEmulator.execution(ApduCommand.SELECT_MF_APDU.getValue());

        if(hasErrResponse(responseApdu)) {
            return;
        }

        System.out.print("Переход к DF telecome. ");
        responseApdu = simEmulator.execution(ApduCommand.SELECT_DF_TELECOM_APDU.getValue());

        if(hasErrResponse(responseApdu)) {
            return;
        }

        System.out.print("Переход к EF sms 111. ");
        responseApdu = simEmulator.execution(ApduCommand.SELECT_EF_SMS_111_APDU.getValue());

        if(hasErrResponse(responseApdu)) {
            return;
        }

        //вывод номера SMS центра
        System.out.print("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execution(ApduCommand.READ_BINARY_SMS_C_APDU.getValue());

        if (hasErrReadSmsC(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        //обновление номера sms центра
        System.out.print("Обновление номера SMS центра. ");
        responseApdu = simEmulator.execution(ApduCommand.UPDATE_BINARY_SMS_C_APDU.getValue());

        if (responseApdu[0] == (byte)0x90 && responseApdu[1] == (byte)0x00) {
            System.out.println("Результат: " + byteToHexString(responseApdu));
        } else {
            System.out.println("Ошибка выполнения команды: " + byteToHexString(responseApdu));
            return;
        }

        System.out.print("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execution(ApduCommand.READ_BINARY_SMS_C_APDU.getValue());

        if (hasErrReadSmsC(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        // Закрываем соединение с картой
    }

    private static boolean hasErrResponse(final byte[] response) {
        if (response[0] == (byte)0x9F) {
            System.out.println("Результат: " + byteToHexString(response));
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + byteToHexString(response));
        }
        return true;
    }

    private static boolean hasErrReadSmsC(final byte[] response, final byte[] data) {
        if (response[0] == (byte)0x90 && response[1] == (byte)0x00) {
            // Конвертируем данные из формата BCD в строку
            String smsCenterNumber = byteToHexString(data);
            String formattedSmsCenterNumber = formatPhoneNumber(smsCenterNumber);
            System.out.println("Текущий номер SMS-центра: " + formattedSmsCenterNumber + " " + byteToHexString(response));
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + byteToHexString(response));
        }

        return true;
    }

    /**
     * Метод, который преобразует байты в hex.
     *
     * @param bytes входное значение байтов
     * @return строка в hex
     */
    private static String byteToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }

    private static String formatPhoneNumber(String number) {
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