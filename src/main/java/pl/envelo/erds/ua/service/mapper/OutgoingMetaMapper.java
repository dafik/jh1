package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.OutgoingMetaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutgoingMeta} and its DTO {@link OutgoingMetaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OutgoingMetaMapper extends EntityMapper<OutgoingMetaDTO, OutgoingMeta> {


    @Mapping(target = "letterGroup", ignore = true)
    OutgoingMeta toEntity(OutgoingMetaDTO outgoingMetaDTO);

    default OutgoingMeta fromId(Long id) {
        if (id == null) {
            return null;
        }
        OutgoingMeta outgoingMeta = new OutgoingMeta();
        outgoingMeta.setId(id);
        return outgoingMeta;
    }
}
