package gov.daraa.citizen.service.mapper;

import gov.daraa.citizen.domain.Directorate;
import gov.daraa.citizen.domain.ServiceCategory;
import gov.daraa.citizen.service.dto.DirectorateDTO;
import gov.daraa.citizen.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceCategory} and its DTO {@link ServiceCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceCategoryMapper extends EntityMapper<ServiceCategoryDTO, ServiceCategory> {
    @Mapping(target = "directorate", source = "directorate", qualifiedByName = "directorateId")
    ServiceCategoryDTO toDto(ServiceCategory s);

    @Named("directorateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DirectorateDTO toDtoDirectorateId(Directorate directorate);
}
