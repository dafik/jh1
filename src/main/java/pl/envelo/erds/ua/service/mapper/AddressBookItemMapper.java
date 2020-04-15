package pl.envelo.erds.ua.service.mapper;


import pl.envelo.erds.ua.domain.*;
import pl.envelo.erds.ua.service.dto.AddressBookItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AddressBookItem} and its DTO {@link AddressBookItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserAccountMapper.class})
public interface AddressBookItemMapper extends EntityMapper<AddressBookItemDTO, AddressBookItem> {

    @Mapping(source = "userAccount.id", target = "userAccountId")
    AddressBookItemDTO toDto(AddressBookItem addressBookItem);

    @Mapping(source = "userAccountId", target = "userAccount")
    AddressBookItem toEntity(AddressBookItemDTO addressBookItemDTO);

    default AddressBookItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        AddressBookItem addressBookItem = new AddressBookItem();
        addressBookItem.setId(id);
        return addressBookItem;
    }
}
