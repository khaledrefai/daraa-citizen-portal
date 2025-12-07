package gov.daraa.citizen.service.mapper;

import static gov.daraa.citizen.domain.CitizenServiceAsserts.*;
import static gov.daraa.citizen.domain.CitizenServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CitizenServiceMapperTest {

    private CitizenServiceMapper citizenServiceMapper;

    @BeforeEach
    void setUp() {
        citizenServiceMapper = new CitizenServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCitizenServiceSample1();
        var actual = citizenServiceMapper.toEntity(citizenServiceMapper.toDto(expected));
        assertCitizenServiceAllPropertiesEquals(expected, actual);
    }
}
