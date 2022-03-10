package dk.hoejbjerg.loanmanagement.controller;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.FeeType;
import dk.hoejbjerg.loanmanagement.repository.IFeeTypeMongoImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * Rest controller for the Room part of the CaveService
 * <p>
 * Author: Team Alpha
 */
@RestController
public class FeeTypeController {

    private final IFeeTypeMongoImpl feetypeRepository;

    public FeeTypeController(IFeeTypeMongoImpl feetypeRepository) {
        this.feetypeRepository = feetypeRepository;
        this.feetypeRepository.initialize();
    }

    /**
     * Get feetype
     * <p>
     * EXAMPLE:
     * <p>
     * GET http://localhost:8080/v1/feetype/{id}
     */
    @GetMapping(path = "/v1/feetype/{id}", produces = "application/json")
    public ResponseEntity<String> getFeeType(@PathVariable(value = "id") String id) {
        try {
            FeeType feetype = feetypeRepository.getFeeType(id);
            if (Objects.isNull(feetype)) {
                return new ResponseEntity<>("Feetype not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new Gson().toJson(feetype), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }
}



