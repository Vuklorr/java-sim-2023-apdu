package org.apdu;

import lombok.extern.slf4j.Slf4j;
import org.apdu.utils.SimUtils;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import java.util.List;

@Slf4j(topic = "UpdateSmsCenterNumber")
public class UpdateSmsCenterNumber {
    public static void main(String[] args) {
        try {
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = terminalFactory.terminals().list();
            CardTerminal terminal = terminals.get(0); // Первый доступный карт ридер

            // Протокол T=0 является байтовым, т.е. наименьшая единица данных, которая передается или обрабатывается, это байт
            // Протокол T=1 является блочно-ориентированным, т.е. наименьшая единица данных, которая передается или обрабатывается,
            //  это блок, состоящий из последовательности байтов.
            Card card = terminal.connect("T=0");
            CardChannel channel = card.getBasicChannel();

            // Отправляем команды на карту и получаем ответ
            log.info("Переход к MF. ");
            byte[] selectCommandApdu = new byte[] {(byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00, (byte)0x02,
                    (byte)0x3F, (byte)0x00};
            ResponseAPDU response = channel.transmit(new CommandAPDU(selectCommandApdu));

            if(SimUtils.hasErrResponse(response)) {
                return;
            }

            log.info("Переход к DF Telecom. ");
            selectCommandApdu[5] = (byte)0x7F;
            selectCommandApdu[6] = (byte)0x10;
            response = channel.transmit(new CommandAPDU(selectCommandApdu));

            if(SimUtils.hasErrResponse(response)) {
                return;
            }

            log.info("Переход к EF SMSP. ");
            selectCommandApdu[5] = (byte)0x6F;
            selectCommandApdu[6] = (byte)0x42;
            response = channel.transmit(new CommandAPDU(selectCommandApdu));

            if(SimUtils.hasErrResponse(response)) {
                return;
            }

            //нужна ли проверка на CHV1 для read и update зависит от sim

            log.info("Вывод номера SMS центра. ");
            byte[] readCommandApdu = new byte[] {(byte)0xA0, (byte)0xB2, (byte)0x03, (byte)0x04, (byte) 0x06};
            response = channel.transmit(new CommandAPDU(readCommandApdu));

            if (SimUtils.hasErrReadSmsCenter(response)) {
                return;
            }

            log.info("Обновление номера SMS центра. ");
            byte[] updateCommandApdu = new byte[] {(byte)0xA0, (byte)0xDC, (byte)0x03, (byte)0x04, (byte) 0x08,
                    (byte)0x07, (byte)0x91, (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6};
            response = channel.transmit(new CommandAPDU(updateCommandApdu));

            if (response.getSW() == 0x9000) {
                log.info("Результат: " + Integer.toHexString(response.getSW()));
            } else {
                log.info("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
                return;
            }

            log.info("Вывод номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(readCommandApdu));

            if (SimUtils.hasErrReadSmsCenter(response)) {
                return;
            }

            // Закрываем соединение с картой
            card.disconnect(true);
        } catch (CardException e) {
            log.error(e.getMessage());
        }
    }
}
