package org.svalero.memesconclase.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.svalero.memesconclase.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDto {
    private long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String avatar;
    private String phone;
    private Boolean active = true;
    private LocalDateTime dateRecord = LocalDateTime.now();
    private String sex;

    // Constructor personalizado que convierte un User en un UserOutDto
    public UserOutDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.avatar = user.getAvatar();
        this.phone = user.getPhone();
        this.active = user.getActive();
        this.dateRecord = user.getDateRecord();
        this.sex = user.getSex();
    }
}
