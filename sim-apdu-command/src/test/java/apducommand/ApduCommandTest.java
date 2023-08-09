package apducommand;

import org.apdu.command.ApduCommand;
import org.apdu.command.ApduResponse;
import org.apdu.emulator.DefaultSimEmulator;
import org.apdu.emulator.impl.DefaultSimEmulatorImpl;
import org.apdu.sim.DefaultSim;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ApduCommandTest {
    private final byte[] SMS_CENTER_NUMBER = new byte[]{(byte)0x06,
            (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0,
            (byte) 0xFF, (byte) 0xFF};

    @Test
    public void shouldReturnErrorOnTooSmallCommandLength() {

        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] SMALL_COMMAND_LENGTH = new byte[]{(byte)0xA0, (byte)0xD6};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(SMALL_COMMAND_LENGTH);

        assertArrayEquals(result, ApduResponse.RESPONSE_LENGTH_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnErrorOnTooLargeCommandLength() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] LARGE_COMMAND_LENGTH = new byte[261];
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(LARGE_COMMAND_LENGTH);

        assertArrayEquals(result, ApduResponse.RESPONSE_LENGTH_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnErrorOnIncorrectClaParam() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] INCORRECT_CLA_PARAM = new byte[]{(byte)0xC0, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x06};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_CLA_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_CLA_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
   }

    @Test
    public void shouldReturnErrorOnIncorrectInsParam() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        // FINS = FA - SLEEP, но она не реализована в этом эмуляторе по заданию
        final byte[] INCORRECT_INS_PARAM = new byte[]{(byte)0xA0, (byte)0xFA, (byte)0x00, (byte)0x00, (byte)0x06};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_INS_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INS_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnErrorOnIncorrectParamForSelect() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] INCORRECT_SELECT_PARAM = new byte[]{(byte)0xA0, (byte)0xA4, (byte)0x01, (byte)0x02, (byte)0x02,
                (byte)0x3F, (byte)0x00};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_SELECT_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnErrorOnIncorrectParamForReadBinary() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        final byte[] INCORRECT_READ_BINARY_PARAM = new byte[]{(byte)0xA0, (byte)0xB0, (byte)0x03, (byte)0x02, (byte)0x06};

        byte[] result = SIM_EMULATOR.execute(INCORRECT_READ_BINARY_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnErrorOnIncorrectParamForUpdateBinary() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        final byte[] INCORRECT_UPDATE_BINARY_PARAM = new byte[]{(byte)0xA0, (byte)0xD6, (byte)0x02, (byte)0x03, (byte)0x06,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6};

        byte[] result = SIM_EMULATOR.execute(INCORRECT_UPDATE_BINARY_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnSelectMFCommand() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(ApduCommand.SELECT_MF_APDU.getValue());

        assertArrayEquals(result, ApduResponse.RESPONSE_SELECT_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnSelectDFTelecomCommand() {
         final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(ApduCommand.SELECT_DF_GSM_APDU.getValue());

        assertArrayEquals(result, ApduResponse.RESPONSE_SELECT_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnSelectEFSmsCommand() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(ApduCommand.SELECT_EF_IMSI_APDU.getValue());

        assertArrayEquals(result, ApduResponse.RESPONSE_SELECT_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnReadBinaryCommand() {
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(ApduCommand.READ_BINARY_SMS_CENTER_APDU.getValue());

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnReadBinaryWithParamCommand() {
        // Первые 3 байта и последние 3 байта - это данные, которые нам не нужны.
        // Номер SMS - центра находится между этими байтами и имеет размер 6 байт.
        final byte[] DATA_WITH_SMS_CENTER_NUMBER = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x05,
                (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0,
                (byte) 0x00, (byte) 0x01, (byte) 0x05};
        final byte[] READ_BINARY_SMS_CENTER_WITH_PARAM_APDU = new byte[]{(byte)0xA0, (byte)0xB0, (byte)0x03, (byte)0x03, (byte)0x06};


        final byte[] SMS_CENTER_NUMBER = new byte[]{(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0};
        final DefaultSim SIM = new DefaultSim(DATA_WITH_SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(READ_BINARY_SMS_CENTER_WITH_PARAM_APDU);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM_EMULATOR.getResponseData());
    }

    @Test
    public void shouldReturnSuccessesOnUpdateBinaryCommand() {
        final byte[] NEW_SMS_CENTER_NUMBER = new byte[]{(byte)0x06,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6,
                (byte)0xFF, (byte)0xFF};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(ApduCommand.UPDATE_BINARY_SMS_CENTER_APDU.getValue());

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(NEW_SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnUpdateBinaryWithParamCommand() {
        final byte[] NEW_SMS_CENTER_NUMBER = new byte[]{(byte) 0x06,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6,
                (byte) 0xFF, (byte) 0xFF,};
        final byte[] UPDATE_BINARY_SMS_CENTER_WITH_PARAM_APDU = new byte[]{(byte)0xA0, (byte)0xD6, (byte)0x01, (byte)0x02, (byte)0x06,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6};

        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(UPDATE_BINARY_SMS_CENTER_WITH_PARAM_APDU);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(NEW_SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnUpdateBinaryWithLessIMSI() {
        final byte[] NEW_SMS_CENTER_NUMBER = new byte[]{(byte)0x04,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0xF3,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        final byte[] UPDATE_BINARY_SMS_CENTER_APDU = new byte[]{(byte)0xA0, (byte)0xD6, (byte)0x00, (byte)0x00, (byte)0x04,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0xF3};

        byte[] result = SIM_EMULATOR.execute(UPDATE_BINARY_SMS_CENTER_APDU);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(NEW_SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    public void shouldReturnSuccessesOnUpdateBinaryWithLargeIMSI() {
        final byte[] NEW_SMS_CENTER_NUMBER = new byte[]{(byte)0x08,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23,(byte)0x14, (byte)0x56, (byte)0x87, (byte)0xF9};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        final byte[] UPDATE_BINARY_SMS_CENTER_APDU = new byte[]{(byte)0xA0, (byte)0xD6, (byte)0x01, (byte)0x00, (byte)0x08,
                (byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23,(byte)0x14, (byte)0x56, (byte)0x87, (byte)0xF9};

        byte[] result = SIM_EMULATOR.execute(UPDATE_BINARY_SMS_CENTER_APDU);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(NEW_SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }
}
