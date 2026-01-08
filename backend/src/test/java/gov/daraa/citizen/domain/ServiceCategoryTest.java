package gov.daraa.citizen.domain;

import static gov.daraa.citizen.domain.CitizenServiceTestSamples.*;
import static gov.daraa.citizen.domain.DirectorateTestSamples.*;
import static gov.daraa.citizen.domain.ServiceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServiceCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceCategory.class);
        ServiceCategory serviceCategory1 = getServiceCategorySample1();
        ServiceCategory serviceCategory2 = new ServiceCategory();
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);

        serviceCategory2.setId(serviceCategory1.getId());
        assertThat(serviceCategory1).isEqualTo(serviceCategory2);

        serviceCategory2 = getServiceCategorySample2();
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);
    }

    @Test
    void servicesTest() {
        ServiceCategory serviceCategory = getServiceCategoryRandomSampleGenerator();
        CitizenService citizenServiceBack = getCitizenServiceRandomSampleGenerator();

        serviceCategory.addServices(citizenServiceBack);
        assertThat(serviceCategory.getServices()).containsOnly(citizenServiceBack);
        assertThat(citizenServiceBack.getCategory()).isEqualTo(serviceCategory);

        serviceCategory.removeServices(citizenServiceBack);
        assertThat(serviceCategory.getServices()).doesNotContain(citizenServiceBack);
        assertThat(citizenServiceBack.getCategory()).isNull();

        serviceCategory.services(new HashSet<>(Set.of(citizenServiceBack)));
        assertThat(serviceCategory.getServices()).containsOnly(citizenServiceBack);
        assertThat(citizenServiceBack.getCategory()).isEqualTo(serviceCategory);

        serviceCategory.setServices(new HashSet<>());
        assertThat(serviceCategory.getServices()).doesNotContain(citizenServiceBack);
        assertThat(citizenServiceBack.getCategory()).isNull();
    }

    @Test
    void directorateTest() {
        ServiceCategory serviceCategory = getServiceCategoryRandomSampleGenerator();
        Directorate directorateBack = getDirectorateRandomSampleGenerator();

        serviceCategory.setDirectorate(directorateBack);
        assertThat(serviceCategory.getDirectorate()).isEqualTo(directorateBack);

        serviceCategory.directorate(null);
        assertThat(serviceCategory.getDirectorate()).isNull();
    }
}
