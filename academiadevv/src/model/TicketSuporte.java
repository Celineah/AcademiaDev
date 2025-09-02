package model;

import java.time.LocalDateTime;

public class TicketSuporte {
    private String titulo;
    private String mensagem;
    private Usuario usuario;
    private LocalDateTime dataCriacao;

    public TicketSuporte(String titulo, String mensagem, Usuario usuario) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.usuario = usuario;
        this.dataCriacao = LocalDateTime.now();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
