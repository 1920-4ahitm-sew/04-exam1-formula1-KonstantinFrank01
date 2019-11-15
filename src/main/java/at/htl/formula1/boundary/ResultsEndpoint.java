package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("results")
public class ResultsEndpoint {


    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    public JsonObject getPointsSumOfDriver(
            String name
    ) {
        return null;
    }

    /**
     * @param id des Rennens
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("winner/{id}")
    public Response findWinnerOfRace(@PathParam("id") long id) {

        return null;
    }


    // Ergänzen Sie Ihre eigenen Methoden ...

}
