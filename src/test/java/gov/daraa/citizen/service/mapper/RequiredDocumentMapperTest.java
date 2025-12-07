package gov.daraa.citizen.service.mapper;

import static gov.daraa.citizen.domain.RequiredDocumentAsserts.*;
import static gov.daraa.citizen.domain.RequiredDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequiredDocumentMapperTest {

    private RequiredDocumentMapper requiredDocumentMapper;

    @BeforeEach
    void setUp() {
        requiredDocumentMapper = new RequiredDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRequiredDocumentSample1();
        var actual = requiredDocumentMapper.toEntity(requiredDocumentMapper.toDto(expected));
        assertRequiredDocumentAllPropertiesEquals(expected, actual);
    }
}
