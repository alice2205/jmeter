 package ru.antara.main_test.fragments;

 import org.apache.jmeter.protocol.http.util.HTTPConstants;
 import ru.antara.main_test.postprocessors.UC_1_7_ResponseCheck;
 import ru.antara.common.interfaces.SimpleController;
 import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

 import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


 public class FiltrationFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("sortx", "created")
                        .rawParam("date_from", "")
                        .rawParam("date_to", "")
                        .rawParam("status", "3")
                        .rawParam("q", "")
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR"),
                                regexExtractor("token", "name='query_encoded' value=\\'(.*)\\'\\/")
                                        .defaultValue("token_ERR")
                        ),

                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("draw", "1")
                        .rawParam("length", "25")
                        .rawParam("start", "0")
                        .children(
                                jsonExtractor("filtration_check", "data[?status == 'Resolved']")
                                        .defaultValue("filtration_check_error"),
                                jsr223PostProcessor(UC_1_7_ResponseCheck.class)
                        ),

                httpSampler(">_/save_query/", "/save_query/")
                                .method(HTTPConstants.POST)
                                .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                                .rawParam("query_encoded", "${token}")
                                .rawParam("title", "filterTest"),

                httpSampler("<_/tickets/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("draw", "1")
                        .rawParam("length", "25")
                        .rawParam("start", "0")
        );
    }

 }



