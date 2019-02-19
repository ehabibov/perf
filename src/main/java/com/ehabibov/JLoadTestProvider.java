package com.ehabibov;

import com.ehabibov.listeners.HttpResponseBodySizeInvocationListener;
import com.ehabibov.queries.GetRequestQueryProvider;
import com.ehabibov.queries.ResponseHeadersQueryProvider;
import com.ehabibov.queries.XmlQueryProvider;
import com.ehabibov.util.JaggerPropertiesProvider;
import com.ehabibov.validators.Content;
import com.ehabibov.validators.HttpQueryValidatorProvider;
import com.ehabibov.validators.HttpResponseContentBodyValidatorProvider;
import com.ehabibov.validators.HttpResponseContentTypeHeaderValidatorProvider;
import com.griddynamics.jagger.engine.e1.collector.JHttpResponseStatusValidatorProvider;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileInvocation;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.InvocationCount;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.ThreadCount;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaBackground;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaDuration;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import java.util.ArrayList;

@Configuration
public class JLoadTestProvider extends JaggerPropertiesProvider {


    @Bean(name = "loadTest1")
    public ArrayList<JLoadTest> loadTest1(){
        int users = Integer.valueOf(getTestPropertyValue("test.1.load.scenario.virtual.users"));
        int iterations = Integer.valueOf(getTestPropertyValue("test.1.load.scenario.termination.iterations"));
        long maxDuration = Long.valueOf(getTestPropertyValue("test.1.load.scenario.termination.max.duration.seconds"));

        JTestDefinition definition = JTestDefinition.builder(Id.of("td1"), new EndpointsProvider())
                .withQueryProvider(new GetRequestQueryProvider())
                .addValidator(JHttpResponseStatusValidatorProvider.of("2.."))
                .addValidator(HttpResponseContentTypeHeaderValidatorProvider.of(MediaType.APPLICATION_JSON))
                .addValidator(HttpResponseContentBodyValidatorProvider.of(Content.JSON))
                .addListener(new HttpResponseBodySizeInvocationListener())
                .build();

        JLoadProfile loadProfile = JLoadProfileInvocation.builder(InvocationCount.of(iterations), ThreadCount.of(users))
                .build();

        JTerminationCriteria termination = JTerminationCriteriaIterations.of(
                IterationsNumber.of(iterations), MaxDurationInSeconds.of(maxDuration));

        return new ArrayList<JLoadTest>(){{
            add(JLoadTest.builder(Id.of("load_test_1"), definition, loadProfile, termination).build());
        }};
    }

    @Bean(name = "loadTest2")
    public ArrayList<JLoadTest> loadTest2(){
        int users = Integer.valueOf(getTestPropertyValue("test.2.load.scenario.users.group.virtual.users.per.load.profile"));
        int invocationsDelay = Integer.valueOf(getTestPropertyValue("test.2.load.scenario.invocations.delay.millisecs"));
        int startDelta = Integer.valueOf(getTestPropertyValue("test.2.load.scenario.users.group.start.delay.delta.seconds"));
        long maxDuration = Long.valueOf(getTestPropertyValue("test.2.load.scenario.termination.max.duration.seconds"));

        JTestDefinition definition = JTestDefinition.builder(Id.of("td2"), new EndpointsProvider())
                .withQueryProvider(new XmlQueryProvider())
                .addValidator(JHttpResponseStatusValidatorProvider.of("2.."))
                .addValidator(HttpResponseContentTypeHeaderValidatorProvider.of(MediaType.APPLICATION_XML))
                .addValidator(HttpResponseContentBodyValidatorProvider.of(Content.XML))
                .addListener(new HttpResponseBodySizeInvocationListener())
                .build();

        JLoadProfileUsers userProfile1 = JLoadProfileUsers.builder(NumberOfUsers.of(users))
                .withStartDelayInSeconds(startDelta)
                .build();
        JLoadProfileUsers userProfile2 = JLoadProfileUsers.builder(NumberOfUsers.of(users))
                .withStartDelayInSeconds(startDelta * 2)
                .build();
        JLoadProfileUsers userProfile3 = JLoadProfileUsers.builder(NumberOfUsers.of(users))
                .withStartDelayInSeconds(startDelta * 3)
                .build();

        JLoadProfileUserGroups loadProfile = JLoadProfileUserGroups.builder(userProfile1, userProfile2, userProfile3)
                .withDelayBetweenInvocationsInMilliseconds(invocationsDelay)
                .build();

        JTerminationCriteria termination = JTerminationCriteriaDuration.of(DurationInSeconds.of(maxDuration));

        return new ArrayList<JLoadTest>(){{
            add(JLoadTest.builder(Id.of("load_test_2"), definition, loadProfile, termination).build());
        }};
    }

    @Bean(name = "loadTest3")
    public ArrayList<JLoadTest> loadTest3(){
        long userGroupOneVirtualUsers = Long.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.1.virtual.users"));
        int userGroupOneDelay = Integer.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.1.invocations.delay.millisecs"));
        long maxDuration = Long.valueOf(getTestPropertyValue("test.3.load.scenario.termination.max.duration.seconds"));

        long userGroupTwoVirtualUsers = Long.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.2.virtual.users"));
        int userGroupTwoDelay = Integer.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.2.invocations.delay.millisecs"));

        String datasource = getTestPropertyValue("test.3.load.scenario.datasource.path");

        JTestDefinition definition = JTestDefinition.builder(Id.of("td3"), new EndpointsProvider())
                .withQueryProvider(new ResponseHeadersQueryProvider(datasource))
                .addValidator(JHttpResponseStatusValidatorProvider.of("2.."))
                .addValidator(HttpResponseContentTypeHeaderValidatorProvider.of(MediaType.APPLICATION_JSON))
                .addValidator(HttpResponseContentBodyValidatorProvider.of(Content.JSON))
                .addValidator(HttpQueryValidatorProvider.of(datasource))
                .addListener(new HttpResponseBodySizeInvocationListener())
                .build();

        JLoadProfileUsers groupOne = JLoadProfileUsers.builder(NumberOfUsers.of(userGroupOneVirtualUsers))
                .build();
        JLoadProfileUsers groupTwo = JLoadProfileUsers.builder(NumberOfUsers.of(userGroupTwoVirtualUsers))
                .build();

        JLoadProfileUserGroups loadProfile1 = JLoadProfileUserGroups.builder(groupOne)
                .withDelayBetweenInvocationsInMilliseconds(userGroupOneDelay)
                .build();
        JLoadProfileUserGroups loadProfile2 = JLoadProfileUserGroups.builder(groupTwo)
                .withDelayBetweenInvocationsInMilliseconds(userGroupTwoDelay)
                .build();

        JTerminationCriteria termination1 = JTerminationCriteriaDuration.of(DurationInSeconds.of(maxDuration));
        JTerminationCriteria termination2 = JTerminationCriteriaBackground.getInstance();

        return new ArrayList<JLoadTest>(){{
                add(JLoadTest.builder(Id.of("load_test_3_1"), definition, loadProfile1, termination1).build());
                add(JLoadTest.builder(Id.of("load_test_3_2"), definition, loadProfile2, termination2).build());
        }};
    }
}
