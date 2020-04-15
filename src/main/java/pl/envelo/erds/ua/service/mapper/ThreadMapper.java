package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.ThreadDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Thread} and its DTO {@link ThreadDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThreadMapper extends EntityMapper<ThreadDTO, Thread> {


    @Mapping(target = "letterGroups", ignore = true)
    @Mapping(target = "removeLetterGroup", ignore = true)
    Thread toEntity(ThreadDTO threadDTO);

    default Thread fromId(Long id) {
        if (id == null) {
            return null;
        }
        Thread thread = new Thread();
        thread.setId(id);
        return thread;
    }
}
