package dk.hoejbjerg.loanmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product implements  Serializable{
    private static final long serialVersionUID = -1L;
    private @Id String id;
    private String  description;
    private double interestRate;
    private int feeType;
    private int minimumTerm;
    private int maximumTerm;
}