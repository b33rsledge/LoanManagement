package dk.hoejbjerg.loanmanagement.repository;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.FeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.util.stream.Stream;

@Repository
public class IFeeTypeMongoImpl implements IFeeType {
    private static final Logger logger = LoggerFactory.getLogger(IFeeTypeMongoImpl.class);

    @Resource(name = "mongoTemplate")
    private MongoOperations feetypeMongo;

    /**
     * initialize collection
     */
    @Override
    public void initialize() {
        // Only initialize if the collection does not already exist.
        if (feetypeMongo.collectionExists(collection)) {
            return;
        }

        try {
            logger.info("Initializing product data: starting");
            File file = ResourceUtils.getFile("file:./data/FeeTypeMockup.json");
            if (file.exists()) {
                logger.info("Initializing FeeType data: Reading data");
                Stream<String> lines = new String(Files.readAllBytes(file.toPath())).lines();
                lines.forEach(
                        str -> {
                            FeeType fee = new Gson().fromJson(str, FeeType.class);
                            feetypeMongo.insert(fee);
                        }
                );
                logger.info("FeeType mockup data has been preloaded");
            }
        } catch (Exception e) {
            logger.error("method=initialize, implementationClass="
                  + "FeeType - Unable to initialize: " + e);
        }
    }


    /***
     * Get FeeType by id
     */
    @Override
    public FeeType getFeeType(String id) {

        try {
            return feetypeMongo.findById(id, FeeType.class);
        } catch (Exception e) {
            logger.error("method=getFeeType, implementationClass="
                    + this.getClass().getName()
                    + "Unable to find: " + e);
        }
        return null;
    }
}