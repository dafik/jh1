package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.IncomingMetaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link IncomingMeta} and its DTO {@link IncomingMetaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IncomingMetaMapper extends EntityMapper<IncomingMetaDTO, IncomingMeta> {


    @Mapping(target = "letterGroup", ignore = true)
    IncomingMeta toEntity(IncomingMetaDTO incomingMetaDTO);

    default IncomingMeta fromId(Long id) {
        if (id == null) {
            return null;
        }
        IncomingMeta incomingMeta = new IncomingMeta();
        incomingMeta.setId(id);
        return incomingMeta;
    }
}
