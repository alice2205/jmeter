package ru.antara.main_test.fragments;

import ru.antara.main_test.postprocessors.UC_1_7_ResponseCheck;
import us.abstracta.jmeter.javadsl.core.configs.DslCsvDataSet;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class UserLoginFragment implements SimpleController {

    public DslSimpleController get() {
        DslCsvDataSet csvDataSet = csvDataSet("exercise_5.csv")
                .delimiter(",")
                .variableNames("username,userpassword");

        return simpleController(
                csvDataSet,
                httpSampler("<_/", "/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler(">_/login/", "/login/")
                        .method(HTTPConstants.POST)
                        .rawParam("username", "${username}")
                        .rawParam("password", "${userpassword}")
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("next", "/")
                        .children(
                                regexExtractor("login_check", "(Logout)")
                                        .defaultValue("login_check_error"),
                                        jsr223PostProcessor(UC_1_7_ResponseCheck.class)
                        ),
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
                        )
        );
    }
}
