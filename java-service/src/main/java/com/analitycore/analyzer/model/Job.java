package com.analitycore.analyzer.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Job {

 //   @Id
//    @GeneratedValue
  //  private UUID id;

    @Id
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String texto;

    private String estado;

    @Column(columnDefinition = "TEXT")
    private String resultado;

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}
