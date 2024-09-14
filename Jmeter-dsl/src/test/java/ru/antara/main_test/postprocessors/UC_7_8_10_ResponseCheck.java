package ru.antara.main_test.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor.PostProcessorScript;
import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor.PostProcessorVars;

import java.util.Objects;


public class UC_7_8_10_ResponseCheck implements PostProcessorScript {

    @Override
    public void runScript(PostProcessorVars s) {
        String sampleLabel = s.prev.getSampleLabel();

        //UC 7
        if (sampleLabel.contains("/tickets/")) {
            String openTicketCheck = s.vars.get("change_task_status_check");
            if (!Objects.equals(openTicketCheck, "Resolved") || Objects.equals(openTicketCheck, "change_task_status_check_error")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel("<_/tickets/_FAILED");
            }
        }

        //UC 8
        if (sampleLabel.contains("/datatables_ticket_list/")) {
            String deletedTicketCheck = s.vars.get("deleted_ticket");
            if (Objects.equals(deletedTicketCheck, "data[?id == `${taskID}`].id | [0]") || !Objects.equals(deletedTicketCheck, "deleted_ticket_check_error")) {
                s.prev.setSuccessful(false);
                System.out.println(deletedTicketCheck);
                s.prev.setSampleLabel("<_/datatables_ticket_list/_FAILED");
            }
        }

        //UC 10
        if (sampleLabel.contains("/logout/")) {
            String logoutCheck = s.vars.get("logout_check");
            if (Objects.equals(logoutCheck, "Logout") || Objects.equals(logoutCheck, "logout_check_error")) {
                s.prev.setSuccessful(false);
                s.prev.setSampleLabel("<_/logout/_FAILED");
            }
        }
    }
}
