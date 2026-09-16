package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Regista {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome del regista è obbligatorio")
    private String nome;

    @NotBlank(message = "Il cognome del regista è obbligatorio")
    private String cognome;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascita;

    private String nazionalita;

    @Column(length = 2000)
    private String biografia;

    @OneToMany(mappedBy = "regista", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Film> film;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }
    public String getNazionalita() { return nazionalita; }
    public void setNazionalita(String nazionalita) { this.nazionalita = nazionalita; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public List<Film> getFilm() { return film; }
    public void setFilm(List<Film> film) { this.film = film; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Regista)) return false;
        Regista regista = (Regista) o;
        return Objects.equals(nome, regista.nome) &&
               Objects.equals(cognome, regista.cognome) &&
               Objects.equals(dataNascita, regista.dataNascita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, dataNascita);
    }
}
