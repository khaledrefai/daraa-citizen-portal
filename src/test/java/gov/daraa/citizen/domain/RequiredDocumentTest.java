package gov.daraa.citizen.domain;

import static gov.daraa.citizen.domain.CitizenServiceTestSamples.*;
import static gov.daraa.citizen.domain.RequiredDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequiredDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredDocument.class);
        RequiredDocument requiredDocument1 = getRequiredDocumentSample1();
        RequiredDocument requiredDocument2 = new RequiredDocument();
        assertThat(requiredDocument1).isNotEqualTo(requiredDocument2);

        requiredDocument2.setId(requiredDocument1.getId());
        assertThat(requiredDocument1).isEqualTo(requiredDocument2);

        requiredDocument2 = getRequiredDocumentSample2();
        assertThat(requiredDocument1).isNotEqualTo(requiredDocument2);
    }

    @Test
    void serviceTest() {
        RequiredDocument requiredDocument = getRequiredDocumentRandomSampleGenerator();
        CitizenService citizenServiceBack = getCitizenServiceRandomSampleGenerator();

        requiredDocument.setService(citizenServiceBack);
        assertThat(requiredDocument.getService()).isEqualTo(citizenServiceBack);

        requiredDocument.service(null);
        assertThat(requiredDocument.getService()).isNull();
    }
}
