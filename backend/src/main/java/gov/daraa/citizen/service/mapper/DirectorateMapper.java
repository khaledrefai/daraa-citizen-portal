package gov.daraa.citizen.service.mapper;

import gov.daraa.citizen.domain.Directorate;
import gov.daraa.citizen.service.dto.DirectorateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Directorate} and its DTO {@link DirectorateDTO}.
 */
@Mapper(componentModel = "spring")
public interface DirectorateMapper extends EntityMapper<DirectorateDTO, Directorate> {}
