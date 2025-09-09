package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.List;

@Service
public class PublicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PublicationOutDto> getAll(String typeContentStr, String privacyStr) {
        Publication.ContentType typeContent = null;
        if (typeContentStr != null && !typeContentStr.isEmpty()) {
            typeContent = Publication.ContentType.valueOf(typeContentStr.toUpperCase());
        }

        Publication.Privacy privacy = null;
        if (privacyStr != null && !privacyStr.isEmpty()) {
            privacy = Publication.Privacy.valueOf(privacyStr.toUpperCase());
        }

        List<Publication> publications;

        if (typeContent != null && privacy != null) {
            publications = publicationRepository.findByTypeContentAndPrivacy(typeContent, privacy);
        } else if (typeContent != null) {
            publications = publicationRepository.findByTypeContent(typeContent);
        } else if (privacy != null) {
            publications = publicationRepository.findByPrivacy(privacy);
        } else {
            publications = publicationRepository.findAll();
        }

        return modelMapper.map(publications, new TypeToken<List<PublicationOutDto>>() {}.getType());
    }

    public PublicationOutDto get(long publicationId) throws PublicationNotFoundException {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        return modelMapper.map(publication, PublicationOutDto.class);
    }

    public PublicationOutDto add(PublicationInDto publicationInDto) {
        Publication newPublication = modelMapper.map(publicationInDto, Publication.class);

        User user = userRepository.findById(publicationInDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        newPublication.setUser(user);

        Publication savedPublication = publicationRepository.save(newPublication);

        return modelMapper.map(savedPublication, PublicationOutDto.class);
    }

    public PublicationOutDto modify(long publicationId, PublicationInDto publicationInDto) throws PublicationNotFoundException {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        modelMapper.map(publicationInDto, publication);
        publication = publicationRepository.save(publication);
        return modelMapper.map(publication, PublicationOutDto.class);
    }

    public void delete(long publicationId) throws PublicationNotFoundException {
        publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        publicationRepository.deleteById(publicationId);
    }

    public List<PublicationOutDto> findByUser(long userId) {
        List<Publication> publicationList = publicationRepository.findByUser_Id(userId);
        return modelMapper.map(publicationList, new TypeToken<List<PublicationOutDto>>() {}.getType());
    }
}

