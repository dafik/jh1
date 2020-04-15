package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.AttachmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {LetterGroupMapper.class})
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {

    @Mapping(source = "letterGroup.id", target = "letterGroupId")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(source = "letterGroupId", target = "letterGroup")
    Attachment toEntity(AttachmentDTO attachmentDTO);

    default Attachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return attachment;
    }
}
