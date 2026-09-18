package com.bingomap.bingo_map.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSettingsUpdateDto {
    private boolean notifyEmail;
    private boolean locationEnabled;
}