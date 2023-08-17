package apducommand;

import org.apdu.api.SimEmulator;
import org.apdu.command.ApduResponse;
import org.apdu.command.DefaultApduCommand;
import org.apdu.ef.DefaultEfImsi;
import org.apdu.ef.DefaultEfSmsP;
import org.apdu.emulator.DefaultSimEmulatorImpl;
import org.apdu.utils.SimUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ApduCommandTest {
    private final byte[] SMS_CENTER_NUMBER = new byte[]{(byte)0x06,
            (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0,
            (byte) 0xFF, (byte) 0xFF};

    @Test
    @DisplayName("Cla Error")
    public void shouldReturnErrorOnIncorrectClaParam() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final DefaultApduCommand INCORRECT_CLA_PARAM = new DefaultApduCommand((byte)0xC0, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x06);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_CLA_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_CLA_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
   }

    @Test
    @DisplayName("Ins Error")
    public void shouldReturnErrorOnIncorrectInsParam() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        // FINS = FA - SLEEP, но она не реализована в этом эмуляторе по заданию
        final DefaultApduCommand INCORRECT_INS_PARAM = new DefaultApduCommand((byte)0xA0, (byte)0xFA, (byte)0x00, (byte)0x00, (byte)0x06);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_INS_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INS_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Select Error")
    public void shouldReturnErrorOnIncorrectParamForSelect() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final DefaultApduCommand INCORRECT_SELECT_PARAM = new DefaultApduCommand((byte)0xA0, (byte)0xA4, (byte)0x01, (byte)0x02,
                new byte[]{(byte)0x3F, (byte)0x00});
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_SELECT_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Read Record Error")
    public void shouldReturnErrorOnIncorrectParamForReadRecord() {
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SimUtils.initSimSmsP());
        final DefaultApduCommand INCORRECT_READ_RECORD_PARAM =
                new DefaultApduCommand((byte)0xA0, (byte)0xB2, (byte)0x01, (byte)0x03, (byte)0x06);
        byte[] result = SIM_EMULATOR.execute(INCORRECT_READ_RECORD_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
    }

    @Test
    @DisplayName("Read Record Len Response Error")
    public void shouldReturnSuccessesOnReadRecordLenResponse() {
        DefaultEfSmsP smsP = SimUtils.initSimSmsP();
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(smsP);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xB2, (byte)0x01, (byte)0x04, (byte)0x02);
        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SIM_EMULATOR.getResponseData(), null);
    }

    @Test
    @DisplayName("Update Record Error")
    public void shouldReturnErrorOnIncorrectParamForUpdateRecord() {
        DefaultEfSmsP sim = SimUtils.initSimSmsP();
        final byte VALID_PERIOD = sim.getValidPeriod();
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SimUtils.initSimSmsP());
        final DefaultApduCommand INCORRECT_UPDATE_BINARY_PARAM =
                new DefaultApduCommand((byte)0xA0, (byte)0xDC, (byte)0x06, (byte)0x02,
                        new byte[]{(byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6});

        byte[] result = SIM_EMULATOR.execute(INCORRECT_UPDATE_BINARY_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertEquals(VALID_PERIOD, sim.getValidPeriod());
    }

    @Test
    @DisplayName("Read Binary Error")
    public void shouldReturnErrorOnIncorrectParamForReadBinary() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        final DefaultApduCommand INCORRECT_READ_BINARY_PARAM =
                new DefaultApduCommand((byte)0xA0, (byte)0xB0, (byte)0x03, (byte)0x02, (byte)0x06);
        byte[] result = SIM_EMULATOR.execute(INCORRECT_READ_BINARY_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Update Binary Error")
    public void shouldReturnErrorOnIncorrectParamForUpdateBinary() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        final DefaultApduCommand INCORRECT_UPDATE_BINARY_PARAM =
                new DefaultApduCommand((byte)0xA0, (byte)0xD6, (byte)0x02, (byte)0x03,
                new byte[]{(byte)0x97, (byte)0x02, (byte)0x31, (byte)0x23, (byte)0x32, (byte)0xF6});

        byte[] result = SIM_EMULATOR.execute(INCORRECT_UPDATE_BINARY_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Read Record Success")
    public void shouldReturnSuccessesOnReadRecordCommand() {
        DefaultEfSmsP smsP = SimUtils.initSimSmsP();
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(smsP);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xB2, (byte)0x01, (byte)0x04, (byte)0x01);
        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertEquals(SIM_EMULATOR.getResponseData()[0], smsP.getParameterIndicator());
    }

    @Test
    @DisplayName("Update Record Success")
    public void shouldReturnSuccessesOnUpdateRecordCommand() {
        DefaultEfSmsP smsP = SimUtils.initSimSmsP();
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(smsP);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xDC, (byte)0x03, (byte)0x02,
                new byte[]{(byte)0xFF});
        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertEquals(apduCommand.getData()[0], smsP.getProtocolId());
    }

    @Test
    @DisplayName("Select MF Success")
    public void shouldReturnSuccessesOnSelectMFCommand() {
        final DefaultEfImsi SIM = SimUtils.initSimImsi();
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00,
                new byte[]{(byte)0x3F, (byte)0x00});
        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_SELECT_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Select DF Telecom Success")
    public void shouldReturnSuccessesOnSelectDFTelecomCommand() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00,
                new byte[]{(byte)0x7F, (byte)0x10});
        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_SELECT_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Select EF SMS P Success")
    public void shouldReturnSuccessesOnSelectEFSmsCommand() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xA4, (byte)0x00, (byte)0x00,
                new byte[]{(byte)0x6F, (byte)0x42});
        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_SELECT_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }

    @Test
    @DisplayName("Read Binary Success")
    public void shouldReturnSuccessesOnReadBinaryCommand() {
        final DefaultEfImsi SIM = new DefaultEfImsi(SMS_CENTER_NUMBER);
        final SimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);
        DefaultApduCommand apduCommand = new DefaultApduCommand((byte)0xA0, (byte)0xB0, (byte)0x00, (byte)0x01, (byte)0x06);

        byte[] result = SIM_EMULATOR.execute(apduCommand);

        assertArrayEquals(result, ApduResponse.RESPONSE_SUCCESS.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getImsiDataBCD());
    }
}
