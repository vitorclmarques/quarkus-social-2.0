package io.github.vitorclmarques.quarkussocial.rest.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;


@Setter
@Getter
@Data
public class CreateUserRequest {


    //anottation para verificar se a string esta em branco
    @NotBlank(message = "Name is Required")
    private String name;

    @NotNull(message = "Age is Required")
    private Integer age;


}
