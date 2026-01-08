package gov.daraa.citizen.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import gov.daraa.citizen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DirectorateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectorateDTO.class);
        DirectorateDTO directorateDTO1 = new DirectorateDTO();
        directorateDTO1.setId(1L);
        DirectorateDTO directorateDTO2 = new DirectorateDTO();
        assertThat(directorateDTO1).isNotEqualTo(directorateDTO2);
        directorateDTO2.setId(directorateDTO1.getId());
        assertThat(directorateDTO1).isEqualTo(directorateDTO2);
        directorateDTO2.setId(2L);
        assertThat(directorateDTO1).isNotEqualTo(directorateDTO2);
        directorateDTO1.setId(null);
        assertThat(directorateDTO1).isNotEqualTo(directorateDTO2);
    }
}
