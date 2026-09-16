package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"utente_id", "film_id"})
})
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 1, message = "Il voto deve essere almeno 1")
    @Max(value = 5, message = "Il voto deve essere al massimo 5")
    private Integer voto;

    @NotBlank(message = "Il testo della recensione non può essere vuoto")
    @Size(min = 5, max = 1000, message = "La recensione deve avere tra 5 e 1000 caratteri")
    @Column(length = 1000)
    private String testo;

    private LocalDate dataPubblicazione;

    @ManyToOne
    private User utente;

    @ManyToOne
    @JsonIgnore
    private Film film;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVoto() { return voto; }
    public void setVoto(Integer voto) { this.voto = voto; }
    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }
    public LocalDate getDataPubblicazione() { return dataPubblicazione; }
    public void setDataPubblicazione(LocalDate dataPubblicazione) { this.dataPubblicazione = dataPubblicazione; }
    public User getUtente() { return utente; }
    public void setUtente(User utente) { this.utente = utente; }
    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recensione)) return false;
        Recensione that = (Recensione) o;
        return Objects.equals(utente, that.utente) &&
               Objects.equals(film, that.film);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utente, film);
    }
}
