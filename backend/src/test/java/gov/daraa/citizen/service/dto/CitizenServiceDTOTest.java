package gov.daraa.citizen.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitizenServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitizenServiceDTO.class);
        CitizenServiceDTO citizenServiceDTO1 = new CitizenServiceDTO();
        citizenServiceDTO1.setId(1L);
        CitizenServiceDTO citizenServiceDTO2 = new CitizenServiceDTO();
        assertThat(citizenServiceDTO1).isNotEqualTo(citizenServiceDTO2);
        citizenServiceDTO2.setId(citizenServiceDTO1.getId());
        assertThat(citizenServiceDTO1).isEqualTo(citizenServiceDTO2);
        citizenServiceDTO2.setId(2L);
        assertThat(citizenServiceDTO1).isNotEqualTo(citizenServiceDTO2);
        citizenServiceDTO1.setId(null);
        assertThat(citizenServiceDTO1).isNotEqualTo(citizenServiceDTO2);
    }
}
