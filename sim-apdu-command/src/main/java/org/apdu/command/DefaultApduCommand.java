package org.apdu.command;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DefaultApduCommand {

    private byte cla;

    private byte ins;

    private byte param1;

    private byte param2;

    private byte lenData;

    private byte[] data;

    private byte lenResponse;

    public DefaultApduCommand(byte cla, byte ins, byte param1, byte param2) {
        this.cla = cla;
        this.ins = ins;
        this.param1 = param1;
        this.param2 = param2;
    }

    public DefaultApduCommand(byte cla, byte ins, byte param1, byte param2, byte[] data) {
        this.cla = cla;
        this.ins = ins;
        this.param1 = param1;
        this.param2 = param2;
        lenData = (byte)data.length;
        this.data = data;
    }

    public DefaultApduCommand(byte cla, byte ins, byte param1, byte param2, byte lenResponse) {
        this.cla = cla;
        this.ins = ins;
        this.param1 = param1;
        this.param2 = param2;
        this.lenResponse = lenResponse;
    }

    public DefaultApduCommand(byte cla, byte ins, byte param1, byte param2, byte[] data, byte lenResponse) {
        this.cla = cla;
        this.ins = ins;
        this.param1 = param1;
        this.param2 = param2;
        lenData = (byte)data.length;
        this.data = data;
        this.lenResponse = lenResponse;
    }

    public DefaultApduCommand(byte[] command) {
        if(command.length < 5) {
            throw new IllegalArgumentException();
        }

        cla = command[0];
        ins = command[1];
        param1 = command[2];
        param2 = command[3];
        lenResponse = command[4];

    }

    public DefaultApduCommand(byte[] command, byte[] data) {
        if(command.length < 5) {
            throw new IllegalArgumentException();
        }

        cla = command[0];
        ins = command[1];
        param1 = command[2];
        param2 = command[3];
        lenData = (byte)data.length;
        this.data = data;
    }

    public DefaultApduCommand(byte[] command, byte[] data, byte lenResponse) {
        if(command.length < 5) {
            throw new IllegalArgumentException();
        }

        cla = command[0];
        ins = command[1];
        param1 = command[2];
        param2 = command[3];
        lenData = (byte)data.length;
        this.data = data;
        this.lenResponse = lenResponse;
    }

}
