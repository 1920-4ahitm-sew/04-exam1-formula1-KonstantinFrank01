package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;
import at.htl.formula1.entity.Team;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class ResultsRestClient {


    public static final String RESULTS_ENDPOINT = "http://vm90.htl-leonding.ac.at/results";
    private Client client = ClientBuilder.newClient();
    private WebTarget target = client.target(RESULTS_ENDPOINT);

    @PersistenceContext
    EntityManager em;


    /**
     * Vom RestEndpoint werden alle Result abgeholt und in ein JsonArray gespeichert.
     * Dieses JsonArray wird an die Methode persistResult(...) übergeben
     */
    public void readResultsFromEndpoint() {
        Response response = this.target.request(MediaType.APPLICATION_JSON).get();
        JsonArray payload = response.readEntity(JsonArray.class);
        JsonObject object = payload.getJsonObject(0);
        persistResult(payload);
    }

    /**
     * Das JsonArray wird durchlaufen (iteriert). Man erhäjt dabei Objekte vom
     * Typ JsonValue. diese werden mit der Methode .asJsonObject() in ein
     * JsonObject umgewandelt.
     *
     * zB:
     * for (JsonValue jsonValue : resultsJson) {
     *             JsonObject resultJson = jsonValue.asJsonObject();
     *             ...
     *
     *  Mit den entsprechenden get-Methoden können nun die einzelnen Werte
     *  (raceNo, position und driverFullName) ausgelesen werden.
     *
     *  Mit dem driverFullName wird der entsprechende Driver aus der Datenbank ausgelesen.
     *
     *  Dieser Driver wird dann dem neu erstellten Result-Objekt übergeben
     *
     * @param resultsJson
     */
    @Transactional
    void persistResult(JsonArray resultsJson) {
        JsonObject object = resultsJson.getJsonObject(0);
        List<JsonObject> values = resultsJson.getValuesAs(JsonObject.class);
        for (JsonObject value : values) {
            System.out.println(value);
            /**
             * Aufgabe 2: Import REST
             *
             * Als zweite Aufgabe sollen die Ergebnisse (Result) von einem gegebenen REST-Endpoint eingelesen werden.
             * Implementieren Sie hierfür die Klasse ResultRestClient:
             */
            //Race race = new Race(race.getId(), race.getCountry(), value.getString("raceNo"));
            Team team = new Team();
            Driver driver = new Driver(value.getString("driverFullName"), team);
            //em.persist(new Result(race, value.getInt("position"), driver));
        }
    }

}
