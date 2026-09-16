	-- 1. Registi
	INSERT INTO regista (id, nome, cognome, data_nascita, nazionalita, biografia) 
	VALUES (nextval('regista_seq'), 'Christopher', 'Nolan', '1970-07-30', 'Regno Unito', 'Regista, sceneggiatore e produttore celebre per film complessi e spettacolari come Inception e Oppenheimer.');
	
	INSERT INTO regista (id, nome, cognome, data_nascita, nazionalita, biografia) 
	VALUES (nextval('regista_seq'), 'Denis', 'Villeneuve', '1967-10-03', 'Canada', 'Uno dei più acclamati registi contemporanei di fantascienza, autore di Arrival, Blade Runner 2049 e Dune.');
	
	INSERT INTO regista (id, nome, cognome, data_nascita, nazionalita, biografia) 
	VALUES (nextval('regista_seq'), 'Greta', 'Gerwig', '1983-08-04', 'Stati Uniti', 'Regista e sceneggiatrice nota per Lady Bird, Piccole Donne e il fenomeno globale Barbie.');
	
	INSERT INTO regista (id, nome, cognome, data_nascita, nazionalita, biografia) 
	VALUES (nextval('regista_seq'), 'Paolo', 'Sorrentino', '1970-05-31', 'Italia', 'Vincitore dell Oscar per il miglior film straniero con La Grande Bellezza e autore di È stata la mano di Dio.');
	
	INSERT INTO regista (id, nome, cognome, data_nascita, nazionalita, biografia) 
	VALUES (nextval('regista_seq'), 'Hayao', 'Miyazaki', '1941-01-05', 'Giappone', 'Maestro indiscusso dell animazione mondiale, co-fondatore dello Studio Ghibli e premio Oscar.');
	
	
	-- 2. Festival Cinematografici
	INSERT INTO festival (id, nome, anno, citta, data_inizio, data_fine, descrizione) 
	VALUES (nextval('festival_seq'), 'Mostra Internazionale d Arte Cinematografica di Venezia', 2026, 'Venezia', '2026-08-26', '2026-09-05', 'Uno dei festival cinematografici più antichi e prestigiosi al mondo, ospitato al Lido di Venezia.');
	
	INSERT INTO festival (id, nome, anno, citta, data_inizio, data_fine, descrizione) 
	VALUES (nextval('festival_seq'), 'Festival di Cannes', 2026, 'Cannes', '2026-05-12', '2026-05-23', 'Il celebre festival della Costa Azzurra famoso per la Palma d Oro e le sue anteprime mondiali.');
	
	INSERT INTO festival (id, nome, anno, citta, data_inizio, data_fine, descrizione) 
	VALUES (nextval('festival_seq'), 'Berlinale - Festival Internazionale del Cinema di Berlino', 2026, 'Berlino', '2026-02-19', '2026-03-01', 'Festival d avanguardia che premia le migliori opere internazionali con l Orso d Oro.');
	
	INSERT INTO festival (id, nome, anno, citta, data_inizio, data_fine, descrizione) 
	VALUES (nextval('festival_seq'), 'Sundance Film Festival', 2026, 'Park City', '2026-01-21', '2026-01-31', 'Il più importante festival dedicato al cinema indipendente e ai nuovi autori emergenti.');
	
	
	-- 3. Sale Cinematografiche
	INSERT INTO sala (id, nome, indirizzo, capienza) 
	VALUES (nextval('sala_seq'), 'Sala Grande', 'Lungomare Guglielmo Marconi 18, Venezia', 1032);
	
	INSERT INTO sala (id, nome, indirizzo, capienza) 
	VALUES (nextval('sala_seq'), 'Auditorium Lumière', 'Boulevard de la Croisette, Cannes', 850);
	
	INSERT INTO sala (id, nome, indirizzo, capienza) 
	VALUES (nextval('sala_seq'), 'Cinema Astra', 'Via Po 14, Torino', 420);
	
	INSERT INTO sala (id, nome, indirizzo, capienza) 
	VALUES (nextval('sala_seq'), 'Sala Fellini', 'Piazza Cinecittà 1, Roma', 300);
	
	
	-- 4. Utenti e Credenziali
	INSERT INTO users (id, name, surname, email) 
	VALUES (nextval('users_seq'), 'Admin', 'Festival', 'admin@siwfestival.it');
	INSERT INTO credentials (id, username, password, role, user_id) 
	VALUES (nextval('credentials_seq'), 'admin', '$2a$10$lQpf/73orx5T3TBzbu.xNOXFgODGsR4wc39vTGc6Hbt8cdQVza.Pq', 'ADMIN', currval('users_seq'));
	
	INSERT INTO users (id, name, surname, email) 
	VALUES (nextval('users_seq'), 'Mario', 'Rossi', 'mario.rossi@email.com');
	INSERT INTO credentials (id, username, password, role, user_id) 
	VALUES (nextval('credentials_seq'), 'mario', '$2a$10$lQpf/73orx5T3TBzbu.xNOXFgODGsR4wc39vTGc6Hbt8cdQVza.Pq', 'DEFAULT', currval('users_seq'));
	
	INSERT INTO users (id, name, surname, email) 
	VALUES (nextval('users_seq'), 'Chiara', 'Bianchi', 'chiara.bianchi@email.com');
	INSERT INTO credentials (id, username, password, role, user_id) 
	VALUES (nextval('credentials_seq'), 'chiara', '$2a$10$lQpf/73orx5T3TBzbu.xNOXFgODGsR4wc39vTGc6Hbt8cdQVza.Pq', 'DEFAULT', currval('users_seq'));
	
	
	-- 5. Film
	INSERT INTO film (id, titolo, anno, durata, genere, paese_produzione, trama, regista_id) 
	VALUES (nextval('film_seq'), 'Oppenheimer', 2023, 180, 'Biografico / Drammatico', 'Stati Uniti', 'La storia del fisico J. Robert Oppenheimer e del Progetto Manhattan durante la Seconda Guerra Mondiale.', (SELECT id FROM regista WHERE cognome = 'Nolan' LIMIT 1));
	
	INSERT INTO film (id, titolo, anno, durata, genere, paese_produzione, trama, regista_id) 
	VALUES (nextval('film_seq'), 'Dune - Parte Due', 2024, 166, 'Fantascienza / Avventura', 'Stati Uniti', 'Paul Atreides si unisce ai Fremen per vendicare la sua famiglia e salvare il destino di Arrakis.', (SELECT id FROM regista WHERE cognome = 'Villeneuve' LIMIT 1));
	
	INSERT INTO film (id, titolo, anno, durata, genere, paese_produzione, trama, regista_id) 
	VALUES (nextval('film_seq'), 'Barbie', 2023, 114, 'Commedia / Fantastico', 'Stati Uniti', 'Barbie e Ken vivono nel colorato mondo di Barbieland fino a quando non affrontano la realtà nel mondo reale.', (SELECT id FROM regista WHERE cognome = 'Gerwig' LIMIT 1));
	
	INSERT INTO film (id, titolo, anno, durata, genere, paese_produzione, trama, regista_id) 
	VALUES (nextval('film_seq'), 'La Grande Bellezza', 2013, 142, 'Drammatico', 'Italia', 'Jep Gambardella riflette sulla vacuità della vita mondana romana nel giorno del suo sessantacinquesimo compleanno.', (SELECT id FROM regista WHERE cognome = 'Sorrentino' LIMIT 1));
	
	INSERT INTO film (id, titolo, anno, durata, genere, paese_produzione, trama, regista_id) 
	VALUES (nextval('film_seq'), 'Il Ragazzo e l Airone', 2023, 124, 'Animazione / Fantastico', 'Giappone', 'Un giovane ragazzo entra in un mondo magico guidato da un misterioso airone cenerino dopo la morte della madre.', (SELECT id FROM regista WHERE cognome = 'Miyazaki' LIMIT 1));
	
	
	-- 6. Associazione Film - Festival (Tabella Join Many-to-Many)
	INSERT INTO festival_film (festival_id, film_id) 
	VALUES ((SELECT id FROM festival WHERE nome LIKE '%Venezia%' LIMIT 1), (SELECT id FROM film WHERE titolo = 'Oppenheimer' LIMIT 1));
	
	INSERT INTO festival_film (festival_id, film_id) 
	VALUES ((SELECT id FROM festival WHERE nome LIKE '%Venezia%' LIMIT 1), (SELECT id FROM film WHERE titolo = 'La Grande Bellezza' LIMIT 1));
	
	INSERT INTO festival_film (festival_id, film_id) 
	VALUES ((SELECT id FROM festival WHERE nome LIKE '%Cannes%' LIMIT 1), (SELECT id FROM film WHERE titolo = 'Dune - Parte Due' LIMIT 1));
	
	INSERT INTO festival_film (festival_id, film_id) 
	VALUES ((SELECT id FROM festival WHERE nome LIKE '%Cannes%' LIMIT 1), (SELECT id FROM film WHERE titolo = 'Barbie' LIMIT 1));
	
	INSERT INTO festival_film (festival_id, film_id) 
	VALUES ((SELECT id FROM festival WHERE nome LIKE '%Berlinale%' LIMIT 1), (SELECT id FROM film WHERE titolo = 'Il Ragazzo e l Airone' LIMIT 1));
	
	
	-- 7. Programma Proiezioni
	INSERT INTO proiezione (id, data, ora, stato, festival_id, film_id, sala_id) 
	VALUES (nextval('proiezione_seq'), '2026-08-27', '18:00:00', 'SCHEDULED', 
	        (SELECT id FROM festival WHERE nome LIKE '%Venezia%' LIMIT 1), 
	        (SELECT id FROM film WHERE titolo = 'Oppenheimer' LIMIT 1), 
	        (SELECT id FROM sala WHERE nome = 'Sala Grande' LIMIT 1));
	
	INSERT INTO proiezione (id, data, ora, stato, festival_id, film_id, sala_id) 
	VALUES (nextval('proiezione_seq'), '2026-08-28', '21:00:00', 'SCHEDULED', 
	        (SELECT id FROM festival WHERE nome LIKE '%Venezia%' LIMIT 1), 
	        (SELECT id FROM film WHERE titolo = 'La Grande Bellezza' LIMIT 1), 
	        (SELECT id FROM sala WHERE nome = 'Sala Grande' LIMIT 1));
	
	INSERT INTO proiezione (id, data, ora, stato, festival_id, film_id, sala_id) 
	VALUES (nextval('proiezione_seq'), '2026-05-14', '20:30:00', 'SCHEDULED', 
	        (SELECT id FROM festival WHERE nome LIKE '%Cannes%' LIMIT 1), 
	        (SELECT id FROM film WHERE titolo = 'Dune - Parte Due' LIMIT 1), 
	        (SELECT id FROM sala WHERE nome = 'Auditorium Lumière' LIMIT 1));
	
	
	-- 8. Recensioni
	INSERT INTO recensione (id, voto, testo, data_pubblicazione, utente_id, film_id) 
	VALUES (nextval('recensione_seq'), 5, 'Un capolavoro assoluto del cinema contemporaneo. Regia magistrale e colonna sonora mozzafiato!', '2026-08-20', 
	        (SELECT id FROM users WHERE email = 'mario.rossi@email.com' LIMIT 1), 
	        (SELECT id FROM film WHERE titolo = 'Oppenheimer' LIMIT 1));
	
	INSERT INTO recensione (id, voto, testo, data_pubblicazione, utente_id, film_id) 
	VALUES (nextval('recensione_seq'), 4, 'Visivamente straordinario, Denis Villeneuve porta la fantascienza a vette inarrivabili.', '2026-08-22', 
	        (SELECT id FROM users WHERE email = 'chiara.bianchi@email.com' LIMIT 1), 
	        (SELECT id FROM film WHERE titolo = 'Dune - Parte Due' LIMIT 1));
