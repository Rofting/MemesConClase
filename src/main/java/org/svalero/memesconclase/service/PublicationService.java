package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.domain.dto.PublicationPatchDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class PublicationService {

    @Autowired private UserRepository userRepository;
    @Autowired private PublicationRepository publicationRepository;
    @Autowired private ModelMapper modelMapper;

    public List<PublicationOutDto> getAll(String typeContentStr, String privacyStr, Long userId) {
        Publication.ContentType typeContent = null;
        Publication.Privacy privacy = null;

        if (typeContentStr != null && !typeContentStr.isBlank()) {
            typeContent = Publication.ContentType.valueOf(typeContentStr.toUpperCase());
        }
        if (privacyStr != null && !privacyStr.isBlank()) {
            privacy = Publication.Privacy.valueOf(privacyStr.toUpperCase());
        }

        List<Publication> publications;

        if (typeContent != null && privacy != null && userId != null) {
            publications = publicationRepository.findByTypeContentAndPrivacy(typeContent, privacy)
                    .stream()
                    .filter(p -> p.getUser() != null && Objects.equals(p.getUser().getId(), userId))
                    .toList();
        } else if (typeContent != null && privacy != null) {
            publications = publicationRepository.findByTypeContentAndPrivacy(typeContent, privacy);
        } else if (typeContent != null) {
            publications = publicationRepository.findByTypeContent(typeContent);
        } else if (privacy != null) {
            publications = publicationRepository.findByPrivacy(privacy);
        } else if (userId != null) {
            publications = publicationRepository.findByUser_Id(userId);
        } else {
            publications = publicationRepository.findAll();
        }

        return publications.stream().map(this::toOutDto).toList();
    }

    public PublicationOutDto get(long publicationId) throws PublicationNotFoundException {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        return toOutDto(publication);
    }

    public PublicationOutDto add(PublicationInDto in) throws UserNotFoundException {
        User user = userRepository.findById(in.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario " + in.getUserId() + " no existe"));

        Publication p = modelMapper.map(in, Publication.class);
        p.setUser(user); // relación

        try {
            Publication saved = publicationRepository.save(p);
            return toOutDto(saved);
        } catch (DataIntegrityViolationException dive) {
            throw new IllegalArgumentException("Datos de publicación inválidos: " + dive.getMostSpecificCause().getMessage());
        }
    }

    public PublicationOutDto modify(long publicationId, PublicationInDto in)
            throws PublicationNotFoundException, UserNotFoundException {

        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);

        if (in.getUserId() != null) {
            User user = userRepository.findById(in.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Usuario " + in.getUserId() + " no existe"));
            publication.setUser(user);
        }

        modelMapper.map(in, publication);

        try {
            Publication saved = publicationRepository.save(publication);
            return toOutDto(saved);
        } catch (DataIntegrityViolationException dive) {
            throw new IllegalArgumentException("Datos de publicación inválidos: " + dive.getMostSpecificCause().getMessage());
        }
    }

    public void delete(long publicationId) throws PublicationNotFoundException {
        publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        publicationRepository.deleteById(publicationId);
    }

    public List<PublicationOutDto> findByUser(long userId) {
        List<Publication> list = publicationRepository.findByUser_Id(userId);
        return list.stream().map(this::toOutDto).toList();
    }

    public PublicationOutDto partialModify(long publicationId, PublicationPatchDto patch)
            throws PublicationNotFoundException {

        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);

        modelMapper.map(patch, publication);

        Publication saved = publicationRepository.save(publication);
        return toOutDto(saved);
    }

    private PublicationOutDto toOutDto(Publication src) {
        if (src == null) return null;
        PublicationOutDto dto = new PublicationOutDto();
        dto.setId(src.getId());
        dto.setContent(src.getContent());
        dto.setImageUrl(src.getImageUrl());
        dto.setTypeContent(src.getTypeContent() != null ? src.getTypeContent().name() : null);
        dto.setPrivacy(src.getPrivacy() != null ? src.getPrivacy().name() : null);
        dto.setPublicationDate(src.getPublicationDate());
        dto.setUserName(src.getUser() != null ? src.getUser().getName() : null);
        dto.setLatitude(src.getLatitude());
        dto.setLongitude(src.getLongitude());
        dto.setViews(src.getViews());
        dto.setFeatured(src.isFeatured());
        return dto;
    }
}

