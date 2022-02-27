package dk.hoejbjerg.loanmanagement.controller;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import dk.hoejbjerg.loanmanagement.repository.LoanFacilityMongoImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Rest controller for the Room part of the CaveService
 *
 * Author: Team Alpha
 */
@RestController
public class LoanFacilityController {

    private final LoanFacilityMongoImpl loanFacilityStorage;

    public LoanFacilityController(LoanFacilityMongoImpl loanFacilityRepository) {
        this.loanFacilityStorage = loanFacilityRepository;
        this.loanFacilityStorage.initialize();
    }

    /**
     * Get all exits at position
     *
     * EXAMPLE:
     *
     * GET http://localhost:8080/v2/room/{1,1,1}/exits
     */
    @GetMapping(path="/v1/LoanFacility/{agreementId}", produces = "application/json")
    public ResponseEntity<String> getLoanFacility(@PathVariable(value = "agreementId") String id   )
    {
        try {
            LoanFacility loanFacility = loanFacilityStorage.getLoanFacility(id);
            if (Objects.isNull(loanFacility)) {
                return new ResponseEntity<>("Loan facility not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new Gson().toJson(loanFacility), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

}



