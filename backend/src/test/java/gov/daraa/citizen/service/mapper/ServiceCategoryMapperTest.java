package gov.daraa.citizen.service.mapper;

import static gov.daraa.citizen.domain.ServiceCategoryAsserts.*;
import static gov.daraa.citizen.domain.ServiceCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceCategoryMapperTest {

    private ServiceCategoryMapper serviceCategoryMapper;

    @BeforeEach
    void setUp() {
        serviceCategoryMapper = new ServiceCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceCategorySample1();
        var actual = serviceCategoryMapper.toEntity(serviceCategoryMapper.toDto(expected));
        assertServiceCategoryAllPropertiesEquals(expected, actual);
    }
}
