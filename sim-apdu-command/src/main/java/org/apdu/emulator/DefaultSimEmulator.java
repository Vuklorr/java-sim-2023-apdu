package org.apdu.emulator;


public interface DefaultSimEmulator {
    byte[] execute(byte[] apdu);

    byte[] getResponseData();
}
