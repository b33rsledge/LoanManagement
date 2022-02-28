package dk.hoejbjerg.loanmanagement.repository;

import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import dk.hoejbjerg.loanmanagement.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public interface ProductInteface {
    /**
     * Identifcation of product collection
     */
    String collection = "product";

    /**
     * initialize - loads example data from file if no collection exists
     */
    void initialize();

    /**
     * Update properties of a product, which is equivalent to loan updating loan terms
     *
     * @param id
     *        Identification of product to update
     *
     * @param pToUpdate
     *        Product to update
     *
     * @return Updated product
     */
    Product updateProduct(String id, Product pToUpdate);

    /**
     *  Get product identified by id
     *
     * @param id
     *        Identification of product to retrieve
     *
     * @return Product
     */
    Product getProduct (String id);
}