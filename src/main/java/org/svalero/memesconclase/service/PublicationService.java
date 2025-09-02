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

    public List<PublicationOutDto> getAll(String typeContent, String privacy) {
        List<Publication> publicationList;

        if (typeContent.isEmpty() && privacy.isEmpty()) {
            publicationList = publicationRepository.findAll();
        } else if (typeContent.isEmpty()) {
            publicationList = publicationRepository.findByPrivacy(privacy);
        } else if (privacy.isEmpty()) {
            publicationList = publicationRepository.findByTypeContent(typeContent);
        } else {
            publicationList = publicationRepository.findByTypeContentAndPrivacy(typeContent, privacy);
        }

        return modelMapper.map(publicationList, new TypeToken<List<PublicationOutDto>>() {}.getType());
    }

    public PublicationOutDto get(long publicationId) throws PublicationNotFoundException {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        return modelMapper.map(publication, PublicationOutDto.class);
    }

    public PublicationOutDto add(PublicationInDto publicationInDto) {
        // 1. Convertimos el DTO a la entidad. Gracias a la configuración, el ID se ignorará.
        Publication newPublication = modelMapper.map(publicationInDto, Publication.class);

        // 2. Aún necesitamos buscar y asignar el objeto User manualmente.
        User user = userRepository.findById(publicationInDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        newPublication.setUser(user);

        // 3. Guardamos la nueva publicación.
        Publication savedPublication = publicationRepository.save(newPublication);

        // 4. Devolvemos el DTO de salida.
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

