package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInDto {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate birthDate;

    private String avatar;

    @Pattern(regexp = "^[0-9]{9}$", message = "El número de teléfono debe contener 9 dígitos.")
    private String phone;

    @NotNull(message = "El estado activo es obligatorio.")
    private Boolean active;

    @Pattern(regexp = "Male|Female|Other", message = "El sexo debe ser Male, Female o Other.")
    private String sex;
}
