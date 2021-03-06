package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.repository.FolderRepository;
import com.cosmicode.mypass.service.dto.FolderDTO;
import com.cosmicode.mypass.service.mapper.FolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Folder.
 */
@Service
@Transactional
public class FolderService {

    private final Logger log = LoggerFactory.getLogger(FolderService.class);

    private final FolderRepository folderRepository;

    private final FolderMapper folderMapper;

    public FolderService(FolderRepository folderRepository, FolderMapper folderMapper) {
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
    }

    /**
     * Save a folder.
     *
     * @param folderDTO the entity to save
     * @return the persisted entity
     */
    public FolderDTO save(FolderDTO folderDTO) {
        log.debug("Request to save Folder : {}", folderDTO);

        Folder folder = folderMapper.toEntity(folderDTO);
        folder.setModified(Instant.now());
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    /**
     * Get all the folders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FolderDTO> findAll() {
        log.debug("Request to get all Folders");
        return folderRepository.findAllWithEagerRelationships().stream()
            .map(folderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Folder with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<FolderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return folderRepository.findAllWithEagerRelationships(pageable).map(folderMapper::toDto);
    }
    

    /**
     * Get one folder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FolderDTO> findOne(Long id) {
        log.debug("Request to get Folder : {}", id);
        return folderRepository.findOneWithEagerRelationships(id)
            .map(folderMapper::toDto);
    }

    /**
     * Delete the folder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Folder : {}", id);
        folderRepository.deleteById(id);
    }

    public List<FolderDTO> getCurrentUserFolders(){
        log.debug("Request to get Secrets for current user");
        return folderRepository.findByCurrentUserHasAccess().stream()
            .map(folderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
