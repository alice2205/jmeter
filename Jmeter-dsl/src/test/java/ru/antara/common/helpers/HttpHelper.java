package ru.antara.common.helpers;

import us.abstracta.jmeter.javadsl.http.DslHttpDefaults;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static us.abstracta.jmeter.javadsl.JmeterDsl.httpDefaults;

public class HttpHelper {

    public static DslHttpDefaults getHttpDefaults() {

        return httpDefaults()
                .protocol("${__P(PROTOCOL)}")
                .host("${__P(HELPDESK_HOST)}")
                .port(23232)

                .encoding(StandardCharsets.UTF_8)
                .connectionTimeout(Duration.ofSeconds(3))
                .responseTimeout(Duration.ofSeconds(10));
    }

}
