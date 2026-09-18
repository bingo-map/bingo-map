package com.bingomap.bingo_map.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsUpdateDto {
    private boolean signupEnabled;
    private boolean maintenanceMode;
    private String maintenanceMessage;
}