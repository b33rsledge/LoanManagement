package dk.hoejbjerg.loanmanagement.repository;

import dk.hoejbjerg.loanmanagement.Configuration;
import dk.hoejbjerg.loanmanagement.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * TDD of RoomRepository interface and
 * driving the RedisRepository implementation.
 *
 * @author Peter Højbjerg
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IProductMongoImpl.class, Configuration.class})

@ContextConfiguration()
public class ProductMongoTest {

    @Autowired
    IProductMongoImpl storage;

    @BeforeEach
    public void setup() {
        this.storage.initialize();
    }

    @Test
    public void shouldGetProduct1000() {
        Product p = storage.getProduct("1000");
    }
    /*
    @Test
    public void shouldGetFacilitiesForProduct2000() {
        List<LoanFacility> loans = storage.getFacilitiesByProduct("2000");
        Assertions.assertTrue(loans.size() > 0);
    }
    @Test
    public void shouldGetLoanFacility() {
      LoanFacility l = storage.getLoanFacility("3fa851a9-bb01-4ef2-ac45-2b53bbbf51eb");
      assertThat(l.getTerm(), is(16));
      assertThat(l.getIBAN(), is("AT40 7865 0444 5015 0070"));
    }

    @Test
    public void shouldNotGetLoanFacility() {
        LoanFacility l = storage.getLoanFacility("42");
        Assertions.assertNull(l);
    }
*/
}
