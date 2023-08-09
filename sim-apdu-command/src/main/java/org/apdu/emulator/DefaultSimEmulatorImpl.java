package org.apdu.emulator;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apdu.command.ApduResponse;
import org.apdu.api.SimEmulator;
import org.apdu.sim.DefaultSim;
import org.apdu.utils.SimUtils;

/**
 * Реализация интерфейса SimEmulator.
 * Позволяет получить APDU команду, обработать и отправить ответ (R-APDU)
 */
@RequiredArgsConstructor
public class DefaultSimEmulatorImpl implements SimEmulator {
    private final DefaultSim mySim;

    @Getter
    private byte[] responseData;

    @Override
    public byte[] execute(byte[] apdu) {
        if(apdu.length < 5 || apdu.length > 260) {
            return ApduResponse.RESPONSE_LENGTH_ERROR.getValue();
        }

        byte cla = apdu[0];
        byte ins = apdu[1];
        byte param1 = apdu[2];
        byte param2 = apdu[3];
        byte param3 = apdu[4];

        // в данном случае рассматривается только класс GSM
        // GSM - глобальный стандарт цифровой мобильной сотовой связи с разделением каналов по времени и частоте.
        if(cla != (byte)0xA0) {
            return ApduResponse.RESPONSE_CLA_ERROR.getValue();
        }

        // в данном случае рассматривается всего 3 команды select, read binary и update binary
        switch (ins) {
            case (byte) 0xA4 -> {
                return param1 == (byte) 0x00 && param2 == (byte) 0x00
                        ? ApduResponse.RESPONSE_SELECT_SUCCESS.getValue()
                        : ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
            }
            case (byte) 0xB0 -> {
                final int MAX_DATA_LEN = mySim.getImsiDataBCD().length;
                if(MAX_DATA_LEN < (param1 + param2 + param3)) {
                    return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                }

                responseData = SimUtils.offsetReadData(param1, param2, param3, mySim.getImsiDataBCD());
                return ApduResponse.RESPONSE_SUCCESS.getValue();
            }
            case (byte) 0xD6 -> {
                final int MAX_DATA_LEN = mySim.getImsiDataBCD().length;
                if(MAX_DATA_LEN < (param1 + param2 + param3)) {
                    return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                }

                byte[] apduData = new byte[apdu[4]];
                System.arraycopy(apdu, 5, apduData, 0, apdu.length - 5);

                responseData = SimUtils.offsetUpdateData(param1, param2, param3, apduData, mySim.getImsiDataBCD());
                mySim.setImsiDataBCD(responseData);

                return ApduResponse.RESPONSE_SUCCESS.getValue();
            }
        }
        return ApduResponse.RESPONSE_INS_ERROR.getValue();
    }
}
