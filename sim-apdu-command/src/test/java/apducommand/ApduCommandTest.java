package apducommand;

import org.apdu.command.ApduResponse;
import org.apdu.emulator.DefaultSimEmulator;
import org.apdu.emulator.impl.DefaultSimEmulatorImpl;
import org.apdu.sim.DefaultSim;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ApduCommandTest {
    @Test
    public void shouldReturnErrorOnTooSmallCommandLength() {
        final byte[] SMS_CENTER_NUMBER = new byte[]{(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] SMALL_COMMAND_LENGTH = new byte[]{(byte)0xA0, (byte)0xD6};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(SMALL_COMMAND_LENGTH);

        assertArrayEquals(result, ApduResponse.RESPONSE_LENGTH_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getSmsCenterNumberBCD());
    }

    @Test
    public void shouldReturnErrorOnTooLargeCommandLength() {
        final byte[] SMS_CENTER_NUMBER = new byte[]{(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] LARGE_COMMAND_LENGTH = new byte[261];
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(LARGE_COMMAND_LENGTH);

        assertArrayEquals(result, ApduResponse.RESPONSE_LENGTH_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getSmsCenterNumberBCD());
    }

    @Test
    public void shouldReturnErrorOnIncorrectClaParam() {
        final byte[] SMS_CENTER_NUMBER = new byte[]{(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        final byte[] INCORRECT_CLA_PARAM = new byte[]{(byte)0xC0, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x06};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_CLA_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_CLA_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getSmsCenterNumberBCD());
   }

    @Test
    public void shouldReturnErrorOnIncorrectInsParam() {
        final byte[] SMS_CENTER_NUMBER = new byte[]{(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87, (byte) 0x09, (byte) 0xF0};
        final DefaultSim SIM = new DefaultSim(SMS_CENTER_NUMBER);

        // FINS = FA - SLEEP, но она не реализована в этом эмуляторе по заданию
        final byte[] INCORRECT_INS_PARAM = new byte[]{(byte)0xA0, (byte)0xFA, (byte)0x00, (byte)0x00, (byte)0x06};
        final DefaultSimEmulator SIM_EMULATOR = new DefaultSimEmulatorImpl(SIM);

        byte[] result = SIM_EMULATOR.execute(INCORRECT_INS_PARAM);

        assertArrayEquals(result, ApduResponse.RESPONSE_INS_ERROR.getValue());
        assertArrayEquals(SMS_CENTER_NUMBER, SIM.getSmsCenterNumberBCD());
    }
}
