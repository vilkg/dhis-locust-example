package org.hisp.dhis;

import io.restassured.RestAssured;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class Dhis2RestAssured
{
    private static ThreadLocal<RestAssured> restAssuredThreadLocal = new ThreadLocal<RestAssured>();

    public static RestAssured getRestAssured() {
        return Dhis2RestAssured.restAssuredThreadLocal.get();
    }

}
