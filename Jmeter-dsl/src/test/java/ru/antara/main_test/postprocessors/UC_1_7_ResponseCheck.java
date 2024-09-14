package ru.antara.main_test.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor.PostProcessorScript;
import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor.PostProcessorVars;

import java.util.Objects;


public class UC_1_7_ResponseCheck implements PostProcessorScript {

    @Override
    public void runScript(PostProcessorVars s) {
        String sampleLabel = s.prev.getSampleLabel();

        //UC 1
        if (sampleLabel.contains("/login")) {
            String loginCheck = s.vars.get("login_check");
            if (loginCheck != null) {
                if (!Objects.equals(loginCheck, "Logout") || Objects.equals(loginCheck, "login_check_error")) {
                    s.prev.setSuccessful(false);
                    s.prev.setSampleLabel(">_/login/_FAILED");
                }
            }
        }

        //UC 2
        if (sampleLabel.contains("/admin/auth/user/add")) {
            String userIDCheck = s.vars.get("userID");
            if (Objects.equals(userIDCheck, "null") || Objects.equals(userIDCheck, "userID_ERR")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel(">_/admin/auth/user/add/_FAILED");
            }
        }

        //UC 3
        if (sampleLabel.contains("/tickets/submit/")) {
            String ticketIDCheck = s.vars.get("ticketID");
            if (Objects.equals(ticketIDCheck, "null") || Objects.equals(ticketIDCheck, "ticketID_ERR")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel(">_/tickets/submit/_FAILED");
            }
        }

        //UC 4
        if (sampleLabel.contains("/datatables_ticket_list/")) {
            String paginationCheck = s.vars.get("pagination_check");
            if (!Objects.equals(paginationCheck, "10") || Objects.equals(paginationCheck, "pagination_check_error")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel("<_/datatables_ticket_list/${token}_FAILED");
            }
        }

        //UC 5
        if (sampleLabel.contains("/datatables_ticket_list/")) {
            String filtrationCheck = s.vars.get("filtration_check");
            if (Objects.equals(filtrationCheck, "filtration_check_error")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel("<_/datatables_ticket_list/${token}_FAILED");
            }
        }

        //UC 6
        if (sampleLabel.contains("/tickets/")) {
            String openTicketCheck = s.vars.get("open_ticket_check");
            if (Objects.equals(openTicketCheck, "null") || Objects.equals(openTicketCheck, "open_ticket_check_error")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel("<_/tickets/_FAILED");
            }
        }
    }
}
