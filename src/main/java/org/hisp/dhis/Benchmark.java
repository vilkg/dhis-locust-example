package org.hisp.dhis;

import com.github.myzhan.locust4j.Locust;
import com.github.myzhan.locust4j.ratelimit.StableRateLimiter;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class Benchmark
{
    public static Benchmark newInstance() {
        return new Benchmark();
    }

    public Locust init() {

        Locust locust = Locust.getInstance();
        locust.setMasterHost("127.0.0.1");
        locust.setMasterPort(5557);
        // print out locust4j's internal logs.
        locust.setVerbose(true);

        return locust;

    }


}
