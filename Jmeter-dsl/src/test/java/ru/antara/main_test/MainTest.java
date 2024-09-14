package ru.antara.main_test;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.antara.main_test.fragments.*;
import ru.antara.main_test.helpers.AdminLoginPropertyHelper;
import ru.antara.main_test.samplers.MainThreadGroup;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;


import static ru.antara.common.helpers.ActionHelper.testAction;
import static ru.antara.common.helpers.CacheHelper.getCacheManager;
import static ru.antara.common.helpers.CookiesHelper.getCookiesClean;
import static ru.antara.common.helpers.HeadersHelper.getHeaders;
import static ru.antara.common.helpers.HttpHelper.getHttpDefaults;
import static ru.antara.common.helpers.LogHelper.getTestResultString;
import static ru.antara.common.helpers.LogHelper.influxDbLog;
import static ru.antara.common.helpers.VisualizersHelper.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class MainTest {
    boolean debugEnable;
    boolean errorLogEnable;
    boolean influxDbLogEnable;
    boolean resultTreeEnable;
    boolean resultDashboardEnable;
    boolean debugPostProcessorEnable;
    double throughputPerMinute;

    static final Logger logger = LogManager.getLogger(MainTest.class);
    EmbeddedJmeterEngine embeddedJmeterEngine = new EmbeddedJmeterEngine();
    Properties properties = new Properties();

    @BeforeTest
    private void init() throws IOException {
        properties = AdminLoginPropertyHelper.readAdminLoginProperties();
        AdminLoginPropertyHelper.setPropertiesToEngine(embeddedJmeterEngine, properties);

        debugEnable = Boolean.parseBoolean(properties.getProperty("DEBUG_ENABLE"));
        errorLogEnable = Boolean.parseBoolean(properties.getProperty("ERROR_LOG_ENABLE"));
        influxDbLogEnable = Boolean.parseBoolean(properties.getProperty("INFLUX_DB_LOG_ENABLE"));
        resultTreeEnable = Boolean.parseBoolean(properties.getProperty("RESULT_TREE_ENABLE"));
        resultDashboardEnable = Boolean.parseBoolean(properties.getProperty("RESULT_DASHBOARD_ENABLE"));
        debugPostProcessorEnable = Boolean.parseBoolean(properties.getProperty("DEBUG_POSTPROCESSOR_ENABLE"));
        throughputPerMinute = Double.parseDouble(properties.getProperty("THROUGHPUT"));
    }

    @SuppressWarnings("unused")
    @Test
    private void test() throws IOException, InterruptedException, TimeoutException {
        AdminLoginFragment adminLoginFragment = new AdminLoginFragment();
        LogoutFragment logoutFragment = new LogoutFragment();
        CreateUserFragment createUserFragment = new CreateUserFragment();
        CreateTicketFragment createTicketFragment = new CreateTicketFragment();
        UserLoginFragment userLoginFragment = new UserLoginFragment();
        PaginationFragment paginationFragment = new PaginationFragment();
        FiltrationFragment filtrationFragment = new FiltrationFragment();
        OpenTicketFragment openTicketFragment = new OpenTicketFragment();
        ChangeTaskStatusFragment changeTaskStatusFragment = new ChangeTaskStatusFragment();
        DeleteTicketFragment deleteTicketFragment = new DeleteTicketFragment();


        TestPlanStats run = testPlan(
                getCookiesClean(),
                getCacheManager(),
                getHeaders(),
                getHttpDefaults(),
                MainThreadGroup.getThreadGroup("TG_MAIN", debugEnable)
                        .children(
                                ifController(s -> !debugEnable,
                                        testAction(throughputTimer(throughputPerMinute).perThread())
                                ),
                                transaction("UC 1_ADMIN_LOGIN",
                                        adminLoginFragment.get()
                                ),
                                transaction("UC 2_CREATE_USER",
                                        adminLoginFragment.get(),
                                        createUserFragment.get()
                                ),
                                transaction("UC 3_CREATE_TICKET",
                                        userLoginFragment.get(),
                                        createTicketFragment.get()
                                ),
                                transaction("UC 4_PAGINATION",
                                        userLoginFragment.get(),
                                        paginationFragment.get()
                                ),
                                transaction("UC 5_FILTRATION",
                                        userLoginFragment.get(),
                                        filtrationFragment.get()
                                ),
                                transaction("UC 6_OPEN TICKET",
                                        userLoginFragment.get(),
                                        openTicketFragment.get()
                                ),
                                transaction("UC 7_CHANGE TASK STATUS",
                                        userLoginFragment.get(),
                                        changeTaskStatusFragment.get()
                                ),
                                transaction("UC 8_DELETE TICKET",
                                        userLoginFragment.get(),
                                        deleteTicketFragment.get()
                                ),
                                transaction("UC 10_LOGOUT",
                                        userLoginFragment.get(),
                                        logoutFragment.get()
                                ),
                                transaction("UC 11_COMPLEX",
                                        userLoginFragment.get(),
                                        paginationFragment.get(),
                                        createTicketFragment.get(),
                                        deleteTicketFragment.get(),
                                        logoutFragment.get()
                                )
                        ),


                influxDbLog(influxDbLogEnable),
                resultTree(resultTreeEnable),
                resultDashboard(resultDashboardEnable),
                debugPostPro(debugPostProcessorEnable)

        ).runIn(embeddedJmeterEngine);

        logger.info(getTestResultString(run));

    }

}
