package org.apdu;

import org.apdu.command.ApduCommand;

import javax.smartcardio.*;
import java.util.List;

public class UpdateSmsC {
    public static void main(String[] args) {
        try {
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = terminalFactory.terminals().list();
            CardTerminal terminal = terminals.get(0); // Первый доступный картридер

            // Протокол T=0 является быйтовым, т.е. наименьшая единица данных, которая передается или обрабатывается, - это байт
            // Протокол T=1 является блочно-ориентированным, т.е. наименьшая единица данных, которая передается или обрабатывается,
            // - это блок, состоящий из последовательности байтов.
            Card card = terminal.connect("T=0");
            CardChannel channel = card.getBasicChannel();

            // Отправляем команды на карту и получаем ответ
            System.out.print("Переход к MF. ");
            ResponseAPDU response = channel.transmit(new CommandAPDU(ApduCommand.SELECT_MF_APDU.getValue()));

            if(hasErrResponse(response)) {
                return;
            }

            System.out.print("Переход к DF telecome. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.SELECT_DF_TELECOM_APDU.getValue()));

            if(hasErrResponse(response)) {
                return;
            }

            System.out.print("Переход к EF sms 111. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.SELECT_EF_SMS_111_APDU.getValue()));

            if(hasErrResponse(response)) {
                return;
            }

            System.out.print("Вывод номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.READ_BINARY_SMS_C_APDU.getValue()));

            if (hasErrReadSmsC(response)) {
                return;
            }

            System.out.print("Обновление номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.UPDATE_BINARY_SMS_C_APDU.getValue()));

            if (response.getSW() == 0x9000) {
                System.out.println("Результат: " + Integer.toHexString(response.getSW()));
            } else {
                System.out.println("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
                return;
            }

            System.out.print("Вывод номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.READ_BINARY_SMS_C_APDU.getValue()));

            if (hasErrReadSmsC(response)) {
                return;
            }

            // Закрываем соединение с картой
            card.disconnect(true);
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

    private static boolean hasErrResponse(final ResponseAPDU response) {
        if (response.getSW1() == (byte)0x9F) {
            System.out.println("Результат: " + Integer.toHexString(response.getSW()));
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
        }
        return true;
    }

    private static boolean hasErrReadSmsC(final ResponseAPDU response) {
        if (response.getSW() == (byte)0x9000) {
            // Получаем данные ответа
            byte[] responseData = response.getData();
            // Конвертируем данные из формата BCD в строку
            String smsCenterNumber = byteToHexString(responseData);
            System.out.println("Текущий номер SMS-центра: " + smsCenterNumber);
            return false;
        } else {
            System.out.println("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
        }

        return true;
    }

    // Метод для преобразования данных из формата BCD в строку
    private static String byteToHexString(byte[] bcdData) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bcdData) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
