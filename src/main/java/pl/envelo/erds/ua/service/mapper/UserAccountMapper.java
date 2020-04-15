package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.UserAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccount} and its DTO {@link UserAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = {BoxMapper.class})
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount> {


    @Mapping(target = "addressBooks", ignore = true)
    @Mapping(target = "removeAddressBook", ignore = true)
    @Mapping(target = "removeBox", ignore = true)
    UserAccount toEntity(UserAccountDTO userAccountDTO);

    default UserAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setId(id);
        return userAccount;
    }
}
