/**
 * =========================================================================================
 *                          FESTIVALHUB - RICERCA LIVE IN TEMPO REALE
 * =========================================================================================
 * Linguaggio: JavaScript (ES6) con React 18 e JSX.
 * Esecuzione: Gira interamente nel BROWSER dell'utente (Client-side), NON nel server Java.
 * 
 * COME DIALOGA CON SPRING BOOT:
 * 1. L'utente scrive nella casella di ricerca (input).
 * 2. Il timer aspetta 300 millisecondi (debounce).
 * 3. JavaScript fa una chiamata HTTP di tipo GET a Spring Boot:
 *    GET /api/movies/ricerca?q=PAROLA_CERCATA
 * 4. Spring Boot (FilmRestController.java) risponde inviando una lista di film in JSON.
 * 5. React riceve il JSON e disegna le card dei film a schermo senza ricaricare la pagina!
 * =========================================================================================
 */

function SearchBar() {

  // ---------------------------------------------------------------------------------------
  // 1. LE VARIABILI DI STATO (useState)
  // In React, 'useState' è come una variabile Java che, quando cambia valore,
  // dice al browser di ridisegnare la pagina in automatico.
  // ---------------------------------------------------------------------------------------

  // inputValue: Contiene il testo digitato dall'utente nella casella in tempo reale (es: "Oppen")
  const [inputValue, setInputValue] = React.useState('');

  // searchQuery: Contiene la parola confermata dopo il timer di 300ms (es: "Oppenheimer")
  const [searchQuery, setSearchQuery] = React.useState('');

  // films: È la lista di oggetti film (List<Film>) arrivata da Spring Boot in formato JSON
  const [films, setFilms] = React.useState([]);

  // isLoading: Un booleano (true/false). Se è true, mostra "Caricamento film in corso..."
  const [isLoading, setIsLoading] = React.useState(false);

  // error: Se il server va in errore o è spento, contiene il messaggio di errore da mostrare
  const [error, setError] = React.useState(null);


  // ---------------------------------------------------------------------------------------
  // 2. IL TIMER DI ATTESA (Debounce di 300ms)
  // Perché serve? Se scrivi "Batman" (6 lettere), senza questo timer manderesti 6 richieste
  // al server in un secondo! Questo timer aspetta che tu finisca di scrivere la parola.
  // ---------------------------------------------------------------------------------------
  React.useEffect(() => {
    // Avvia il timer di 300 millisecondi
    const handler = setTimeout(() => {
      setSearchQuery(inputValue); // Copia il testo dentro searchQuery per far partire la ricerca
    }, 300);

    // Se l'utente preme un'altra lettera prima di 300ms, cancella il vecchio timer e ne crea uno nuovo
    return () => {
      clearTimeout(handler);
    };
  }, [inputValue]); // Questo blocco si attiva ogni volta che 'inputValue' cambia


  // ---------------------------------------------------------------------------------------
  // 3. LA CHIAMATA A SPRING BOOT (fetch)
  // Questo blocco si attiva ogni volta che 'searchQuery' cambia (dopo i 300ms).
  // Fa la richiesta HTTP asincrona all'endpoint REST di Spring Boot.
  // ---------------------------------------------------------------------------------------
  React.useEffect(() => {
    // Se la casella NON è vuota:
    if (searchQuery.trim().length > 0) {
      setIsLoading(true); // 1. Mostra la scritta di caricamento
      setError(null);     // 2. Resetta eventuali errori precedenti

      // 3. Chiamata HTTP GET a Spring Boot: /api/movies/ricerca?q=...
      fetch(`/api/movies/ricerca?q=${encodeURIComponent(searchQuery)}`)
        .then(res => {
          // Se il server risponde con errore (es: 404, 500), lancia un'eccezione
          if (!res.ok) throw new Error("Errore durante il recupero dei dati dal server.");
          // Converte il testo JSON ricevuto in veri oggetti JavaScript
          return res.json();
        })
        .then(data => {
          // data è la lista List<Film> inviata da FilmRestController.java
          setFilms(data);       // 4. Salva i film trovati dentro lo stato 'films'
          setIsLoading(false);  // 5. Nasconde la scritta di caricamento
        })
        .catch(err => {
          // Se c'è un errore di connessione col server Java:
          console.error("Errore fetch:", err);
          setError(err.message);
          setIsLoading(false);
        });
    } else {
      // Se l'utente ha cancellato tutto il testo, svuota la lista dei film
      setFilms([]);
    }
  }, [searchQuery]); // Questo blocco si attiva ogni volta che 'searchQuery' cambia


  // ---------------------------------------------------------------------------------------
  // 4. COSA DISEGNARE A SCHERMO (Interfaccia Grafica JSX)
  // È simile a un file HTML, ma permette di inserire variabili JavaScript tra parentesi graffe { }
  // ---------------------------------------------------------------------------------------
  return (
    <div style={{ marginTop: '25px' }}>

      {/* ========================================================================= */}
      {/* 4.1 CASELLA DI TESTO (Input di ricerca)                                  */}
      {/* ========================================================================= */}
      <div style={{ position: 'relative', marginBottom: '35px' }}>
        <input 
          type="text" 
          placeholder="Cerca film per titolo, genere (es: Drammatico, Fantascienza) o cognome del regista..." 
          value={inputValue}
          // Quando l'utente scrive una lettera, aggiorna lo stato 'inputValue':
          onChange={(e) => setInputValue(e.target.value)}
          style={{ 
            width: '100%', 
            padding: '16px 22px', 
            fontSize: '1.05rem',
            border: '2px solid #eef0f4',
            borderRadius: '18px',
            outline: 'none',
            boxSizing: 'border-box',
            boxShadow: '0 4px 20px rgba(0, 0, 0, 0.03)',
            fontFamily: 'inherit',
            transition: 'all 0.3s ease'
          }} 
          onFocus={(e) => e.target.style.borderColor = '#4338ca'}
          onBlur={(e) => e.target.style.borderColor = '#eef0f4'}
        />
      </div>

      {/* ========================================================================= */}
      {/* 4.2 MESSAGGIO DI ERRORE (Se error non è null)                            */}
      {/* ========================================================================= */}
      {error && (
        <div style={{ color: '#dc2626', padding: '15px 20px', background: '#fee2e2', borderRadius: '12px', marginBottom: '20px' }}>
          ⚠️ {error}
        </div>
      )}

      {/* ========================================================================= */}
      {/* 4.3 SPINNER DI CARICAMENTO (Se isLoading è true)                         */}
      {/* ========================================================================= */}
      {isLoading && (
        <div style={{ textAlign: 'center', padding: '30px', color: '#4338ca', fontWeight: 600 }}>
          Caricamento film in corso...
        </div>
      )}

      {/* ========================================================================= */}
      {/* 4.4 NESSUN RISULTATO TROVATO (Se la ricerca non ha dato film)            */}
      {/* ========================================================================= */}
      {!isLoading && searchQuery.trim().length > 0 && films.length === 0 && (
        <div style={{ textAlign: 'center', padding: '50px 20px', color: '#64748b' }}>
          <div style={{ fontSize: '3rem', marginBottom: '10px' }}>🎬</div>
          <h3>Nessun film trovato</h3>
          <p>Nessun film corrisponde alla ricerca "{searchQuery}". Prova con parole chiave differenti.</p>
        </div>
      )}

      {/* ========================================================================= */}
      {/* 4.5 STATO INIZIALE (Prima che l'utente scriva qualcosa)                  */}
      {/* ========================================================================= */}
      {searchQuery.trim().length === 0 && (
        <div style={{ textAlign: 'center', padding: '40px 20px', color: '#94a3b8', border: '2.5px dashed #e2e8f0', borderRadius: '24px' }}>
          <p style={{ fontSize: '1.05rem', margin: 0 }}>Digita il titolo di un film, un genere o un regista per visualizzare i risultati in tempo reale.</p>
        </div>
      )}

      {/* ========================================================================= */}
      {/* 4.6 LA GRIGLIA CON LE CARD DEI FILM TROVATI                             */}
      {/* Questo è l'equivalente di <div th:each="film : ${films}"> di Thymeleaf!  */}
      {/* films.map(film => ...) cicla su ogni film e ne disegna la card:          */}
      {/* ========================================================================= */}
      <div style={{ 
        display: 'grid', 
        gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', 
        gap: '25px' 
      }}>
        {films.map(film => (
          <a 
            key={film.id} 
            href={`/film/${film.id}`} // Cliccando porta alla classica scheda film Thymeleaf!
            className="search-card"
            style={{ 
              textDecoration: 'none',
              color: 'inherit',
              background: '#fff', 
              padding: '25px', 
              borderRadius: '20px', 
              border: '2px solid #eef0f4', 
              boxShadow: '0 4px 15px rgba(0,0,0,0.02)',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between',
              transition: 'all 0.3s ease'
            }}
          >
            <div>
              {/* Titolo e Anno */}
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '10px' }}>
                <h3 style={{ margin: 0, fontSize: '1.25rem', color: '#111827' }}>{film.titolo}</h3>
                <span style={{ 
                  background: '#fef3c7', 
                  color: '#b45309', 
                  padding: '3px 10px', 
                  borderRadius: '12px', 
                  fontSize: '0.8rem', 
                  fontWeight: 700 
                }}>
                  {film.anno}
                </span>
              </div>

              {/* Trama del film */}
              <p style={{ fontSize: '0.9rem', color: '#64748b', margin: '0 0 15px 0', display: '-webkit-box', WebkitLineClamp: '2', WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                {film.trama ? film.trama : 'Nessuna trama disponibile.'}
              </p>
			  
              {/* Genere e Durata */}
              <div style={{ marginBottom: '15px' }}>
                <span style={{ 
                  background: '#eef2ff', 
                  color: '#4338ca', 
                  padding: '4px 10px', 
                  borderRadius: '8px', 
                  fontSize: '0.8rem', 
                  fontWeight: 600,
                  marginRight: '8px'
                }}>
                  🎭 {film.genere}
                </span>
                <span style={{ 
                  background: '#f1f5f9', 
                  color: '#475569', 
                  padding: '4px 10px', 
                  borderRadius: '8px', 
                  fontSize: '0.8rem', 
                  fontWeight: 600 
                }}>
                  ⏱️ {film.durata} min
                </span>
              </div>
            </div>

            {/* Nome del Regista e Freccia */}
            <div style={{ 
              borderTop: '1px solid #f1f5f9', 
              paddingTop: '15px', 
              display: 'flex', 
              justifyContent: 'space-between', 
              alignItems: 'center',
              fontSize: '0.88rem',
              color: '#64748b'
            }}>
              <div>
                🎥 <span style={{ fontWeight: 600, color: '#1f2937' }}>
                  {film.regista ? `${film.regista.nome} ${film.regista.cognome}` : 'Regista non indicato'}
                </span>
              </div>
              <div style={{ fontWeight: 600, color: '#4338ca' }}>
                Dettagli →
              </div>
            </div>
          </a>
        ))}
      </div>

      {/* Stile animazione al passaggio del mouse */}
      <style>{`
        .search-card:hover {
          transform: translateY(-6px);
          border-color: #4338ca !important;
          box-shadow: 0 12px 30px rgba(67, 56, 202, 0.1) !important;
        }
      `}</style>
    </div>
  );
}

// =========================================================================================
// 5. AGGANCIO AL DOM
// Prende il componente SearchBar e lo monta dentro il tag <div id="root"> di cerca.html
// =========================================================================================
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<SearchBar />);
