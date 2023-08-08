package org.apdu;

import lombok.extern.slf4j.Slf4j;
import org.apdu.command.ApduCommand;
import org.apdu.emulator.DefaultSimEmulator;
import org.apdu.emulator.impl.DefaultSimEmulatorImpl;
import org.apdu.sim.DefaultSim;
import org.apdu.utils.SimUtils;

@Slf4j(topic = "MyUpdateSmsC")
public class MyUpdateSmsCenterNumber {
    public static void main(String[] args) {
        //Подключение к карт ридеру по протоколу T=0
        //Взаимодействие с SIM картой
        byte[] smsCenterNumber = new byte[]{(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0};
        DefaultSim mySim = new DefaultSim(smsCenterNumber);
        DefaultSimEmulator simEmulator = new DefaultSimEmulatorImpl(mySim);
        //массив байтов, для получения R-APDU
        byte[] responseApdu;


        log.info("Переход к MF. ");
        responseApdu = simEmulator.execute(ApduCommand.SELECT_MF_APDU.getValue());

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Переход к DF telecom. ");
        responseApdu = simEmulator.execute(ApduCommand.SELECT_DF_TELECOM_APDU.getValue());

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Переход к EF sms 111. ");
        responseApdu = simEmulator.execute(ApduCommand.SELECT_EF_SMS_111_APDU.getValue());

        if(SimUtils.hasErrResponse(responseApdu)) {
            return;
        }

        log.info("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execute(ApduCommand.READ_BINARY_SMS_C_APDU.getValue());

        if (SimUtils.hasErrReadSmsCenter(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        log.info("Обновление номера SMS центра. ");
        responseApdu = simEmulator.execute(ApduCommand.UPDATE_BINARY_SMS_C_APDU.getValue());

        if (responseApdu[0] == (byte)0x90 && responseApdu[1] == (byte)0x00) {
            log.info("Результат: " + SimUtils.byteToHexString(responseApdu));
        } else {
            log.info("Ошибка выполнения команды: " + SimUtils.byteToHexString(responseApdu));
            return;
        }

        log.info("Вывод номера SMS центра. ");
        responseApdu = simEmulator.execute(ApduCommand.READ_BINARY_SMS_C_APDU.getValue());

        //После этой проверки должны быть еще действия, поэтому эта проверка нужна
        if (SimUtils.hasErrReadSmsCenter(responseApdu, simEmulator.getResponseData())) {
            return;
        }

        // Закрываем соединение с картой
    }
}