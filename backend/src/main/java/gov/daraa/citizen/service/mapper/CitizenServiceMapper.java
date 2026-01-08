package gov.daraa.citizen.service.mapper;

import gov.daraa.citizen.domain.CitizenService;
import gov.daraa.citizen.domain.ServiceCategory;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
import gov.daraa.citizen.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CitizenService} and its DTO {@link CitizenServiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { ServiceCategoryMapper.class, RequiredDocumentMapper.class, ServiceFormTemplateMapper.class })
public interface CitizenServiceMapper extends EntityMapper<CitizenServiceDTO, CitizenService> {
    @Mapping(target = "category", source = "category", qualifiedByName = "serviceCategoryId")
    CitizenServiceDTO toDto(CitizenService s);

    @Named("serviceCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServiceCategoryDTO toDtoServiceCategoryId(ServiceCategory serviceCategory);
}
