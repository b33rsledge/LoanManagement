package dk.hoejbjerg.loanmanagement.repository;

import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ILoanFacility {

    /**
     * Identification of collecton for loan facilities
     */
    String collection = "loanFacility";

    /**
     * initialize - loads example data from file if no collection exists
     */
    void initialize();

    /**
     * Find a specific loan from a loan identification
     * @param id
     *        Identification of a specific loan
     * @return LoanFacility
     *         The specific loan is returned
     */
    LoanFacility getLoanFacility(String id);

    /**
     * Get all facilities that are defined as being of a specific product type
     * @param productId
     *        Product ID for which to return all asociated LoanFacilities
     * @return List<LoanFacility>
     *        list of  loan facilities
     */
    List<LoanFacility> getFacilitiesByProduct(String productId);

    /**
     * Get all facilities that are defined as being of a specific product type
     * @param productId
     *        Product ID for which to return all asociated LoanFacilities
     *
     * @param pageable
     *        PageRequest (page, size, sort criteria)
     *        Example:  PageRequest.of(0, 500, Sort.by("id").descending()))
     *
     * @return List<LoanFacility>
     *         list of  loan facilities
     */
    LoanFacilityPagination getFacilitiesByProductPageable(String productId, Pageable pageable);

     LoanFacilityPagination getFacilityPage(String productId, Pageable pageable);
}


