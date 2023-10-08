package com.itsziroy.shrinerevive.util;

import java.util.Objects;

public record PlayerTime(String uuid, String name, long time) {

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTime that = (PlayerTime) o;
        return Objects.equals(uuid, that.uuid);
    }
}
