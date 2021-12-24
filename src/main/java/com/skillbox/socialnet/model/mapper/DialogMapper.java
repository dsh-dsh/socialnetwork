package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.DialogDTO;
import com.skillbox.socialnet.model.entity.Dialog;
import lombok.Data;

@Data
public class DialogMapper {
    public static DialogDTO mapToDto(Dialog dialog) {
        return new DialogDTO();
    }
}
