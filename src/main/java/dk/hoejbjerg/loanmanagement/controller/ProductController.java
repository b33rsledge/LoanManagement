package dk.hoejbjerg.loanmanagement.controller;

import com.google.gson.Gson;
import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import dk.hoejbjerg.loanmanagement.repository.LoanFacilityPagination;
import dk.hoejbjerg.loanmanagement.domain.Product;
import dk.hoejbjerg.loanmanagement.repository.ILoanFacilityMongoImpl;
import dk.hoejbjerg.loanmanagement.repository.IProductMongoImpl;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Rest controller for the Room part of the CaveService
 * <p>
 * Author: Team Alpha
 */
@RestController
public class ProductController {

    private final IProductMongoImpl productRepository;
    private final ILoanFacilityMongoImpl loanRepository;
    private final RabbitController productQueue;

    public ProductController(IProductMongoImpl productRepository, ILoanFacilityMongoImpl loanRepository, RabbitTemplate rabbitTemplate, Queue queue) {
        this.productRepository = productRepository;
        this.productRepository.initialize();
        this.loanRepository = loanRepository;
        productQueue = new RabbitController(rabbitTemplate, queue);
    }

    /**
     * Get Product
     * <p>
     * EXAMPLE:
     * <p>
     * GET http://localhost:8080/v1/product/{id}
     */
    @GetMapping(path = "/v1/product/{id}", produces = "application/json")
    public ResponseEntity<String> getProduct(@PathVariable(value = "id") String id) {
        try {
            Product product = productRepository.getProduct(id);
            if (Objects.isNull(product)) {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new Gson().toJson(product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Post room at position
     * <p>
     * EXAMPLE:
     * <p>
     * POST http://localhost:8080/v2/room/(1,0,0)
     * Content-Type: application/json
     * Accept: application/json
     * <p>
     * {
     * "description" : "Yppiekieae",
     * "creatorId" : "PHG"
     * }
     */
    @PutMapping(path = "/v1/product/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody Product product) {

        try {
            product = productRepository.updateProduct(id, product);

            if (product != null) {

                // Retrieve first page - 500 documents at a time
                LoanFacilityPagination lPage =  loanRepository.getFacilitiesByProductPageable(id,
                        PageRequest.of(0, 10000,
                        Sort.by("id").descending()));

                while (!lPage.getPageList().isEmpty()) {
                    List<LoanFacility> list = lPage.getPageList();
                    // Publish loan facilities on queue
                    for (LoanFacility loan : list)
                        productQueue.publishLoanFacility(loan);
                    // Retrieve consecutive pages
                    lPage = loanRepository.getFacilitiesByProductPageable(id, lPage.getPageable().next());
                }

                URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

                return ResponseEntity.ok()
                        .header("location", location.toString())
                        .body(new Gson().toJson(product));
            } else {
                return new ResponseEntity<>("Somehting went wrong", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

}



