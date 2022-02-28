package dk.hoejbjerg.loanmanagement.repository;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
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

    @Resource(name = "mongoTemplate")          // 'redisTemplate' is defined as a Bean in Configuration.java
    private MongoOperations productMongo;

    public void initialize() {
        // Only initialize if the collection does not already exist.
  //     if (productMongo.collectionExists(collection)) {
    //        return;
      //  }
        logger.info("Initializing product data: starting");
        File file;

        try {
            file = ResourceUtils.getFile("file:./data/ProductMockup.json");
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
    /*
    public Room updateRoom(String position, Room roomToUpdate) throws RoomRepositoryException {

        RoomRepository.validateInput(position, roomToUpdate);

        // Get a room - we need to know if it is a new or existing room
        Room existingRoom = getRoom(position);

        if (Objects.isNull(existingRoom)) {
            logger.error("method=updateRoom, implementationClass="
                    + this.getClass().getName()
                    + "Unable to update room at position: " + position + " ");
            throw new RoomRepositoryException("Invalid room position: " + position, HttpStatus.NOT_FOUND);
        } else if (roomToUpdate.getCreatorId().equals(existingRoom.getCreatorId())) {
            // Room exists - just update description.

            //existingRoom.setDescription(roomToUpdate.getDescription());

            // Update

            roomOperations.updateFirst(query(where("id").is(position)), update("description", roomToUpdate.getDescription()), Room.class);
            return getRoom(position);
        } else {
            logger.error("method=updateRoom" +
                    "implementationClass=" + this.getClass().getName() +
                    "Unauthorized attempt to  update room: " + position + " by user " + existingRoom.getCreatorId());
            throw new RoomRepositoryException("Creator ID " + existingRoom.getCreatorId() + "does not match that of the existing room at position " + position, HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Room addRoom(String position, Room roomToAdd) throws RoomRepositoryException {

        RoomRepository.validateInput(position, roomToAdd);

        // Get a room - we need to know if it is a new or existing room
        Room existingRoom = getRoom(position);

        if (Objects.isNull(existingRoom)) {

            if (p000.getPositionString().equals(position) || isNewPositionValid(position)) {
                // if room does not exist - it's new
                Room newRoom = new Room();

                // Using a UUID cause i'm lazy and it works
                newRoom.setId(position);

                // Set creation time
                NowStrategy n = new RealNowStrategy();
                newRoom.setCreationTimeISO8601(n.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

                // Set value
                newRoom.setCreatorId(roomToAdd.getCreatorId());
                newRoom.setDescription(roomToAdd.getDescription());

                // Store it in the repository
                roomOperations.insert(newRoom);
                return newRoom;
            } else {
                throw new RoomRepositoryException("Invalid room position: " + position, HttpStatus.FORBIDDEN);
            }
        } else {
            logger.error("method=addRoom" +
                    "implementationClass=" + this.getClass().getName() +
                    "Unauthorized attempt to  update room: " + position + " by user " + existingRoom.getCreatorId());
            throw new RoomRepositoryException("Not allowed to create room at position: " + position, HttpStatus.CONFLICT);
        }

    }

    @Override
    public Room getRoom(String position) throws RoomRepositoryException {
        logger.info("method=getRoom, implementationClass=" + this.getClass().getName() + "Getting room at position: " + position);
        if (!Point3.isPositionStringValid(position)) {
            logger.error("method=getAllExitsAtPosition, implementationClass=" + this.getClass().getName() + "Getting exits for room at position: " + position);
            throw new RoomRepositoryException("invalid position string", HttpStatus.BAD_REQUEST);
        }
        return roomOperations.findById(position, Room.class);
    }

    @Override
    public ArrayList<String> getAllExitsAtPosition(String position) throws RoomRepositoryException {

        logger.info("method=getAllExitsAtPosition, implementationClass=" + this.getClass().getName() + "Getting exits for room at position: " + position);
        if (!Point3.isPositionStringValid(position)) {
            logger.error("method=getAllExitsAtPosition, implementationClass=" + this.getClass().getName() + "Getting exits for room at position: " + position);
            throw new RoomRepositoryException("invalid position string", HttpStatus.BAD_REQUEST);
        }
        ArrayList<String> rcList = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            Point3 tempPoint = (Point3) Point3.parseString(position).clone();
            tempPoint.translate(direction);
            Room r = roomOperations.findById(tempPoint.getPositionString(), Room.class);
            if (!Objects.isNull(r))
                rcList.add(direction.toString());
        }
        return rcList;

    }

    @Override
    public boolean isNewPositionValid(String position) {
        if (!Point3.isPositionStringValid(position)) return false;

        try {
            ArrayList<String> exitList = getAllExitsAtPosition(position);
            if (exitList.isEmpty()) return false;
        } catch (RoomRepositoryException e) {
            return false;
        }

        ArrayList<String> rcList = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            Point3 tempPoint = (Point3) Point3.parseString(position).clone();
            tempPoint.translate(direction);
            Room r =  roomOperations.findById(tempPoint.getPositionString(), Room.class);
            if (Objects.isNull(r))
                return true;
        }
        return false;

    }
    */


