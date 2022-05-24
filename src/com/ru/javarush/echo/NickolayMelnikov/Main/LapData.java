package com.ru.javarush.echo.NickolayMelnikov.Main;

import java.time.LocalTime;

record LapData (String racerName, int lapNumber, LocalTime lapTime) {
    public String getRacerName() {
        return racerName;
    }

    public int getLapNumber() {
        return lapNumber;
    }

    public LocalTime getLapTime() {
        return lapTime;
    }
}
