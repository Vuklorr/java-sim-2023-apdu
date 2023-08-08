package org.apdu.emulator.impl;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apdu.command.ApduResponse;
import org.apdu.emulator.DefaultSimEmulator;
import org.apdu.sim.DefaultSim;

/**
 * Простой сим эмулятор, который больше похож на mock:
 * отправляется запрос и возвращается ответ.
 * Нужен для эмуляции взаимодействия с SIM - картой
 */
@RequiredArgsConstructor
public class DefaultSimEmulatorImpl implements DefaultSimEmulator {
    private final DefaultSim mySim;

    @Getter
    private byte[] responseData;


    /**
     * Метод для обработки APDU команд.
     *
     * @param apdu - apdu команда (C-APDU)
     * @return - apdu ответ (R-APDU)
     */
    @Override
    public byte[] execute(byte[] apdu) {
        if(apdu.length < 5 || apdu.length > 260) {
            return ApduResponse.RESPONSE_LENGTH_ERROR.getValue();
        }

        byte cla = apdu[0];
        byte ins = apdu[1];

        // в данном случае рассматривается только класс GSM
        // GSM - глобальный стандарт цифровой мобильной сотовой связи с разделением каналов по времени и частоте.
        if(cla != (byte)0xA0) {
            return ApduResponse.RESPONSE_CLA_ERROR.getValue();
        }

        // в данном случае рассматривается всего 3 команды select, read binary и update binary
        switch (ins) {
            case (byte) 0xA4 -> {
                mySim.setIdCurrentDir(apdu[5]);
                return ApduResponse.RESPONSE_SELECT_SUCCESS.getValue();
            }
            case (byte) 0xB0 -> {
                responseData = mySim.getSmsCenterNumberBCD();
                return ApduResponse.RESPONSE_SUCCESS.getValue();
            }
            case (byte) 0xD6 -> {
                byte[] data = new byte[apdu[4]];
                System.arraycopy(apdu, 5, data, 0, apdu.length - 5);
                mySim.setSmsCenterNumberBCD(data);

                return ApduResponse.RESPONSE_SUCCESS.getValue();
            }
        }
        return ApduResponse.RESPONSE_INS_ERROR.getValue();
    }
}
