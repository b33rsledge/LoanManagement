package dk.hoejbjerg.loanmanagement.repository;

import dk.hoejbjerg.loanmanagement.domain.LoanFacility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanFacilityPagination implements  Serializable{
    private static final long serialVersionUID = -1L;
    List<LoanFacility> pageList;
    Pageable pageable;
}