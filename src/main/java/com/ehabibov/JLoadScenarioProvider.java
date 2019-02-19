package com.ehabibov;

import com.ehabibov.util.JaggerPropertiesProvider;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;

@Configuration
@ComponentScan(basePackages = {"com.ehabibov"})
@Import(JLoadTestProvider.class)
public class JLoadScenarioProvider extends JaggerPropertiesProvider {

    @Bean
    public JLoadScenario mainScenario(ArrayList<JLoadTest> loadTest1,
                                      ArrayList<JLoadTest> loadTest2,
                                      ArrayList<JLoadTest> loadTest3){

        JParallelTestsGroup testGroup1 = JParallelTestsGroup.builder(Id.of("tg_get"), loadTest1)
                .build();
        JParallelTestsGroup testGroup2 = JParallelTestsGroup.builder(Id.of("tg_xml"), loadTest2)
                .build();
        JParallelTestsGroup testGroup3 = JParallelTestsGroup.builder(Id.of("tg_headers"), loadTest3)
                .build();
        return JLoadScenario.builder(Id.of("mainScenario"), testGroup1, testGroup2, testGroup3)
                .build();
    }
}