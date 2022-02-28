package dk.hoejbjerg.loanmanagement.repository;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
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
    private MongoOperations mongoOperations;

    /**
     * initialize collection
     */
    @Override
    public void initialize() {

        // Only initialize if the collection does not already exist.
        if (mongoOperations.collectionExists(collection)) {
            return;
        }


        // Read loans from the mockup file and insert them as documents in MongoDB, given that a mockup file exists.
        try {

            File file = ResourceUtils.getFile("file:./data/LoanFacilityMockup.json");
            if (file.exists()) {
                Stream<String> lines = new String(Files.readAllBytes(file.toPath())).lines();
                lines.forEach(
                        str -> {
                            dk.hoejbjerg.loanmanagement.domain.LoanFacility facility = new Gson().fromJson(str, dk.hoejbjerg.loanmanagement.domain.LoanFacility.class);
                            mongoOperations.insert(facility);
                        }

                );
                logger.info("Loan facility mockup data has been preloaded");
            }
        } catch (Exception e) {
            logger.error("method=initialize, implementationClass="
                    + this.getClass().getName()
                    + "Unable to initialize: " + e );
        }
    }
    /**
     * Find a specific loan from a loan identification
     *
     * @return LoanFacility
     */
    @Override
    public LoanFacility getLoanFacility(String id) {
        return mongoOperations.findById(id, LoanFacility.class);
    }

    /**
     * Gert all loan facilities, which are depending on a specific product.
     *
     * @return LoanFacility
     */
    @Override
    public List<LoanFacility> getFacilitiesByProduct(String productId) {
        Query query = new Query()
                .addCriteria(Criteria.where("ProductTypeId").is(productId));
        return mongoOperations.find(query, LoanFacility.class);
    }

    /**
     * Gert all loan facilities, which are depending on a specific product. PageAble i.e. a limited number at a time
     *
     * @return LoanFacilityPagination
     */
    @Override
    public LoanFacilityPagination getFacilitiesByProductPageable(String productId, Pageable pageable) {

        Query query = new Query()
                .addCriteria(Criteria.where("ProductTypeId").is(productId))
                .with(pageable)
                .skip((long) pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize());

        LoanFacilityPagination lPage = new LoanFacilityPagination();
        lPage.setPageList(mongoOperations.find(query, LoanFacility.class));
        lPage.setPageable(pageable);
        return lPage;
    }
}


