package de.karrieretutor.springboot.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Login {
    @Email
    @NotBlank
    String email;
    @NotBlank
    String password;
    String passwordRepeat;

    boolean registerMode;

    public Login() {}

    public Login(boolean registerMode) {
        this.registerMode = registerMode;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }
    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public boolean isRegisterMode() {
        return registerMode;
    }
    public void setRegisterMode(boolean registerMode) {
        this.registerMode = registerMode;
    }
}
