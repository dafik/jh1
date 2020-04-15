package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.EvidenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evidence} and its DTO {@link EvidenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {BoxMapper.class})
public interface EvidenceMapper extends EntityMapper<EvidenceDTO, Evidence> {

    @Mapping(source = "box.id", target = "boxId")
    EvidenceDTO toDto(Evidence evidence);

    @Mapping(source = "boxId", target = "box")
    Evidence toEntity(EvidenceDTO evidenceDTO);

    default Evidence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Evidence evidence = new Evidence();
        evidence.setId(id);
        return evidence;
    }
}
