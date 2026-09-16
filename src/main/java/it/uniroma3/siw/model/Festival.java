package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome del festival è obbligatorio")
    private String nome;

    @NotNull(message = "L'anno è obbligatorio")
    private Integer anno;

    @NotBlank(message = "La città è obbligatoria")
    private String citta;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInizio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFine;

    @Column(length = 2000)
    private String descrizione;

    @ManyToMany
    @JoinTable(
        name = "festival_film",
        joinColumns = @JoinColumn(name = "festival_id"),
        inverseJoinColumns = @JoinColumn(name = "film_id")
    )
    @JsonIgnoreProperties({"festival", "recensioni", "proiezioni"})
    private List<Film> film;

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Proiezione> proiezioni;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getAnno() { return anno; }
    public void setAnno(Integer anno) { this.anno = anno; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }
    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public List<Film> getFilm() { return film; }
    public void setFilm(List<Film> film) { this.film = film; }
    public List<Proiezione> getProiezioni() { return proiezioni; }
    public void setProiezioni(List<Proiezione> proiezioni) { this.proiezioni = proiezioni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Festival)) return false;
        Festival festival = (Festival) o;
        return Objects.equals(nome, festival.nome) &&
               Objects.equals(anno, festival.anno) &&
               Objects.equals(citta, festival.citta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, anno, citta);
    }
}
