package com.itsziroy.shrinerevive.util;

public record PlayerTime(String uuid, String name, long time) {

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTime that = (PlayerTime) o;
        return uuid.equals(that.uuid);
    }
}
