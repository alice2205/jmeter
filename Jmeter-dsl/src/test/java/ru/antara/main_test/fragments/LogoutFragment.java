package ru.antara.main_test.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.UC_7_8_10_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class LogoutFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/logout/", "/logout/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("logout_check", "(login)")
                                        .defaultValue("logout_check_error"),
                                jsr223PostProcessor(UC_7_8_10_ResponseCheck.class)
                        ),

                httpSampler("<_/", "/")
                        .method(HTTPConstants.GET)
                );
    }
}
