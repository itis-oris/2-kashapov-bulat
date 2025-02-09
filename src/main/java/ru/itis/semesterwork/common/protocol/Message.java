package ru.itis.semesterwork.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class Message {
    public static final byte CONNECT = 0;
    public static final byte DISCONNECT = 1;
    public static final byte GAME_READY = 2;
    public static final byte GAME_START = 3;
    public static final byte PLAYER_STATE = 4;
    public static final byte GAME_RESULT = 5;
    public static final byte OPPONENT_DISCONNECT = 6;

    public static final byte[] START_BYTES = {0xA, 0xB};

    private byte[] data;
    private byte messageType;

    public static byte[] getBytes(Message message) {
        int length = START_BYTES.length + 1 + 4 + message.getData().length;
        byte[] result = new byte[length];
        Collectors
        int curIndex = 0;
        for (int i = 0; i < START_BYTES.length; i++) {
            result[curIndex++] = START_BYTES[i];
        }
        result[curIndex++] = message.getMessageType();
        byte[] dataLength = ByteBuffer.allocate(4).putInt(message.getData().length).array();
        for (int i = 0; i < dataLength.length; i++) {
            result[curIndex++] = dataLength[i];
        }

        byte[] data = message.getData();
        for (int i = 0; i < data.length; i++) {
            result[curIndex++] = data[i];
        }
        return result;
    }

    public static Message getMessage(byte[] rawData) {
        if (rawData[0] == START_BYTES[0] && rawData[1] == START_BYTES[1]) {
            byte type = rawData[2];
            byte[] dataLength = new byte[4];
            int curIndex = 3;
            for (int i = 0; i < dataLength.length; i++) {
                dataLength[i] = rawData[curIndex++];
            }
            int length = ByteBuffer.wrap(dataLength).getInt();
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = rawData[curIndex++];
            }
            return new Message(data, type);
        } else {
            throw new RuntimeException();
        }
    }

    public static void sendMessage(Socket socket, Message message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(getBytes(message));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> toMap(Message message) {
        Map<String, String> map = new HashMap<>();
        String stringData = new String(message.getData());
        for (String stringPair : stringData.split(";")) {
            String[] dividedPair = stringPair.split(":");
            if (dividedPair.length != 2) {
                throw new RuntimeException();
            } else {
                map.put(dividedPair[0], dividedPair[1]);
            }
        }
        return map;
    }
}
