package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;
import at.htl.formula1.entity.Team;

import javax.json.*;
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
    // tag::readResults[]
    public void readResultsFromEndpoint() {
        Response response = this.target.request(MediaType.APPLICATION_JSON).get();
        JsonArray payload = response.readEntity(JsonArray.class);
        /*for (JsonValue item : payload) {
            JsonObject resultJson = Json.createObjectBuilder().build();
            resultJson.
        }*/
        //JsonObject object = payload.getJsonObject(0);
        persistResult(payload);
    }
    // end::readResults[]

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
    // tag::persistResults[]
    @Transactional
    void persistResult(JsonArray resultsJson) {
        for (JsonValue value : resultsJson) {
            JsonObject resultJson = value.asJsonObject();
            Driver driver = em
                    .createNamedQuery("Driver.getDriverByName", Driver.class)
                    .setParameter("NAME", resultJson.getString("driverFullName"))
                    .getSingleResult();
            Race race = em
                    .createQuery("select r from Race r where r.id = :ID", Race.class)
                    .setParameter("ID", Long.valueOf(resultJson.getInt("raceNo")))
                    .getSingleResult();
            em.persist(new Result(race, resultJson.getInt("position"), driver));
        }
    }
    // end::persistResults[]

}
