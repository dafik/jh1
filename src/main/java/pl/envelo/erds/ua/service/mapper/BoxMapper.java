package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.BoxDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Box} and its DTO {@link BoxDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserAccountMapper.class})
public interface BoxMapper extends EntityMapper<BoxDTO, Box> {

    @Mapping(source = "rulesAcceptanceUser.id", target = "rulesAcceptanceUserId")
    BoxDTO toDto(Box box);

    @Mapping(target = "evidences", ignore = true)
    @Mapping(target = "removeEvidence", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "removeNotification", ignore = true)
    @Mapping(target = "letterGroups", ignore = true)
    @Mapping(target = "removeLetterGroup", ignore = true)
    @Mapping(source = "rulesAcceptanceUserId", target = "rulesAcceptanceUser")
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "removeUserAccount", ignore = true)
    Box toEntity(BoxDTO boxDTO);

    default Box fromId(Long id) {
        if (id == null) {
            return null;
        }
        Box box = new Box();
        box.setId(id);
        return box;
    }
}
