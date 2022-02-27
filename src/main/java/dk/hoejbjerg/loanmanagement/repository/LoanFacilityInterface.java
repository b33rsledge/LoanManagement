package dk.hoejbjerg.loanmanagement.repository;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LoanFacilityInterface {
    String collection = "loanFacility";

    void initialize();

    /**
     * Find a specific loan from a loan identification
     * @param id
     * @return LoanFacility
     */
    dk.hoejbjerg.loanmanagement.domain.LoanFacility getLoanFacility(String id);

    /**
     *
     * @param productId
     * @return
     */
    List<dk.hoejbjerg.loanmanagement.domain.LoanFacility> getFacilitiesByProduct(String productId);
}


