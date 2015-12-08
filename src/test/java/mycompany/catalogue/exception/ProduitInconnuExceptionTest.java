package mycompany.catalogue.exception;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ProduitInconnuExceptionTest {

    @Test
    public void test1() {
        Assertions.assertThat(new ProduitInconnuException("reference")).isNotNull();
    }


}