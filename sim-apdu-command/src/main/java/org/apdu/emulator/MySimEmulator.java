package org.apdu.emulator;


import lombok.Getter;
import org.apdu.sim.MySim;

public class MySimEmulator {
    private final byte[] RESPONSE_SUCCESS = {(byte)0x90, (byte)0x00};

    //второй байт должен выводить длину значения ответа, но в данной реализации он выводит одно и то же значение, так как оно особо не влияет
    private final byte[] RESPONSE_SELECT_SUCCESS = {(byte)0x9F, (byte)0x07};

    private final byte[] RESPONSE_CLA_ERROR = {(byte)0x6E, (byte)0x00};

    private final byte[] RESPONSE_INS_ERROR = {(byte)0x6D, (byte)0x00};

    private final MySim mySim;

    @Getter
    private byte[] responseData;

    public MySimEmulator(MySim mySim) {
        this.mySim = mySim;
    }


    // Метод обработки APDU команд
    public byte[] execution (byte[] apdu) {
        byte cla = apdu[0];
        byte ins = apdu[1];

        if(cla != (byte)0xA0) {
            return RESPONSE_CLA_ERROR;
        }

        //в данном случае рассматривается всего 3 команды select, read binary и update binary
        switch (ins) {
            case (byte) 0xA4 -> {
                mySim.setIdCurrentDir(apdu[5]);
                return RESPONSE_SELECT_SUCCESS;
            }
            case (byte) 0xB0 -> {
                responseData = mySim.getSmsCenterNumberBCD();
                return RESPONSE_SUCCESS;
            }
            case (byte) 0xD6 -> {
                byte[] data = new byte[apdu[4]];
                System.arraycopy(apdu, 5, data, 0, apdu.length - 5);
                mySim.setSmsCenterNumberBCD(data);

                return RESPONSE_SUCCESS;
            }
        }
        return RESPONSE_INS_ERROR;
    }
}
