package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.ReactionRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.List;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ReactionOutDto> getAll(String typeStr, Long publicationId) {
        Reaction.ReactionType reactionType = null;
        if (typeStr != null && !typeStr.isEmpty()) {
            reactionType = Reaction.ReactionType.valueOf(typeStr.toUpperCase());
        }

        List<Reaction> reactionList;

        if (reactionType == null && publicationId == null) {
            reactionList = reactionRepository.findAll();
        } else if (reactionType == null) {
            reactionList = reactionRepository.findByPublication_Id(publicationId);
        } else if (publicationId == null) {
            reactionList = reactionRepository.findByType(reactionType);
        } else {
            reactionList = reactionRepository.findByTypeAndPublication_Id(reactionType, publicationId);
        }

        return modelMapper.map(reactionList, new TypeToken<List<ReactionOutDto>>() {}.getType());
    }


    public ReactionOutDto get(long reactionId) throws ReactionNotFoundException {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(String.valueOf(reactionId)));
        return modelMapper.map(reaction, ReactionOutDto.class);
    }

    public ReactionOutDto modify(long reactionId, ReactionInDto reactionInDto) throws ReactionNotFoundException {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(String.valueOf(reactionId)));

        // Mapeamos solo el tipo de reacción, no el usuario ni la publicación
        Reaction.ReactionType newType = Reaction.ReactionType.valueOf(reactionInDto.getType().toUpperCase());
        reaction.setType(newType);

        reactionRepository.save(reaction);
        return modelMapper.map(reaction, ReactionOutDto.class);
    }

    public ReactionOutDto add(ReactionInDto reactionInDto) throws UserNotFoundException, PublicationNotFoundException {
        User user = userRepository.findById(reactionInDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(reactionInDto.getUserId())));
        Publication publication = publicationRepository.findById(reactionInDto.getPublicationId())
                .orElseThrow(() -> new PublicationNotFoundException(String.valueOf(reactionInDto.getPublicationId())));

        Reaction newReaction = modelMapper.map(reactionInDto, Reaction.class);
        newReaction.setUser(user);
        newReaction.setPublication(publication);

        newReaction = reactionRepository.save(newReaction);
        return modelMapper.map(newReaction, ReactionOutDto.class);
    }

    public void delete(long reactionId) throws ReactionNotFoundException {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(String.valueOf(reactionId)));
        reactionRepository.delete(reaction);
    }
}