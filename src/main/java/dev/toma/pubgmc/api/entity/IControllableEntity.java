package dev.toma.pubgmc.api.entity;

public interface IControllableEntity {

    void onInputUpdate(boolean forward, boolean backward, boolean right, boolean left);
}
