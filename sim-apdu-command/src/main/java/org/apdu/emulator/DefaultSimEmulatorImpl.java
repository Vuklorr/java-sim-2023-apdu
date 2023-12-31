package org.apdu.emulator;


import lombok.Getter;
import org.apdu.api.SimEmulator;
import org.apdu.command.ApduResponse;
import org.apdu.command.DefaultApduCommand;
import org.apdu.ef.DefaultEfImsi;
import org.apdu.ef.DefaultEfSmsP;
import org.apdu.utils.SimUtils;

/**
 * Реализация интерфейса SimEmulator.
 * Позволяет получить APDU команду, обработать и отправить ответ (R-APDU)
 */

public class DefaultSimEmulatorImpl implements SimEmulator {
    private DefaultEfSmsP mySimSmsP;

    private DefaultEfImsi mySimIMSI;

    @Getter
    private byte[] responseData;

    public DefaultSimEmulatorImpl(DefaultEfSmsP mySimSmsP) {
        this.mySimSmsP = mySimSmsP;
    }

    public DefaultSimEmulatorImpl(DefaultEfImsi mySimIMSI) {
        this.mySimIMSI = mySimIMSI;
    }

    public DefaultSimEmulatorImpl(DefaultEfSmsP mySimSmsP, DefaultEfImsi mySimIMSI) {
        this.mySimSmsP = mySimSmsP;
        this.mySimIMSI = mySimIMSI;
    }

    @Override
    public byte[] execute(DefaultApduCommand apdu) {
        // в данном случае рассматривается только класс GSM
        // GSM - глобальный стандарт цифровой мобильной сотовой связи с разделением каналов по времени и частоте.
        if(apdu.getCla() != (byte)0xA0) {
            return ApduResponse.RESPONSE_CLA_ERROR.getValue();
        }
        byte[] data = apdu.getData();
        // в данном случае рассматривается всего 5 команды select, read record, update record, read binary и update binary
        switch (apdu.getIns()) {
            case (byte) 0xA4 -> {
                return apdu.getParam1() == (byte) 0x00 && apdu.getParam2() == (byte) 0x00
                        ? ApduResponse.RESPONSE_SELECT_SUCCESS.getValue()
                        : ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
            }
            case (byte) 0xB2 -> {
                switch (apdu.getParam1()) {
                    //в данном случае нет указателя на запись
                    case (byte) 0x01 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            if(apdu.getLenResponse() > mySimSmsP.getDestinationAddress().length) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = mySimSmsP.getDestinationAddress();
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getParameterIndicator()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x02 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            if(apdu.getLenResponse() > mySimSmsP.getServiceCentreAddress().length) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = mySimSmsP.getServiceCentreAddress();
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getParameterIndicator()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenResponse() > mySimSmsP.getDestinationAddress().length) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = mySimSmsP.getDestinationAddress();
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x03 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getProtocolId()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            if(apdu.getLenResponse() > mySimSmsP.getDestinationAddress().length) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = mySimSmsP.getDestinationAddress();
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenResponse() > mySimSmsP.getServiceCentreAddress().length) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = mySimSmsP.getServiceCentreAddress();
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x04 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getDataCodingScheme()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            if(apdu.getLenResponse() > mySimSmsP.getServiceCentreAddress().length) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = mySimSmsP.getServiceCentreAddress();
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getProtocolId()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x05 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getValidPeriod()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getProtocolId()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getDataCodingScheme()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x06 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getDataCodingScheme()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenResponse() != (byte)0x01) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            responseData = new byte[]{mySimSmsP.getValidPeriod()};
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                }

                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
            }
            case (byte) 0xDC -> {
                switch (apdu.getParam1()) {
                    //в данном случае нет указателя на запись
                    case (byte) 0x01 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            mySimSmsP.setDestinationAddress(data);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            mySimSmsP.setParameterIndicator(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x02 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            mySimSmsP.setServiceCentreAddress(data);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            mySimSmsP.setParameterIndicator(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            mySimSmsP.setDestinationAddress(data);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x03 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            mySimSmsP.setProtocolId(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            mySimSmsP.setDestinationAddress(data);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            if(apdu.getLenData() > 0x0C) {
                                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                            }

                            if(apdu.getLenData() == 0x0C) {
                                mySimSmsP.setServiceCentreAddress(data);
                            } else {
                                mySimSmsP.setServiceCentreAddress(SimUtils.recordUpdate(data));
                            }

                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x04 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            mySimSmsP.setDataCodingScheme(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            mySimSmsP.setServiceCentreAddress(data);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            mySimSmsP.setProtocolId(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x05 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            mySimSmsP.setValidPeriod(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            mySimSmsP.setProtocolId(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            mySimSmsP.setDataCodingScheme(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                    case (byte) 0x06 -> {
                        //следующая запись
                        if(apdu.getParam2() == 0x02) {
                            return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                        }

                        //предыдущая запись
                        if(apdu.getParam2() == 0x03) {
                            mySimSmsP.setDataCodingScheme(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }

                        //текущая запись
                        if(apdu.getParam2() == 0x04) {
                            mySimSmsP.setValidPeriod(data[0]);
                            return ApduResponse.RESPONSE_SUCCESS.getValue();
                        }
                    }
                }

                return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
            }
            case (byte) 0xB0 -> {
                final int MAX_DATA_LEN = mySimIMSI.getImsiDataBCD().length;
                short offset = (short) (apdu.getParam1() << 8 | apdu.getParam2());

                if(MAX_DATA_LEN < (offset + apdu.getLenResponse())) {
                    return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                }

                responseData = SimUtils.offsetReadData(offset, apdu.getLenResponse(), mySimIMSI.getImsiDataBCD());
                return ApduResponse.RESPONSE_SUCCESS.getValue();
            }
            case (byte) 0xD6 -> {
                final int MAX_DATA_LEN = mySimIMSI.getImsiDataBCD().length;
                short offset = (short) (apdu.getParam1() << 8 | apdu.getParam2());

                if(MAX_DATA_LEN < (offset + apdu.getLenResponse())) {
                    return ApduResponse.RESPONSE_INCORRECT_PARAM_ERROR.getValue();
                }

                responseData = SimUtils.offsetUpdateData(offset, apdu.getLenData(), data, mySimIMSI.getImsiDataBCD());
                mySimIMSI.setImsiDataBCD(responseData);

                return ApduResponse.RESPONSE_SUCCESS.getValue();
            }
        }
        return ApduResponse.RESPONSE_INS_ERROR.getValue();
    }
}
