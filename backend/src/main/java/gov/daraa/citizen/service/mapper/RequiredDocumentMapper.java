package gov.daraa.citizen.service.mapper;

import gov.daraa.citizen.domain.CitizenService;
import gov.daraa.citizen.domain.RequiredDocument;
import gov.daraa.citizen.service.dto.CitizenServiceDTO;
import gov.daraa.citizen.service.dto.RequiredDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequiredDocument} and its DTO {@link RequiredDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequiredDocumentMapper extends EntityMapper<RequiredDocumentDTO, RequiredDocument> {
    @Mapping(target = "service", source = "service", qualifiedByName = "citizenServiceId")
    RequiredDocumentDTO toDto(RequiredDocument s);

    @Named("citizenServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitizenServiceDTO toDtoCitizenServiceId(CitizenService citizenService);
}
