package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome della sala è obbligatorio")
    private String nome;

    @NotBlank(message = "L'indirizzo della sala è obbligatorio")
    private String indirizzo;

    @NotNull(message = "La capienza è obbligatoria")
    @Min(value = 1, message = "La capienza minima deve essere di almeno 1 posto")
    private Integer capienza;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Proiezione> proiezioni;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public Integer getCapienza() { return capienza; }
    public void setCapienza(Integer capienza) { this.capienza = capienza; }
    public List<Proiezione> getProiezioni() { return proiezioni; }
    public void setProiezioni(List<Proiezione> proiezioni) { this.proiezioni = proiezioni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sala)) return false;
        Sala sala = (Sala) o;
        return Objects.equals(nome, sala.nome) &&
               Objects.equals(indirizzo, sala.indirizzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, indirizzo);
    }
}
