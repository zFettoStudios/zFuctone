package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import com.zfettostudios.zfuctone.model.constant.world.PermissionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(makeFinal = false)
public class PermissionSetting {
    String name;
    PermissionType type;
}
