package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Festival;
import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.repository.FestivalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.*;

@Service
public class AnalisiPerformanceService {

    private static final Logger log = LoggerFactory.getLogger(AnalisiPerformanceService.class);
    private static final int RIPETIZIONI = 5;

    @Autowired
    private FilmService filmService;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    public synchronized Map<String, BenchmarkResult> eseguiBenchmark() {

        Statistics stats = entityManagerFactory
                .unwrap(SessionFactory.class)
                .getStatistics();
        stats.setStatisticsEnabled(true);

        // Seleziona il primo festival disponibile per il test
        List<Festival> festivals = festivalRepository.findAll();
        Long festivalId = festivals.isEmpty() ? null : festivals.get(0).getId();

        Map<String, BenchmarkResult> risultati = new LinkedHashMap<>();

        if (festivalId == null) {
            risultati.put("Nessun Festival Disponibile", new BenchmarkResult(0, 0,
                    "Nessun festival presente nel database per eseguire il benchmark.", Collections.emptyList(), 0));
            return risultati;
        }

        risultati.put("LAZY (default JPA)", misura("LAZY", () -> filmService.findFilmDiFestivalLazy(festivalId), stats,
                "findAll/findByFestivalId standard — accesso LAZY. Il template accede a film.regista.nome " +
                "per ogni film, causando 1 query SQL aggiuntiva per regista: problema N+1."));

        risultati.put("EAGER (N+1)", misura("EAGER", () -> filmService.findFilmDiFestivalEagerLoad(festivalId), stats,
                "Caricamento film + Hibernate.initialize(regista) riga per riga (nessun JOIN): " +
                "una query SQL separata per ogni film, riproducendo esattamente il problema N+1."));

        risultati.put("JOIN FETCH", misura("JOIN FETCH", () -> filmService.findFilmDiFestivalJoinFetch(festivalId), stats,
                "SELECT DISTINCT f FROM Film f JOIN FETCH f.regista JOIN f.festival — " +
                "Carica film e registi in un'unica query SQL, eliminando le query N+1."));

        risultati.put("@EntityGraph (regista + festival)", misura("EntityGraph", () -> filmService.findFilmDiFestivalEntityGraph(festivalId), stats,
                "@EntityGraph(attributePaths={\"regista\", \"festival\"}) — " +
                "Alternativa dichiarativa a JOIN FETCH. Carica tutte le associazioni in un'unica query " +
                "azzerando il problema N+1 in modo pulito e manutenibile."));

        return risultati;
    }

    private BenchmarkResult misura(String nome,
                                   java.util.function.Supplier<List<Film>> strategia,
                                   Statistics stats,
                                   String descrizione) {
        List<Long> tempi = new ArrayList<>();
        final int[] nFilmArray = new int[1];
        long queryCount = 0;

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setReadOnly(true);

        for (int i = 0; i < RIPETIZIONI; i++) {
            stats.clear();

            long inizio = System.nanoTime();
            transactionTemplate.execute(status -> {
                entityManager.clear();

                List<Film> risultato = strategia.get();
                nFilmArray[0] = risultato.size();

                for (Film f : risultato) {
                    if (f.getRegista() != null) {
                        f.getRegista().getNome();
                        f.getRegista().getCognome();
                    }
                }
                return null;
            });
            long fine = System.nanoTime();

            long ms = (fine - inizio) / 1_000_000;
            tempi.add(ms);

            long queryEseguite = stats.getPrepareStatementCount();
            if (i > 0) {
                queryCount += queryEseguite;
            }

            log.info("[PERF] Strategia '{}' - run {}: {} ms | query SQL: {}", nome, i + 1, ms, queryEseguite);
        }

        List<Long> tempiSenzaWarmup = new ArrayList<>(tempi.subList(1, tempi.size()));
        tempiSenzaWarmup.sort(Long::compare);
        long mediana = tempiSenzaWarmup.get(tempiSenzaWarmup.size() / 2);

        long queryMedie = queryCount / (RIPETIZIONI - 1);

        return new BenchmarkResult(mediana, nFilmArray[0], descrizione, tempi, queryMedie);
    }

    
    
    
    
    
    public static class BenchmarkResult {
        private final long tempoMs;
        private final int nFilm;
        private final String descrizione;
        private final List<Long> tuttiTempi;
        private final long queryCount;

        public BenchmarkResult(long tempoMs, int nFilm, String descrizione,
                               List<Long> tuttiTempi, long queryCount) {
            this.tempoMs = tempoMs;
            this.nFilm = nFilm;
            this.descrizione = descrizione;
            this.tuttiTempi = tuttiTempi;
            this.queryCount = queryCount;
        }

        public long getTempoMs() { return tempoMs; }
        public int getNFilm() { return nFilm; }
        public String getDescrizione() { return descrizione; }
        public List<Long> getTuttiTempi() { return tuttiTempi; }
        public long getQueryCount() { return queryCount; }

        public String getComportamento() {
            if (queryCount <= 2) {
                return "1 query (SELECT unico)";
            } else if (queryCount < nFilm) {
                return "N+1 parziale (associazione non coperta dal fetch)";
            } else {
                return "1 + N (N+1 problem)";
            }
        }

        public String getBadgeClass() {
            if (queryCount <= 2) return "badge badge-fast";
            if (queryCount < nFilm) return "badge badge-ok";
            return "badge badge-slow";
        }

        public String getBadgeText() {
            if (queryCount <= 2) return "Ottimale";
            if (queryCount < nFilm) return "Parzialmente ottimizzato";
            return "Inefficiente (N+1)";
        }
    }
}
