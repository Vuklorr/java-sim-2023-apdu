package org.apdu;

import lombok.extern.slf4j.Slf4j;
import org.apdu.command.ApduCommand;
import org.apdu.api.SimEmulator;
import org.apdu.emulator.DefaultSimEmulatorImpl;
import org.apdu.sim.DefaultSim;
import org.apdu.utils.SimUtils;

@Slf4j(topic = "MyUpdateSmsC")
public class MyUpdateSmsCenterNumber {
    public static void main(String[] args) {
        //Подключение к карт ридеру по протоколу T=0
        //Взаимодействие с SIM картой
        byte[] smsCenterNumber = new byte[]{(byte)0x06,
                (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0,
                (byte) 0xFF, (byte) 0xFF};
        DefaultSim mySim = new DefaultSim(smsCenterNumber);
        SimEmulator simEmulator = new DefaultSimEmulatorImpl(mySim);
        //массив байтов, для получения R-APDU
        byte[] responseApdu;


        log.info("Переход к MF. ");
        responseApdu = simEmulator.execute(ApduCommand.SELECT_MF_APDU.getValue());

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Переход к DF GSM. ");
        responseApdu = simEmulator.execute(ApduCommand.SELECT_DF_GSM_APDU.getValue());

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Переход к EF IMSI. ");
        responseApdu = simEmulator.execute(ApduCommand.SELECT_EF_IMSI_APDU.getValue());

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execute(ApduCommand.READ_BINARY_SMS_CENTER_APDU.getValue());

        if (SimUtils.hasErrReadSmsCenter(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        log.info("Обновление номера SMS центра. ");
        responseApdu = simEmulator.execute(ApduCommand.UPDATE_BINARY_SMS_CENTER_APDU.getValue());
        if (responseApdu[0] == (byte)0x90 && responseApdu[1] == (byte)0x00) {
            log.info("Результат: " + SimUtils.byteToHexString(responseApdu));
        } else {
            log.info("Ошибка выполнения команды: " + SimUtils.byteToHexString(responseApdu));
            return;
        }

        log.info("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execute(ApduCommand.READ_BINARY_SMS_CENTER_APDU.getValue());

        //После этой проверки должны быть еще действия, поэтому эта проверка нужна
        if (SimUtils.hasErrReadSmsCenter(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        // Закрываем соединение с картой
    }
}