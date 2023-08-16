package org.apdu;

import lombok.extern.slf4j.Slf4j;
import org.apdu.api.SimEmulator;
import org.apdu.command.DefaultApduCommand;
import org.apdu.emulator.DefaultSimEmulatorImpl;
import org.apdu.utils.SimUtils;

@Slf4j(topic = "MyUpdateSmsC")
public class MyUpdateSmsCenterNumber {
    public static void main(String[] args) {
        //Подключение к карт ридеру по протоколу T=0
        //Взаимодействие с SIM картой
        SimEmulator simEmulator = new DefaultSimEmulatorImpl(SimUtils.initSimSMSP(), SimUtils.initSimIMSI());
        //массив байтов, для получения R-APDU
        byte[] responseApdu;
        DefaultApduCommand selectApdu = new DefaultApduCommand((byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00,
                new byte[]{(byte)0x3F, (byte)0x00});

        log.info("Переход к MF. ");
        responseApdu = simEmulator.execute(selectApdu);

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Переход к DF GSM. ");
        selectApdu.setData(new byte[]{(byte)0x7F, (byte)0x10});
        responseApdu = simEmulator.execute(selectApdu);

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Переход к EF IMSI. ");
        selectApdu.setData(new byte[]{(byte)0x6F, (byte)0x42});
        responseApdu = simEmulator.execute(selectApdu);

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        //нужна ли проверка на CHV1 для read и update зависит от sim
        log.info("Вывод номера SMS центра. ");
        DefaultApduCommand readRecordApdu = new DefaultApduCommand((byte)0xA0, (byte)0xB2, (byte)0x03, (byte)0x04, (byte)0x06);
        responseApdu = simEmulator.execute(readRecordApdu);

        if (SimUtils.hasErrReadSmsCenter(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        log.info("Обновление номера SMS центра. ");
        DefaultApduCommand updateRecordApdu = new DefaultApduCommand((byte)0xA0, (byte)0xDC, (byte)0x03, (byte)0x04,
                new byte[]{(byte)0x07, (byte)0x91, (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6});
        responseApdu = simEmulator.execute(updateRecordApdu);
        if (responseApdu[0] == (byte)0x90 && responseApdu[1] == (byte)0x00) {
            log.info("Результат: " + SimUtils.byteToHexString(responseApdu));
        } else {
            log.info("Ошибка выполнения команды: " + SimUtils.byteToHexString(responseApdu));
            return;
        }

        log.info("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execute(readRecordApdu);

        //После этой проверки должны быть еще действия, поэтому эта проверка нужна
        if (SimUtils.hasErrReadSmsCenter(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        // Закрываем соединение с картой
    }
}