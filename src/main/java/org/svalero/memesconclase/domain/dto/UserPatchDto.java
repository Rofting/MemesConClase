package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserPatchDto {
    private String name;

    @Email(message = "El formato del correo electrónico es inválido.")
    private String email;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    private LocalDate birthDate;

    private String avatar;

    @Pattern(regexp = "^[0-9]{9}$", message = "El número de teléfono debe contener 9 dígitos.")
    private String phone;

    private Boolean active;

    @Pattern(regexp = "Male|Female|Other", message = "El sexo debe ser Male, Female o Other.")
    private String sex;
}
