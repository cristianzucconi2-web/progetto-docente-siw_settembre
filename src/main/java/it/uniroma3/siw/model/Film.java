package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;

    @NotNull(message = "L'anno è obbligatorio")
    @Min(value = 1888, message = "L'anno deve essere valido")
    private Integer anno;

    @NotNull(message = "La durata è obbligatoria")
    @Min(value = 1, message = "La durata deve essere di almeno 1 minuto")
    private Integer durata; // in minuti

    @NotBlank(message = "Il genere è obbligatorio")
    private String genere;

    @NotBlank(message = "Il paese di produzione è obbligatorio")
    private String paeseProduzione;

    @Column(length = 2000)
    private String trama;

    @NotNull(message = "Il regista è obbligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"film"})
    private Regista regista;

    @ManyToMany(mappedBy = "film")
    @JsonIgnoreProperties({"film", "proiezioni"})
    private List<Festival> festival;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Proiezione> proiezioni;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Recensione> recensioni;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public Integer getAnno() { return anno; }
    public void setAnno(Integer anno) { this.anno = anno; }
    public Integer getDurata() { return durata; }
    public void setDurata(Integer durata) { this.durata = durata; }
    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }
    public String getPaeseProduzione() { return paeseProduzione; }
    public void setPaeseProduzione(String paeseProduzione) { this.paeseProduzione = paeseProduzione; }
    public String getTrama() { return trama; }
    public void setTrama(String trama) { this.trama = trama; }
    public Regista getRegista() { return regista; }
    public void setRegista(Regista regista) { this.regista = regista; }
    public List<Festival> getFestival() { return festival; }
    public void setFestival(List<Festival> festival) { this.festival = festival; }
    public List<Proiezione> getProiezioni() { return proiezioni; }
    public void setProiezioni(List<Proiezione> proiezioni) { this.proiezioni = proiezioni; }
    public List<Recensione> getRecensioni() { return recensioni; }
    public void setRecensioni(List<Recensione> recensioni) { this.recensioni = recensioni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return Objects.equals(titolo, film.titolo) &&
               Objects.equals(anno, film.anno) &&
               Objects.equals(regista, film.regista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, anno, regista);
    }
}
