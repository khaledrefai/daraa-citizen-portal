package gov.daraa.citizen.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceFormTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFormTemplateDTO.class);
        ServiceFormTemplateDTO serviceFormTemplateDTO1 = new ServiceFormTemplateDTO();
        serviceFormTemplateDTO1.setId(1L);
        ServiceFormTemplateDTO serviceFormTemplateDTO2 = new ServiceFormTemplateDTO();
        assertThat(serviceFormTemplateDTO1).isNotEqualTo(serviceFormTemplateDTO2);
        serviceFormTemplateDTO2.setId(serviceFormTemplateDTO1.getId());
        assertThat(serviceFormTemplateDTO1).isEqualTo(serviceFormTemplateDTO2);
        serviceFormTemplateDTO2.setId(2L);
        assertThat(serviceFormTemplateDTO1).isNotEqualTo(serviceFormTemplateDTO2);
        serviceFormTemplateDTO1.setId(null);
        assertThat(serviceFormTemplateDTO1).isNotEqualTo(serviceFormTemplateDTO2);
    }
}
