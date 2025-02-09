package ru.itis.semesterwork.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.Socket;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Player {
    private final String name;
    private final Socket socket;
    private boolean alive;
    private Point location;
}
