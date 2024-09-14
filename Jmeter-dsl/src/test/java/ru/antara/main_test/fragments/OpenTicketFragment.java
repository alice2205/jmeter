package ru.antara.main_test.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.UC_1_7_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class OpenTicketFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/${taskID}/", "/tickets/${taskID}/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/tickets/${taskID}/", "/tickets/${taskID}/")
                        .method(HTTPConstants.GET)
                        .rawParam("take", "")
                        .children(
                                regexExtractor("open_ticket_check", "Assigned To</th>\\s*<td>(.+)")
                                        .defaultValue("open_ticket_check_error"),
                                jsr223PostProcessor(UC_1_7_ResponseCheck.class)
                        ),
                httpSampler("<_/tickets/${taskID}/", "/tickets/${taskID}/")
                        .method(HTTPConstants.GET)
        );
    }
}
