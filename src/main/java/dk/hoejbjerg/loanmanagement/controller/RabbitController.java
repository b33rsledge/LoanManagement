package dk.hoejbjerg.loanmanagement.controller;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import org.springframework.stereotype.Service;


@Service
public class RabbitController {

    private RabbitTemplate rabbitTemplate;
    private Queue queue;

    public RabbitController(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void publishLoanFacility(LoanFacility loan) {
        rabbitTemplate.convertAndSend(this.queue.getName(), loan);
    }
}
