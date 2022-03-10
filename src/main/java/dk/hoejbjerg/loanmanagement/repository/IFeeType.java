package dk.hoejbjerg.loanmanagement.repository;

import dk.hoejbjerg.loanmanagement.domain.FeeType;
import dk.hoejbjerg.loanmanagement.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface IFeeType {
    /**
     * Identifcation of product collection
     */
    String collection = "feeType";

    /**
     * initialize - loads example data from file if no collection exists
     */
    void initialize();

    /**
     *  Get FeeType identified by id
     *
     * @param id
     *        Identification of FeeType to retrieve
     *
     * @return FeeType
     */
    FeeType getFeeType (String id);
}