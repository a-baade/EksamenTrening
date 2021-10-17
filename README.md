# Eksamen

## Oppgave

Mappeoppgaven for PGR203 er å lage en backend server for å håndtere spørreundersøkelser med svar på spørsmål. Serveren skal la bruker kunne opprette spørsmål,
registrere svaralternativer, opprette besvarelse og svare på spørsmål.
Programmet skal utvikles på en måte som demonstrerer programmeringsferdigheter slik det vises i undervisningen. Spesielt skal all funksjonalitet ha automatiske
tester og være fri for grunnleggende sikkerhetssvakheter. Programmet skal demonstre at kandidatene mestrer Sockets og JDBC bibliotekene i Java.

## Funksjonalitet
 
- Opprett et spørsmål (for eksempel: "hva slags mat liker du")
- Legg til svaralternativer (for eksempel: "salat", "brødskive", "kake")
- Registrer et svar
- Vise alle svar på et spørsmål
- Endre tittel og tekst på et spørsmål
### Ekstra funksjonalitet

- Hvert svaralternativ skal besvares på en skala
- Brukeren kan bestemme skalaen (for eksempel 1-10 eller 1-5) og merkelappen på en skala (for eksempel: "helt enig" til "helg uenig")
- Et sett med spørsmål kan knyttes sammen til en spørreundersøkelse
- Når en bruker besvarer en spørreundersøkelse skal brukerens fornavn, etternavn og epostadresse registreres. Registrer verdiene i en cookie og legg de inn i
  databasen når brukeren svare

# Plan
* **Http client**
* [x] Browser communicates with server(Sockets,HttpRequest)
* [x] HttpClient should read status code
* [x] HttpClient should read header fields
* [x] HttpClient should read content length
* [x] HttpClient should read message body
* **Http server** 
* [x] Initial server connection 
* [x] HttpServer should respond with 404
* [x] HttpServer should include request target in 404
* [x] Content-type
* [x] Return HTML-file from disk
* [x] Handle more than one request
* [ ] Process more than one file
* [ ] HttpServer should handle more than one request



