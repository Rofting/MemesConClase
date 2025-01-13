package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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


    public Publication get(long publicationId) throws PublicationNotFoundException {
        return publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
    }

    public Publication add(Publication publication) {
        if (!userRepository.existsById(publication.getUser().getId())) {
            throw new IllegalArgumentException("The user with ID " + publication.getUser().getId() + " does not exist.");
        }
        return publicationRepository.save(publication);
    }

    public PublicationOutDto modify(long publicationId, PublicationInDto publicationInDto) throws PublicationNotFoundException {
        Publication publication = publicationRepository.findById(publicationId).orElseThrow(PublicationNotFoundException::new);
        modelMapper.map(publicationInDto, publication);
        publicationRepository.save(publication);
        return modelMapper.map(publication, PublicationOutDto.class);
    }


    public void delete(long publicationId) throws PublicationNotFoundException {
        publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        publicationRepository.deleteById(publicationId);
    }
}
