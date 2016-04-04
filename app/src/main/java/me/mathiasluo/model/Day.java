package me.mathiasluo.model;

import java.util.List;

public interface Day {
    String getName();

    Integer getDayOfWeek();

    List<Hours> getHours();
}
