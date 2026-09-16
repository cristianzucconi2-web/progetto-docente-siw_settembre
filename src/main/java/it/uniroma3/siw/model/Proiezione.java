package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Proiezione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "La data è obbligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @NotNull(message = "L'ora è obbligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime ora;

    @NotNull(message = "Lo stato è obbligatorio")
    @Enumerated(EnumType.STRING)
    private StatoProiezione stato = StatoProiezione.SCHEDULED;

    @NotNull(message = "Il festival è obbligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"film", "proiezioni"})
    private Festival festival;

    @NotNull(message = "Il film è obbligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"proiezioni", "recensioni", "festival"})
    private Film film;

    @NotNull(message = "La sala è obbligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"proiezioni"})
    private Sala sala;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getOra() { return ora; }
    public void setOra(LocalTime ora) { this.ora = ora; }
    public StatoProiezione getStato() { return stato; }
    public void setStato(StatoProiezione stato) { this.stato = stato; }
    public Festival getFestival() { return festival; }
    public void setFestival(Festival festival) { this.festival = festival; }
    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }
    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proiezione)) return false;
        Proiezione that = (Proiezione) o;
        return Objects.equals(data, that.data) &&
               Objects.equals(ora, that.ora) &&
               Objects.equals(sala, that.sala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, ora, sala);
    }
}
