package com.capstone.backend.dtos;
import com.capstone.backend.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserDto(
        String id,

        @Size(min=3 , max = 15 , message = "Name must be 3-15 characters !!")
        String name,

        @Email(message = "Invalid User Email")
        @NotBlank(message = "Email cannot be blank")
        @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
                message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @Size(min=4 , max = 6 , message = "Invalid gender")
        Gender gender,
        @NotBlank(message = "Write something about yourself...!!!")
        String about,
        String imageName
) {
}

