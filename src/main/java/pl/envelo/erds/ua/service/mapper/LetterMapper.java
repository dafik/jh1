package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.LetterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Letter} and its DTO {@link LetterDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActorMapper.class, LetterGroupMapper.class})
public interface LetterMapper extends EntityMapper<LetterDTO, Letter> {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receipient.id", target = "receipientId")
    @Mapping(source = "letterGroup.id", target = "letterGroupId")
    LetterDTO toDto(Letter letter);

    @Mapping(source = "senderId", target = "sender")
    @Mapping(source = "receipientId", target = "receipient")
    @Mapping(source = "letterGroupId", target = "letterGroup")
    Letter toEntity(LetterDTO letterDTO);

    default Letter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Letter letter = new Letter();
        letter.setId(id);
        return letter;
    }
}
