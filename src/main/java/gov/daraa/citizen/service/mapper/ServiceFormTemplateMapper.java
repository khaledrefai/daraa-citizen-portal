package gov.daraa.citizen.service.mapper;

import gov.daraa.citizen.domain.CitizenService;
import gov.daraa.citizen.domain.ServiceFormTemplate;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
import gov.daraa.citizen.service.dto.ServiceFormTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceFormTemplate} and its DTO {@link ServiceFormTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceFormTemplateMapper extends EntityMapper<ServiceFormTemplateDTO, ServiceFormTemplate> {
    @Mapping(target = "service", source = "service", qualifiedByName = "citizenServiceId")
    ServiceFormTemplateDTO toDto(ServiceFormTemplate s);

    @Named("citizenServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitizenServiceDTO toDtoCitizenServiceId(CitizenService citizenService);
}
