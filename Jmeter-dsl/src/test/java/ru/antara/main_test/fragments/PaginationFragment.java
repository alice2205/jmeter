 package ru.antara.main_test.fragments;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.UC_1_7_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;


 public class PaginationFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("draw", "2")
                        .rawParam("length", "10")
                        .children(
                                jsonExtractor("pagination_check", "length(data)\n")
                                        .defaultValue("pagination_check_error"),
                                jsr223PostProcessor(UC_1_7_ResponseCheck.class)
                        ),
                httpSampler("<_/datatables_ticket_list/token", "/datatables_ticket_list/${token}")
                        .method(HTTPConstants.GET)
                        .rawParam("draw", "3")
                        .rawParam("length", "10")
                        .rawParam("start", "10")
        );
    }
}



