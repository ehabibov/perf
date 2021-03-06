package com.ehabibov;

import com.ehabibov.queries.GetRequestQueryProvider;
import com.ehabibov.queries.ResponseHeadersQueryProvider;
import com.ehabibov.queries.XmlQueryProvider;
import com.ehabibov.util.JaggerPropertiesProvider;
import com.ehabibov.validators.*;
import com.griddynamics.jagger.engine.e1.collector.JHttpResponseStatusValidatorProvider;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.limits.JLimit;
import com.griddynamics.jagger.user.test.configurations.limits.JLimitVsRefValue;
import com.griddynamics.jagger.user.test.configurations.limits.auxiliary.JMetricName;
import com.griddynamics.jagger.user.test.configurations.limits.auxiliary.RefValue;
import com.griddynamics.jagger.user.test.configurations.load.*;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.*;
import com.griddynamics.jagger.user.test.configurations.termination.*;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class JLoadTestProvider extends JaggerPropertiesProvider {

    @Bean(name = "loadTest1")
    public ArrayList<JLoadTest> loadTest1(){
        String host = getTestPropertyValue("host");
        String endpoint = getTestPropertyValue("test.1.load.scenario.endpoint");
        int users = Integer.valueOf(getTestPropertyValue("test.1.load.scenario.virtual.users"));
        int iterations = Integer.valueOf(getTestPropertyValue("test.1.load.scenario.termination.iterations"));
        long maxDuration = Long.valueOf(getTestPropertyValue("test.1.load.scenario.termination.max.duration.seconds"));
        String string = getTestPropertyValue("test.1.load.scenario.response.validation.string");

        JTestDefinition definition = JTestDefinition.builder(Id.of("td1"), new EndpointsProvider(host))
                .withQueryProvider(new GetRequestQueryProvider(endpoint))
                .addValidator(JHttpResponseStatusValidatorProvider.of("2.."))
                .addValidator(new NotNullResponseValidatorProvider())
                .addValidator(ContentResponseValidatorProvider.of(string))
                .build();

        JLimit successRateLimit = JLimitVsRefValue.builder(JMetricName.PERF_SUCCESS_RATE_OK, RefValue.of(1D))
                .build();
        JLimit failRateLimit = JLimitVsRefValue.builder(JMetricName.PERF_SUCCESS_RATE_FAILS, RefValue.of(0D))
                .build();
        JLimit responseNotNull = JLimitVsRefValue.builder(NotNullResponseValidatorProvider.getName(), RefValue.of(1D))
                .build();
        JLimit responseContent = JLimitVsRefValue.builder(ContentResponseValidatorProvider.getName(), RefValue.of(1D))
                .build();
        List<JLimit> limits = Arrays.asList(successRateLimit, failRateLimit, responseNotNull, responseContent);

        JLoadProfile loadProfile = JLoadProfileInvocation.builder(InvocationCount.of(iterations), ThreadCount.of(users))
                .build();

        JTerminationCriteria termination = JTerminationCriteriaIterations.of(
                IterationsNumber.of(iterations), MaxDurationInSeconds.of(maxDuration));

        return new ArrayList<JLoadTest>(){{
            add(JLoadTest.builder(Id.of("load_test_1"), definition, loadProfile, termination)
                    .withLimits(limits)
                    .build());
        }};
    }

    @Bean(name = "loadTest2")
    public ArrayList<JLoadTest> loadTest2(){
        String host = getTestPropertyValue("host");
        String endpoint = getTestPropertyValue("test.2.load.scenario.endpoint");
        int users = Integer.valueOf(getTestPropertyValue("test.2.load.scenario.users.group.virtual.users.per.load.profile"));
        int invocationsDelay = Integer.valueOf(getTestPropertyValue("test.2.load.scenario.invocations.delay.millisecs"));
        int startDelta = Integer.valueOf(getTestPropertyValue("test.2.load.scenario.users.group.start.delay.delta.seconds"));
        long maxDuration = Long.valueOf(getTestPropertyValue("test.2.load.scenario.termination.max.duration.seconds"));
        String string = getTestPropertyValue("test.2.load.scenario.response.validation.string");

        JTestDefinition definition = JTestDefinition.builder(Id.of("td2"), new EndpointsProvider(host))
                .withQueryProvider(new XmlQueryProvider(endpoint))
                .addValidator(JHttpResponseStatusValidatorProvider.of("2.."))
                .addValidator(new NotNullResponseValidatorProvider())
                .addValidator(ContentResponseValidatorProvider.of(string))
                .build();

        JLimit successRateLimit = JLimitVsRefValue.builder(JMetricName.PERF_SUCCESS_RATE_OK, RefValue.of(1D))
                .build();
        JLimit failRateLimit = JLimitVsRefValue.builder(JMetricName.PERF_SUCCESS_RATE_FAILS, RefValue.of(0D))
                .build();
        JLimit responseNotNull = JLimitVsRefValue.builder(NotNullResponseValidatorProvider.getName(), RefValue.of(1D))
                .build();
        JLimit responseContent = JLimitVsRefValue.builder(ContentResponseValidatorProvider.getName(), RefValue.of(1D))
                .build();
        List<JLimit> limits = Arrays.asList(successRateLimit, failRateLimit, responseNotNull, responseContent);

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
            add(JLoadTest.builder(Id.of("load_test_2"), definition, loadProfile, termination)
                    .withLimits(limits)
                    .build());
        }};
    }

    @Bean(name = "loadTest3")
    public ArrayList<JLoadTest> loadTest3(){
        String host = getTestPropertyValue("host");
        String endpoint = getTestPropertyValue("test.3.load.scenario.endpoint");
        long userGroupOneVirtualUsers = Long.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.1.virtual.users"));
        int userGroupOneDelay = Integer.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.1.invocations.delay.millisecs"));
        long maxDuration = Long.valueOf(getTestPropertyValue("test.3.load.scenario.termination.max.duration.seconds"));

        long userGroupTwoVirtualUsers = Long.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.2.virtual.users"));
        int userGroupTwoDelay = Integer.valueOf(getTestPropertyValue("test.3.load.scenario.users.group.2.invocations.delay.millisecs"));

        String datasource = getTestPropertyValue("test.3.load.scenario.datasource.path");

        JTestDefinition definition = JTestDefinition.builder(Id.of("td3"), new EndpointsProvider(host))
                .withQueryProvider(new ResponseHeadersQueryProvider(endpoint, datasource))
                .addValidator(JHttpResponseStatusValidatorProvider.of("2.."))
                .addValidator(new NotNullResponseValidatorProvider())
                .addValidator(HttpQueryValidatorProvider.of(datasource))
                .build();

        JLimit successRateLimit = JLimitVsRefValue.builder(JMetricName.PERF_SUCCESS_RATE_OK, RefValue.of(1D))
                .build();
        JLimit failRateLimit = JLimitVsRefValue.builder(JMetricName.PERF_SUCCESS_RATE_FAILS, RefValue.of(0D))
                .build();
        JLimit responseNotNull = JLimitVsRefValue.builder(NotNullResponseValidatorProvider.getName(), RefValue.of(1D))
                .build();
        JLimit responseContent = JLimitVsRefValue.builder(ContentResponseValidatorProvider.getName(), RefValue.of(1D))
                .build();
        List<JLimit> limits = Arrays.asList(successRateLimit, failRateLimit, responseNotNull, responseContent);

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
                add(JLoadTest.builder(Id.of("load_test_3_1"), definition, loadProfile1, termination1)
                        .withLimits(limits)
                        .build());
                add(JLoadTest.builder(Id.of("load_test_3_2"), definition, loadProfile2, termination2)
                        .withLimits(limits)
                        .build());
        }};
    }
}