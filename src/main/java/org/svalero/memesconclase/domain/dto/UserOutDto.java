package org.svalero.memesconclase.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String avatar;
    private String phone;
    private Boolean active = true;
    private LocalDateTime dateRecord = LocalDateTime.now();
    private String sex;
}
