package gov.daraa.citizen.domain;

import static gov.daraa.citizen.domain.CitizenServiceTestSamples.*;
import static gov.daraa.citizen.domain.RequiredDocumentTestSamples.*;
import static gov.daraa.citizen.domain.ServiceCategoryTestSamples.*;
import static gov.daraa.citizen.domain.ServiceFormTemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CitizenServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitizenService.class);
        CitizenService citizenService1 = getCitizenServiceSample1();
        CitizenService citizenService2 = new CitizenService();
        assertThat(citizenService1).isNotEqualTo(citizenService2);

        citizenService2.setId(citizenService1.getId());
        assertThat(citizenService1).isEqualTo(citizenService2);

        citizenService2 = getCitizenServiceSample2();
        assertThat(citizenService1).isNotEqualTo(citizenService2);
    }

    @Test
    void requiredDocumentsTest() {
        CitizenService citizenService = getCitizenServiceRandomSampleGenerator();
        RequiredDocument requiredDocumentBack = getRequiredDocumentRandomSampleGenerator();

        citizenService.addRequiredDocuments(requiredDocumentBack);
        assertThat(citizenService.getRequiredDocuments()).containsOnly(requiredDocumentBack);
        assertThat(requiredDocumentBack.getService()).isEqualTo(citizenService);

        citizenService.removeRequiredDocuments(requiredDocumentBack);
        assertThat(citizenService.getRequiredDocuments()).doesNotContain(requiredDocumentBack);
        assertThat(requiredDocumentBack.getService()).isNull();

        citizenService.requiredDocuments(new HashSet<>(Set.of(requiredDocumentBack)));
        assertThat(citizenService.getRequiredDocuments()).containsOnly(requiredDocumentBack);
        assertThat(requiredDocumentBack.getService()).isEqualTo(citizenService);

        citizenService.setRequiredDocuments(new HashSet<>());
        assertThat(citizenService.getRequiredDocuments()).doesNotContain(requiredDocumentBack);
        assertThat(requiredDocumentBack.getService()).isNull();
    }

    @Test
    void formTemplatesTest() {
        CitizenService citizenService = getCitizenServiceRandomSampleGenerator();
        ServiceFormTemplate serviceFormTemplateBack = getServiceFormTemplateRandomSampleGenerator();

        citizenService.addFormTemplates(serviceFormTemplateBack);
        assertThat(citizenService.getFormTemplates()).containsOnly(serviceFormTemplateBack);
        assertThat(serviceFormTemplateBack.getService()).isEqualTo(citizenService);

        citizenService.removeFormTemplates(serviceFormTemplateBack);
        assertThat(citizenService.getFormTemplates()).doesNotContain(serviceFormTemplateBack);
        assertThat(serviceFormTemplateBack.getService()).isNull();

        citizenService.formTemplates(new HashSet<>(Set.of(serviceFormTemplateBack)));
        assertThat(citizenService.getFormTemplates()).containsOnly(serviceFormTemplateBack);
        assertThat(serviceFormTemplateBack.getService()).isEqualTo(citizenService);

        citizenService.setFormTemplates(new HashSet<>());
        assertThat(citizenService.getFormTemplates()).doesNotContain(serviceFormTemplateBack);
        assertThat(serviceFormTemplateBack.getService()).isNull();
    }

    @Test
    void categoryTest() {
        CitizenService citizenService = getCitizenServiceRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        citizenService.setCategory(serviceCategoryBack);
        assertThat(citizenService.getCategory()).isEqualTo(serviceCategoryBack);

        citizenService.category(null);
        assertThat(citizenService.getCategory()).isNull();
    }
}
