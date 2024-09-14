package ru.antara.main_test.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.UC_1_7_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static ru.antara.common.helpers.ActionHelper.jsr223Action;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;

public class CreateTicketFragment implements SimpleController {
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/submit/","/tickets/submit/")
                        .method(HTTPConstants.GET)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")),

                httpSampler(">_/tickets/submit/", "/tickets/submit/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("queue", "1")
                        .rawParam("title", "problem")
                        .rawParam("body", "description")
                        .rawParam("priority", "3")
                        .rawParam("submitter_email", "test@test.com")
                        .rawParam("assigned_to", "${username}")
                        .children(
                                regexExtractor("ticketID", "DH-(\\d+)\\]")
                                        .defaultValue("ticketID_ERR"),
                                jsr223PostProcessor(UC_1_7_ResponseCheck.class)
                        ),

                httpSampler("<_/tickets/${ticketID}/", "/tickets/${ticketID}/")
                        .method(HTTPConstants.GET)
        );
    }
}
