package ru.itis.semesterwork.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trap {
    private Point[] trajectory;
    private double speed;
    private long startTime;
    private long endTime;
    private String type;
}
