package org.apdu;

import lombok.extern.slf4j.Slf4j;
import org.apdu.command.ApduCommand;
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
            ResponseAPDU response = channel.transmit(new CommandAPDU(ApduCommand.SELECT_MF_APDU.getValue()));

            if(SimUtils.hasErrResponse(response)) {
                return;
            }

            log.info("Переход к DF GSM. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.SELECT_DF_GSM_APDU.getValue()));

            if(SimUtils.hasErrResponse(response)) {
                return;
            }

            log.info("Переход к EF IMSI. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.SELECT_EF_IMSI_APDU.getValue()));

            if(SimUtils.hasErrResponse(response)) {
                return;
            }

            log.info("Вывод номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.READ_BINARY_SMS_CENTER_APDU.getValue()));

            if (SimUtils.hasErrReadSmsCenter(response)) {
                return;
            }

            log.info("Обновление номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.UPDATE_BINARY_SMS_CENTER_APDU.getValue()));

            if (response.getSW() == 0x9000) {
                log.info("Результат: " + Integer.toHexString(response.getSW()));
            } else {
                log.info("Ошибка выполнения команды: " + Integer.toHexString(response.getSW()));
                return;
            }

            log.info("Вывод номера SMS центра. ");
            response = channel.transmit(new CommandAPDU(ApduCommand.READ_BINARY_SMS_CENTER_APDU.getValue()));

            if (SimUtils.hasErrReadSmsCenter(response)) {
                return;
            }

            // Закрываем соединение с картой
            card.disconnect(true);
        } catch (CardException e) {
            e.printStackTrace();
        }
    }
}
