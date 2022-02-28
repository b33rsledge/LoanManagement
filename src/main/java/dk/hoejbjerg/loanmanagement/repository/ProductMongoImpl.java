package dk.hoejbjerg.loanmanagement.repository;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.Product;
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
public class ProductMongoImpl implements ProductInteface {
    private static final Logger logger = LoggerFactory.getLogger(ProductMongoImpl.class);
    private static final String collection = "product";

    @Resource(name = "mongoTemplate")
    private MongoOperations productMongo;

    /**
     * initialize collection
     */
    @Override
    public void initialize() {
        // Only initialize if the collection does not already exist.
        if (productMongo.collectionExists(collection)) {
            return;
        }

        try {
            logger.info("Initializing product data: starting");
            File file = ResourceUtils.getFile("file:./data/ProductMockup.json");
            if (file.exists()) {
                logger.info("Initializing product data: Reading data");
                Stream<String> lines = new String(Files.readAllBytes(file.toPath())).lines();
                lines.forEach(
                        str -> {
                            Product product = new Gson().fromJson(str, Product.class);
                            productMongo.insert(product);
                        }
                );
                logger.info("Product mockup data has been preloaded");
            }
        } catch (Exception e) {
            logger.error("method=initialize, implementationClass="
                  + "PRODUCT - Unable to initialize: " + e);
        }
    }

    /***
     * Update properties of a product.
     */
    @Override
    public Product updateProduct(String id, Product pToUpdate) {
        try {
            Product oProduct = getProduct(id);
            if (null == oProduct) {
                logger.error("method=updateProduct, implementationClass=" + this.getClass().getName() + "Product not found");
            } else {
                pToUpdate.setId(oProduct.getId());
                productMongo.save(pToUpdate);
                return pToUpdate;
            }
        } catch (Exception e) {
            logger.error("method=updateProduct, implementationClass="
                    + this.getClass().getName()
                    + "Unable to update: " + e);
        }
        return null;
    }

    /***
     * Get product by product id.
     */
    @Override
    public Product getProduct(String id) {

        try {
            return productMongo.findById(id, Product.class);
        } catch (Exception e) {
            logger.error("method=updateProduct, implementationClass="
                    + this.getClass().getName()
                    + "Unable to find: " + e);
        }
        return null;
    }
}