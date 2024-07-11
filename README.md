Personalverzeichnis


Anleitung zur Importierung in Eclipse

1. Importieren
2. Im Import-Menu auf General -> Existing Projects into Workspace
3. Select archive file auswählen und die personalverzeichnis.zip aus dem Explorer ausählen
4. Im Bereich "projects" wird dann das Projekt Personalverzeichnis angezeigt
5. Projekt anwählen und auf Finish clicken
6. FERTIG


Projekt Lokal ausführen

!!!Achtung!!!
In der kompilierten .jar Datei werden folgende Parameter für die Postgresql Datenbank angenommen: 
Es handelt sich um eine frisch instantiierte Postgresql Datenbank mit dem Standarduser: 

Username: postgres
Password: postgres

und der Standard-Datenbank mit dem Namen postgres, welche lokal auf dem auszuführenden System läuft. 
Sollte dies zutreffen, dann kann das Programm direkt gestartet werden. Sollte dies nicht so sein, dann müssen
noch folgende Punkte beachtet werden. 

1. Öffnen Sie die Kommandozeile auf ihrem Gerät Terminal (Linux, MacOS) oder Eingabeaufforderung (Windows)
2. Navigieren Sie zu dem Ordner, in welchem sich die kompilierte .jar Datei befindet (Bei exportierten Projekten befindet diese sich im /target-Verzeichnis)
3. Führen Sie den folgenden Befehl aus: 
   
        java -Dspring.datasource.url=jdbc:postgresql://Postgress-Adresse -Dspring.datasource.username=Username -Dspring.datasource.password=Password -jar personalverzeichnis-0.0.1-SNAPSHOT.jar

Hierbei ist folgendes zu beachten:
1. Postgres-Adresse durch die lokal vorliegende Adresse ändern (meist localhost:5432/postgres)
2. Username durch den gewählten Username ersetzen
3. Password durch das gewählte Password ersetzen



!!!Achtung!!!

Das Programm ist so eingestellt, dass ALLE Daten in der angegebenen Datenbank bei Systemstart überschrieben werden.
Sollten also noch wichtige Daten auf dieser Datenbankinstanz existieren, sichern Sie diese um Datenverlust zu vermeiden