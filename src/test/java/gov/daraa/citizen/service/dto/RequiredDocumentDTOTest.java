package gov.daraa.citizen.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequiredDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredDocumentDTO.class);
        RequiredDocumentDTO requiredDocumentDTO1 = new RequiredDocumentDTO();
        requiredDocumentDTO1.setId(1L);
        RequiredDocumentDTO requiredDocumentDTO2 = new RequiredDocumentDTO();
        assertThat(requiredDocumentDTO1).isNotEqualTo(requiredDocumentDTO2);
        requiredDocumentDTO2.setId(requiredDocumentDTO1.getId());
        assertThat(requiredDocumentDTO1).isEqualTo(requiredDocumentDTO2);
        requiredDocumentDTO2.setId(2L);
        assertThat(requiredDocumentDTO1).isNotEqualTo(requiredDocumentDTO2);
        requiredDocumentDTO1.setId(null);
        assertThat(requiredDocumentDTO1).isNotEqualTo(requiredDocumentDTO2);
    }
}
