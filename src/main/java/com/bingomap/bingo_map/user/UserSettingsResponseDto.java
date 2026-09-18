package com.bingomap.bingo_map.user;

import lombok.Getter;

@Getter
public class UserSettingsResponseDto {
    private final boolean notifyEmail;
    private final boolean locationEnabled;

    public UserSettingsResponseDto(boolean notifyEmail, boolean locationEnabled) {
        this.notifyEmail = notifyEmail;
        this.locationEnabled = locationEnabled;
    }
}