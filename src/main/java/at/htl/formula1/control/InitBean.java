package at.htl.formula1.control;

import at.htl.formula1.boundary.ResultsRestClient;
import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Team;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class InitBean {

    private static final String TEAM_FILE_NAME = "teams.csv";
    private static final String RACES_FILE_NAME = "races.csv";

    @PersistenceContext
    EntityManager em;

    @Inject
    ResultsRestClient client;

    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        readTeamsAndDriversFromFile(TEAM_FILE_NAME);
        readRacesFromFile(RACES_FILE_NAME);
        client.readResultsFromEndpoint();

    }

    /**
     * Einlesen der Datei "races.csv" und Speichern der Objekte in der Tabelle F1_RACE
     *
     * @param racesFileName
     */
    private void readRacesFromFile(String racesFileName) {
        try {
            BufferedReader buffread = new BufferedReader((new InputStreamReader(getClass().getResourceAsStream("/races.csv"))));
            String line;
            buffread.readLine();
            while ((line = buffread.readLine()) != null) {
                String rowCell[] = line.split(";");
                Race race = new Race();
                race.setId(Long.parseLong(rowCell[0]));
                race.setCountry(rowCell[1]);
                //System.out.println(LocalDate.parse(rowCell[2]));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                race.setDate(LocalDate.parse(rowCell[2], formatter));
                em.persist(race);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Einlesen der Datei "teams.csv".
     * Das String-Array jeder einzelnen Zeile wird der Methode persistTeamAndDrivers(...)
     * übergeben
     *
     * @param teamFileName
     */
    private void readTeamsAndDriversFromFile(String teamFileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/teams.csv")));
            br.readLine();
            String line2;
            while ((line2 = br.readLine()) != null) {
                String[] rowCell = line2.split(";");
                Driver d = new Driver();
                Team t = new Team();
                d.setName(rowCell[1]);
                d.setName(rowCell[2]);
                em.persist(d);
                em.persist(new Team(rowCell[0]));
                /*List<Driver> driver = this.em.createNamedQuery("Driver.getDriverByName", Driver.class)
                        .setParameter("NAME", rowCell[1])
                        .setParameter("NAME", rowCell[2])
                        .getResultList();
                Driver currentDriver;
                /*if (driver.size() != 1) {
                    currentDriver = new Driver(currentDriver.setName(rowCell[1]));
                    em.persist(currentDriver);
                } else {
                    currentDriver = driver.get(0);
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Es wird überprüft ob es das übergebene Team schon in der Tabelle F1_TEAM gibt.
     * Falls nicht, wird das Team in der Tabelle gespeichert.
     * Wenn es das Team schon gibt, dann liest man das Team aus der Tabelle und
     * erstellt ein Objekt (der Klasse Team).
     * Dieses Objekt wird verwendet, um die Fahrer mit Ihrem jeweiligen Team
     * in der Tabelle F!_DRIVER zu speichern.
     *
     * @param line String-Array mit den einzelnen Werten der csv-Datei
     */

    private void persistTeamAndDrivers(String[] line) {

    }


}
