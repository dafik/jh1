package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.ActorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actor} and its DTO {@link ActorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActorMapper extends EntityMapper<ActorDTO, Actor> {



    default Actor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Actor actor = new Actor();
        actor.setId(id);
        return actor;
    }
}
