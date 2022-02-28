package dk.hoejbjerg.loanmanagement.domain;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class FeeType implements Serializable {
    private static final long serialVersionUID = -1L;
    private @Id String id;
    private String  description;
    private int frequency;
    private double amount;
}