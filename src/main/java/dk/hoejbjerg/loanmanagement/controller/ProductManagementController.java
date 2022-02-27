package dk.hoejbjerg.loanmanagement.controller;

import dk.hoejbjerg.loanmanagement.repository.ProductManagementMongoImpl;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for the Room part of the CaveService
 *
 * Author: Team Alpha
 */
@RestController
public class ProductManagementController {

    private final ProductManagementMongoImpl productTypeRepository;

    public ProductManagementController(ProductManagementMongoImpl productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeRepository.initialize();
    }
}



