package gov.daraa.citizen.domain;

import static gov.daraa.citizen.domain.CitizenServiceTestSamples.*;
import static gov.daraa.citizen.domain.ServiceFormTemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceFormTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFormTemplate.class);
        ServiceFormTemplate serviceFormTemplate1 = getServiceFormTemplateSample1();
        ServiceFormTemplate serviceFormTemplate2 = new ServiceFormTemplate();
        assertThat(serviceFormTemplate1).isNotEqualTo(serviceFormTemplate2);

        serviceFormTemplate2.setId(serviceFormTemplate1.getId());
        assertThat(serviceFormTemplate1).isEqualTo(serviceFormTemplate2);

        serviceFormTemplate2 = getServiceFormTemplateSample2();
        assertThat(serviceFormTemplate1).isNotEqualTo(serviceFormTemplate2);
    }

    @Test
    void serviceTest() {
        ServiceFormTemplate serviceFormTemplate = getServiceFormTemplateRandomSampleGenerator();
        CitizenService citizenServiceBack = getCitizenServiceRandomSampleGenerator();

        serviceFormTemplate.setService(citizenServiceBack);
        assertThat(serviceFormTemplate.getService()).isEqualTo(citizenServiceBack);

        serviceFormTemplate.service(null);
        assertThat(serviceFormTemplate.getService()).isNull();
    }
}
