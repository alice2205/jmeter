package ru.antara.main_test.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.UC_7_8_10_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class ChangeTaskStatusFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("token", "name='query_encoded' value=\\'(.*)\\'\\/")
                                        .defaultValue("token_ERR"),
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("draw", "1")
                        .rawParam("length", "25")
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .children(
                                jsonExtractor("change_status", "data[?status == 'Open'] | [0].id")
                                        .defaultValue("change_status_ERR")
                        ),
                httpSampler("<_/tickets/${change_status}/", "/tickets/${change_status}/")
                        .method(HTTPConstants.GET),
                httpSampler(">_/tickets/${change_status}/update/", "/tickets/${change_status}/update/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("comment", "NewComment")
                        .rawParam("new_status", "3")
                        .rawParam("public", "1")
                        .rawParam("title", "apqfvpryvbyvkkyjeyzs")
                        .rawParam("owner", "59")
                        .rawParam("priority", "2")
                        .rawParam("due_date", "")
                        .rawParam("owner", "59"),
                httpSampler("<_/tickets/${change_status}/", "/tickets/${change_status}/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("change_task_status_check", "(Resolved)")
                                        .defaultValue("change_task_status_check_error"),
                                jsr223PostProcessor(UC_7_8_10_ResponseCheck.class)
                        )
        );
    }
}
