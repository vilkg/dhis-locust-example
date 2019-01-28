package org.hisp.dhis.tasks;

import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.hisp.dhis.Dhis2RestAssured;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class EventExportTask
    extends AbstractTask
{
    private JsonObject responseBody;

    public int getWeight()
    {
        return 1;
    }

    public String getName()
    {
        return "Export all metadata";
    }

    public void execute()
        throws Exception
    {
        Response response = Dhis2RestAssured.getRestAssured()
            .given()
            .get( "api/metadata" )
            .thenReturn();

        this.responseBody = response.body().as( JsonObject.class );

        if ( response.statusCode() != 200 )
        {
            Locust.getInstance().recordFailure( "http", getName(), response.getTime(), response.body().prettyPrint() );
            return;
        }

        Locust.getInstance().recordSuccess( "http", getName(), response.getTime(), response.body().asByteArray().length );

    }

    public JsonObject executeAndGetBody()
        throws Exception
    {
        this.execute();

        return responseBody;
    }
}
