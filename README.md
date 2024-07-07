Personalverzeichnis


Anleitung zur Importierung in Eclipse

1. Importieren
2. Im Import-Menu auf General -> Existing Projects into Workspace
3. Select archive file auswählen und die personalverzeichnis.zip aus dem Explorer ausählen
4. Im Bereich "projects" wird dann das Projekt Personalverzeichnis angezeigt
5. Projekt anwählen und auf Finish clicken
6. FERTIG


Projekt Lokal ausführen
1. JDK 17 installieren
2. Postgres Serverdaten (bei lokalen Anwendungen meist localhost:5432/postgres) bereitlegen
3. Postgres Username und Password für DB wissen
4. Mittels Kommandozeile in Ordner navigieren wo sich personalverzeichnis.jar befindet (cd Ordner mit Jar)
5. Folgenden Befehl ausführen:
6. java -Dspring.datasource.url=jdbc:postgresql://Postgress-Adresse -Dspring.datasource.username=Username -Dspring.datasource.password=Password  -jar personalverzeichnis-0.0.1-SNAPSHOT.jar
