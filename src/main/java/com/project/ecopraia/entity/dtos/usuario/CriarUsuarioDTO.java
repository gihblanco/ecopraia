package com.project.ecopraia.entity.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CriarUsuarioDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 a 255 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public CriarUsuarioDTO(
            @NotBlank(message = "O nome é obrigatório") @Size(min = 3, max = 255, message = "O nome deve ter entre 3 a 255 caracteres") String nome,
            @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,
            @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public CriarUsuarioDTO() {

    }
}
