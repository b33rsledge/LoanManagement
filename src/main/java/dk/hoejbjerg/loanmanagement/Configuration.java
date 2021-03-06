package dk.hoejbjerg.loanmanagement;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Objects;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    @Autowired
    Environment environment;

    /*
        MongoDB Configuration
     */
    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString("mongodb://" +
                environment.getProperty("spring.data.mongodb.username") +
                ":" +
                environment.getProperty("spring.data.mongodb.password") +
                "@" +
                environment.getProperty("spring.data.mongodb.host") +
                ":" +
                environment.getProperty("spring.data.mongodb.port") +
                "/" +
                environment.getProperty("spring.data.mongodb.database") +
                "?authSource=" +
                environment.getProperty("spring.data.mongodb.authentication-database"));

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), Objects.requireNonNull(environment.getProperty("spring.data.mongodb.database")));
    }

    @Value("${spring.data.messaging.receive-queue}")
    String queueName;

    @Value("${spring.data.messaging.exchange}")
    String exchange;

    @Value("${spring.data.messaging.routing-key}")
    private String routingkey;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}