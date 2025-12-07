package gov.daraa.citizen.domain;

import static gov.daraa.citizen.domain.DirectorateTestSamples.*;
import static gov.daraa.citizen.domain.ServiceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DirectorateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Directorate.class);
        Directorate directorate1 = getDirectorateSample1();
        Directorate directorate2 = new Directorate();
        assertThat(directorate1).isNotEqualTo(directorate2);

        directorate2.setId(directorate1.getId());
        assertThat(directorate1).isEqualTo(directorate2);

        directorate2 = getDirectorateSample2();
        assertThat(directorate1).isNotEqualTo(directorate2);
    }

    @Test
    void serviceCategoriesTest() {
        Directorate directorate = getDirectorateRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        directorate.addServiceCategories(serviceCategoryBack);
        assertThat(directorate.getServiceCategories()).containsOnly(serviceCategoryBack);
        assertThat(serviceCategoryBack.getDirectorate()).isEqualTo(directorate);

        directorate.removeServiceCategories(serviceCategoryBack);
        assertThat(directorate.getServiceCategories()).doesNotContain(serviceCategoryBack);
        assertThat(serviceCategoryBack.getDirectorate()).isNull();

        directorate.serviceCategories(new HashSet<>(Set.of(serviceCategoryBack)));
        assertThat(directorate.getServiceCategories()).containsOnly(serviceCategoryBack);
        assertThat(serviceCategoryBack.getDirectorate()).isEqualTo(directorate);

        directorate.setServiceCategories(new HashSet<>());
        assertThat(directorate.getServiceCategories()).doesNotContain(serviceCategoryBack);
        assertThat(serviceCategoryBack.getDirectorate()).isNull();
    }
}
