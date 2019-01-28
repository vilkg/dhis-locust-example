package org.hisp.dhis.tasks;

import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.hisp.dhis.Dhis2RestAssured;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class EventImportAsync extends AbstractTask
{

    public int getWeight()
    {
        return 1;
    }

    public String getName()
    {
        return "Import big dataset async ";
    }

    public void execute()
        throws Exception
    {
        new LoginTask().execute();
        JsonObject metadata = new EventExportTask().executeAndGetBody();

        long time = System.currentTimeMillis();

        Response response = Dhis2RestAssured.getRestAssured()
            .given()
            .contentType( ContentType.JSON )
            .body( metadata )
            .post("api/metadata.json?async=true&importMode=COMMIT&identifier=UID&importReportMode=ERRORS&preheatMode=REFERENCE&importStrategy=CREATE_AND_UPDATE&atomicMode=ALL&mergeMode=MERGE&flushMode=AUTO&skipSharing=false&skipValidation=false&async=true&inclusionStrategy=NON_NULL")
            .thenReturn();

        if (response.statusCode() != 200) {
            Locust.getInstance().recordFailure( "http", getName(), time, response.body().prettyPrint() );
        }

        String url = response.jsonPath().getString( "response.relativeNotifierEndpoint" );

        response = isCompleted( url );
        while ( !response.jsonPath().getList( "completed" ).contains( true )) {
            Thread.sleep( 100 );
            response = isCompleted( url );
        }

        time = System.currentTimeMillis() - time;

        if ( response.statusCode() == 200 ) {
            Locust.getInstance().recordSuccess( "http", getName(), time, response.body().asByteArray().length);
            return;
        }

        Locust.getInstance().recordFailure( "http", getName(), time, response.body().print());

    }

    private Response isCompleted(String url) {

        return  Dhis2RestAssured.getRestAssured()
            .given()
            .contentType( ContentType.JSON )
            .get(url)
            .thenReturn();
    }
}
