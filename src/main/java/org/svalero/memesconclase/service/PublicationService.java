package org.svalero.memesconclase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.model.Publication;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.List;

@Service
public class PublicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    public List<Publication> getAll() {
        return publicationRepository.findAll();
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


    public void delete(long publicationId) throws PublicationNotFoundException {
        publicationRepository.findById(publicationId)
                .orElseThrow(PublicationNotFoundException::new);
        publicationRepository.deleteById(publicationId);
    }
}
