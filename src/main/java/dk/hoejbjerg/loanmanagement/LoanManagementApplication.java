package dk.hoejbjerg.loanmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Objects;

@EnableRabbit
@SpringBootApplication
public class LoanManagementApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoanManagementApplication.class);

    public static void main(String[] args) {

        /*
         * Run the application
         */
        SpringApplication springApplication = new SpringApplication(LoanManagementApplication.class);
        ApplicationContext ctx = springApplication.run(args);

        /*
         * Document loaded beans for a newbie :) - the list is only sent to log if debug: true in application properties
         */
        if (Objects.equals(ctx.getEnvironment().getProperty("debug"), "true")) {
            logger.info("********************* BEAN LIST *********************");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger.info("method=main, Bean: " + beanName);
            }
        }

    }

}
