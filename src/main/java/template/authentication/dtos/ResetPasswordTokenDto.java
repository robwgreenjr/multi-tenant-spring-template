package template.authentication.dtos;

import java.util.UUID;

public class ResetPasswordTokenDto {
    public UUID token;
    public String password;
    public String passwordConfirmation;
}