package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.LetterGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LetterGroup} and its DTO {@link LetterGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {IncomingMetaMapper.class, OutgoingMetaMapper.class, BoxMapper.class, ThreadMapper.class})
public interface LetterGroupMapper extends EntityMapper<LetterGroupDTO, LetterGroup> {

    @Mapping(source = "incomingMeta.id", target = "incomingMetaId")
    @Mapping(source = "outgoingMeta.id", target = "outgoingMetaId")
    @Mapping(source = "box.id", target = "boxId")
    @Mapping(source = "thread.id", target = "threadId")
    LetterGroupDTO toDto(LetterGroup letterGroup);

    @Mapping(source = "incomingMetaId", target = "incomingMeta")
    @Mapping(source = "outgoingMetaId", target = "outgoingMeta")
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "removeAttachment", ignore = true)
    @Mapping(target = "letters", ignore = true)
    @Mapping(target = "removeLetter", ignore = true)
    @Mapping(source = "boxId", target = "box")
    @Mapping(source = "threadId", target = "thread")
    LetterGroup toEntity(LetterGroupDTO letterGroupDTO);

    default LetterGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        LetterGroup letterGroup = new LetterGroup();
        letterGroup.setId(id);
        return letterGroup;
    }
}
