package dk.hoejbjerg.loanmanagement.repository;

import com.google.gson.Gson;
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
public class LoanFacilityMongoImpl implements LoanFacilityInterface {
    private static final Logger logger = LoggerFactory.getLogger(LoanFacilityMongoImpl.class);

    @Resource(name = "mongoTemplate")          // 'redisTemplate' is defined as a Bean in Configuration.java
    private MongoOperations loanFacilityOp;

    public void initialize() {

        // Only initialize if the collection does not already exist.
        if (loanFacilityOp.collectionExists(collection)) {
            return;
        }

        // Read loans from the mockup file and insert them as documents in MongoDB
        try {

            File file = ResourceUtils.getFile("classpath:LoanFacilityMockup.json");
            if (file.exists()) {
                Stream<String> lines = new String(Files.readAllBytes(file.toPath())).lines();
                lines.forEach(
                        str -> {
                            dk.hoejbjerg.loanmanagement.domain.LoanFacility facility = new Gson().fromJson(str, dk.hoejbjerg.loanmanagement.domain.LoanFacility.class);
                            loanFacilityOp.insert(facility);
                        }
                );
            }
            logger.info("Loan Facility mockup data has been preloaded");
        } catch (Exception e) {
            logger.error("method=initialize, implementationClass="
                    + this.getClass().getName()
                    + "Unable to initialize: " + e);
        }
    }

    /**
     * Find a specific loan from a loan identification
     * @param id
     * @return LoanFacility
     */
    public dk.hoejbjerg.loanmanagement.domain.LoanFacility getLoanFacility(String id) {
        return loanFacilityOp.findById(id, dk.hoejbjerg.loanmanagement.domain.LoanFacility.class);
    }

    /**
     *
     * @param productId
     * @return
     */
    public List<dk.hoejbjerg.loanmanagement.domain.LoanFacility> getFacilitiesByProduct(String productId) {
        Query query = new Query().addCriteria(Criteria.where("ProductTypeId").is(productId));
        List<dk.hoejbjerg.loanmanagement.domain.LoanFacility> facilities = loanFacilityOp.find(query, dk.hoejbjerg.loanmanagement.domain.LoanFacility.class);
        return facilities;
    }
}


