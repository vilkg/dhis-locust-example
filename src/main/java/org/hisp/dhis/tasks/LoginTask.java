package org.hisp.dhis.tasks;

import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import com.github.myzhan.locust4j.taskset.AbstractTaskSet;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hisp.dhis.Dhis2RestAssured;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.preemptive;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class LoginTask extends AbstractTask
{
    public int getWeight()
    {
        return 1;
    }

    public String getName()
    {
        return "Login task";
    }

    public void execute()
        throws Exception
    {
        Dhis2RestAssured.getRestAssured().authentication = preemptive().basic( "admin", "district" );
        Response apiResponse =
            Dhis2RestAssured.getRestAssured().given()
                .contentType( ContentType.TEXT )
                .when()
                .get( "api//me" )
                .thenReturn();

        if ( apiResponse.statusCode() == 200 ) {
            Locust.getInstance().recordSuccess( "http", getName(), apiResponse.getTime(), 0);
            return;
        }
        Locust.getInstance().recordFailure( "http", getName(), apiResponse.getTime(), apiResponse.body().print() );
    }
}
