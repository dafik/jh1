package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.NotificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = {BoxMapper.class})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {

    @Mapping(source = "box.id", target = "boxId")
    NotificationDTO toDto(Notification notification);

    @Mapping(source = "boxId", target = "box")
    Notification toEntity(NotificationDTO notificationDTO);

    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }
}
