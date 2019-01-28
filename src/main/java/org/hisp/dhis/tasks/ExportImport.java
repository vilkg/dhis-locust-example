package org.hisp.dhis.tasks;

import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import com.github.myzhan.locust4j.taskset.AbstractTaskSet;
import com.google.gson.JsonObject;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hisp.dhis.Dhis2RestAssured;

import static io.restassured.config.RestAssuredConfig.*;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class ExportImport extends AbstractTask
{

    public int getWeight()
    {
        return 1;
    }

    public String getName()
    {
        return "Import big dataset";
    }

    public void execute()
        throws Exception
    {

        new LoginTask().execute();
        Response response = Dhis2RestAssured.getRestAssured()
            .given()
            .get("api/metadata")
            .thenReturn();


        JsonObject responseBody = response.body().as( JsonObject.class );

        response = Dhis2RestAssured.getRestAssured()
            .given()
            .contentType( ContentType.JSON )
            .body( responseBody )
            .post("api/metadata.json?async=false&importMode=COMMIT&identifier=UID&importReportMode=ERRORS&preheatMode=REFERENCE&importStrategy=CREATE_AND_UPDATE&atomicMode=ALL&mergeMode=MERGE&flushMode=AUTO&skipSharing=false&skipValidation=false&async=true&inclusionStrategy=NON_NULL")
            .thenReturn();

        if ( response.statusCode() == 200 ) {
            Locust.getInstance().recordSuccess( "http", getName(), response.getTime(), response.body().asByteArray().length);
            return;
        }

        response.prettyPrint();
        Locust.getInstance().recordFailure( "http", getName(), response.getTime(), response.body().print());



    }
}
