package ru.antara.main_test.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.UC_7_8_10_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class DeleteTicketFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("token", "name='query_encoded' value=\\'(.*)\\'\\/")
                                        .defaultValue("token_ERR")
                        ),
                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("draw", "1")
                        .rawParam("length", "25")
                        .children(
                                jsonExtractor("taskID", "data[*].id")
                                        .defaultValue("taskID_ERR")
                        ),

                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("sortx", "created")
                        .rawParam("status", "5")
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("draw", "1")
                        .rawParam("length", "25"),
                httpSampler(">_/tickets/update/", "/tickets/update/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("ticketTable_length", "25")
                        .rawParam("ticket_id", "${taskID}")
                        .rawParam("action", "delete"),
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .children(
                                jsonExtractor("deleted_ticket", "data[?id == `${taskID}`].id | [0]")
                                        .defaultValue("deleted_ticket_check_error"),
                                jsr223PostProcessor(UC_7_8_10_ResponseCheck.class)
                        )

        );
    }
}
