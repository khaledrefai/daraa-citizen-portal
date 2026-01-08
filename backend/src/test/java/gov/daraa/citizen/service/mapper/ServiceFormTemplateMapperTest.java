package gov.daraa.citizen.service.mapper;

import static gov.daraa.citizen.domain.ServiceFormTemplateAsserts.*;
import static gov.daraa.citizen.domain.ServiceFormTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceFormTemplateMapperTest {

    private ServiceFormTemplateMapper serviceFormTemplateMapper;

    @BeforeEach
    void setUp() {
        serviceFormTemplateMapper = new ServiceFormTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceFormTemplateSample1();
        var actual = serviceFormTemplateMapper.toEntity(serviceFormTemplateMapper.toDto(expected));
        assertServiceFormTemplateAllPropertiesEquals(expected, actual);
    }
}
