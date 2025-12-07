package gov.daraa.citizen.service.mapper;

import static gov.daraa.citizen.domain.DirectorateAsserts.*;
import static gov.daraa.citizen.domain.DirectorateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectorateMapperTest {

    private DirectorateMapper directorateMapper;

    @BeforeEach
    void setUp() {
        directorateMapper = new DirectorateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDirectorateSample1();
        var actual = directorateMapper.toEntity(directorateMapper.toDto(expected));
        assertDirectorateAllPropertiesEquals(expected, actual);
    }
}
