package ru.antara.main_test.fragments;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.main_test.postprocessors.CredentialsGenerator;
import ru.antara.main_test.postprocessors.UC_1_7_ResponseCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class CreateUserFragment implements SimpleController {

    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/system_settings/", "/system_settings/")
                        .method(HTTPConstants.GET)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}"),

                httpSampler("<_/admin/auth/user/", "/admin/auth/user/")
                        .method(HTTPConstants.GET),

                httpSampler("<_/admin/auth/user/add/", "/admin/auth/user/add/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                        .defaultValue("csrf_ERR")),

                httpSampler(">_/admin/auth/user/add/", "/admin/auth/user/add/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("username", "${username}")
                        .rawParam("password1", "${userpassword}")
                        .rawParam("password2", "${userpassword}")
                        .rawParam("_save", "Save")
                        .children(
                                CredentialsGenerator.createCredentialsPreProcessor(),
                                regexExtractor("userID", "\\/(\\d+)\\/")
                                        .defaultValue("userID_ERR"),
                                jsr223PostProcessor(UC_1_7_ResponseCheck.class)
                        ),

                httpSampler("<_/admin/auth/user/${userID}/change/", "/admin/auth/user/${userID}/change/")
                        .method(HTTPConstants.GET)
                        .rawParam("username", "${username}"),

                httpSampler(">_/admin/auth/user/${userID}/change/", "/admin/auth/user/${userID}/change/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("is_active", "on")
                        .rawParam("is_staff", "on")
                        .rawParam("username", "${username}")
                        .rawParam("date_joined_0", "2024-08-11")
                        .rawParam("date_joined_1", "18:06:40")
                        .rawParam("initial-date_joined_0", "2024-08-11")
                        .rawParam("initial-date_joined_1", "18:06:40")
                        .rawParam("_save", "Save"),

                httpSampler("<_/admin/auth/user/", "/admin/auth/user/")
                        .method(HTTPConstants.GET)
        );
    }
}



