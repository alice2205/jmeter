package ru.antara.main_test.postprocessors;
import us.abstracta.jmeter.javadsl.core.preprocessors.DslJsr223PreProcessor;
import java.io.FileWriter;
import java.util.UUID;

import static us.abstracta.jmeter.javadsl.JmeterDsl.jsr223PreProcessor;

public class CredentialsGenerator {

    public static DslJsr223PreProcessor createCredentialsPreProcessor() {
        return jsr223PreProcessor(s -> {
            String username = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String password = UUID.randomUUID().toString().substring(0, 12);

            FileWriter writer = new FileWriter("exercise_5.csv", true);
            writer.append(username);
            writer.append(",");
            writer.append(password);
            writer.append("\n");
            writer.flush();
            writer.close();

            s.vars.put("username", username);
            s.vars.put("userpassword", password);
        });
    }
}