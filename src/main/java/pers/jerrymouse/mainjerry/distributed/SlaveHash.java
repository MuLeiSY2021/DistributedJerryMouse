package pers.jerrymouse.mainjerry.distributed;

public class SlaveHash {
    private final Integer position;

    private final Integer hashCode;

    public SlaveHash(Integer position, Integer hashCode) {
        this.position = position;
        this.hashCode = hashCode;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getHashCode() {
        return hashCode;
    }
}
