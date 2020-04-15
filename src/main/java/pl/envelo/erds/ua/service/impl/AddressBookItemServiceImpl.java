package pl.envelo.erds.ua.service.impl;

import pl.envelo.erds.ua.service.AddressBookItemService;
import pl.envelo.erds.ua.domain.AddressBookItem;
import pl.envelo.erds.ua.repository.AddressBookItemRepository;
import pl.envelo.erds.ua.service.dto.AddressBookItemDTO;
import pl.envelo.erds.ua.service.mapper.AddressBookItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AddressBookItem}.
 */
@Service
@Transactional
public class AddressBookItemServiceImpl implements AddressBookItemService {

    private final Logger log = LoggerFactory.getLogger(AddressBookItemServiceImpl.class);

    private final AddressBookItemRepository addressBookItemRepository;

    private final AddressBookItemMapper addressBookItemMapper;

    public AddressBookItemServiceImpl(AddressBookItemRepository addressBookItemRepository, AddressBookItemMapper addressBookItemMapper) {
        this.addressBookItemRepository = addressBookItemRepository;
        this.addressBookItemMapper = addressBookItemMapper;
    }

    /**
     * Save a addressBookItem.
     *
     * @param addressBookItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AddressBookItemDTO save(AddressBookItemDTO addressBookItemDTO) {
        log.debug("Request to save AddressBookItem : {}", addressBookItemDTO);
        AddressBookItem addressBookItem = addressBookItemMapper.toEntity(addressBookItemDTO);
        addressBookItem = addressBookItemRepository.save(addressBookItem);
        return addressBookItemMapper.toDto(addressBookItem);
    }

    /**
     * Get all the addressBookItems.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AddressBookItemDTO> findAll() {
        log.debug("Request to get all AddressBookItems");
        return addressBookItemRepository.findAll().stream()
            .map(addressBookItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one addressBookItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AddressBookItemDTO> findOne(Long id) {
        log.debug("Request to get AddressBookItem : {}", id);
        return addressBookItemRepository.findById(id)
            .map(addressBookItemMapper::toDto);
    }

    /**
     * Delete the addressBookItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AddressBookItem : {}", id);
        addressBookItemRepository.deleteById(id);
    }
}
