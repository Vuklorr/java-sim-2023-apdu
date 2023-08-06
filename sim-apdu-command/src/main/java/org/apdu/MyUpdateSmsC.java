package org.apdu;

import org.apdu.command.ApduCommand;
import org.apdu.emulator.MySimEmulator;

public class MyUpdateSmsC {
    public static void main(String[] args) {
        //взаимодействие с картой
        MySimEmulator simEmulator = new MySimEmulator();
        //массив байтов, для получения R-APDU
        byte[] responseApdu;

        //переход к папке MF
        responseApdu = simEmulator.execution(ApduCommand.SELECT_MF_APDU.getValue());

        if(hasErrResponse(responseApdu)) {
            return;
        }

        //переход к папке DF
        responseApdu = simEmulator.execution(ApduCommand.SELECT_DF_TELECOM_APDU.getValue());

        if(hasErrResponse(responseApdu)) {
            return;
        }

        //переход к файлу EF sms 111
        responseApdu = simEmulator.execution(ApduCommand.SELECT_EF_SMS_111_APDU.getValue());

        if(hasErrResponse(responseApdu)) {
            return;
        }

        //вывод номера SMS центра
        responseApdu = simEmulator.execution(ApduCommand.READ_BINARY_SMS_C_APDU.getValue());
        if (hasErrReadSmsC(responseApdu)) {
            return;
        }

        //обновление номера sms центра
        responseApdu = simEmulator.execution(ApduCommand.UPDATE_BINARY_SMS_C_APDU.getValue());

        if (responseApdu[0] == (byte)0x90 && responseApdu[1] == (byte)0x00) {
            System.out.println("Результат: " + Integer.toHexString(responseApdu[0]) + Integer.toHexString(responseApdu[1]));
        } else {
            System.out.println("Ошибка выполнения команды: " + Integer.toHexString(responseApdu[0]) + Integer.toHexString(responseApdu[1]));
            return;
        }

        //вывод номера SMS центра
        responseApdu = simEmulator.execution(ApduCommand.READ_BINARY_SMS_C_APDU.getValue());
        if (hasErrReadSmsC(responseApdu)) {
            return;
        }

        // Закрываем соединение с картой
    }

    private static boolean hasErrResponse(final byte[] response) {
        if (response[0] == (byte)0x9F) {
            System.out.println("Результат: " + Integer.toHexString(response[0]) + Integer.toHexString(response[1]));
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + Integer.toHexString(response[0]) + Integer.toHexString(response[1]));
        }
        return true;
    }

    private static boolean hasErrReadSmsC(final byte[] response) {
        if (response[0] == (byte)0x90 && response[1] == (byte)0x00) {
            // Конвертируем данные из формата BCD в строку
            String smsCenterNumber = bcdToString(response);
            System.out.println("Текущий номер SMS-центра: " + smsCenterNumber);
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + Integer.toHexString(response[0]) + Integer.toHexString(response[1]));
        }

        return true;
    }

    // Метод для преобразования данных из формата BCD в строку
    private static String bcdToString(byte[] bcdData) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bcdData) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}