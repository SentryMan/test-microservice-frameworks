//package computerdatabase; // 1

// 2

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BasicSimulationJava extends Simulation { // 3
    Integer nbUsers = Integer.getInteger("users", 1000);
    Long myRepeat = Long.getLong("repeat", 2);
    String baseUrl = "http://localhost:8080";
    HttpProtocolBuilder httpProtocol = http // 4
            .baseUrl(baseUrl) // 5
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

    ChainBuilder search = repeat(myRepeat.intValue()).on(
            exec(
                    http("GetApplicationInfo")
                            .get("/hello")
                            .check(status().is(200))
                            .check(jsonPath("$.name"))
            )
    );
    ScenarioBuilder scn = scenario("hello").exec(search);

    {
        setUp(
                scn.injectOpen(
                        rampUsers(nbUsers).during(Duration.ofSeconds(5))
                ).protocols(httpProtocol)
        );
    }
}