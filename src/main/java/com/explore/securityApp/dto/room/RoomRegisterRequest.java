package com.explore.securityApp.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRegisterRequest {

    @NotBlank
    private String name;

    private Integer capacity;

}
