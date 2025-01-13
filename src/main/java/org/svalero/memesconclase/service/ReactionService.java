package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.repository.ReactionRepository;

import java.util.List;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ReactionOutDto> getAll(String type, Long publicationId) {
        List<Reaction> reactionList;

        if (type.isEmpty() && publicationId == null) {
            reactionList = reactionRepository.findAll();
        } else if (type.isEmpty()) {
            reactionList = reactionRepository.findByPublicationId(publicationId);
        } else if (publicationId == null) {
            reactionList = reactionRepository.findByType(type);
        } else {
            reactionList = reactionRepository.findByTypeAndPublicationId(type, publicationId);
        }

        return modelMapper.map(reactionList, new TypeToken<List<ReactionOutDto>>() {}.getType());
    }


    public Reaction get(long reactionId) throws ReactionNotFoundException {
        return reactionRepository.findById(reactionId)
                .orElseThrow(ReactionNotFoundException::new);
    }

    public ReactionOutDto modify(long reactionId, ReactionInDto reactionInDto) throws ReactionNotFoundException {
        Reaction reaction = reactionRepository.findById(reactionId).orElseThrow(ReactionNotFoundException::new);
        modelMapper.map(reactionInDto, reaction);
        reactionRepository.save(reaction);
        return modelMapper.map(reaction, ReactionOutDto.class);
    }

    public Reaction add(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    public void delete(long reactionId) throws ReactionNotFoundException {
        reactionRepository.findById(reactionId).orElseThrow(ReactionNotFoundException::new);
        reactionRepository.deleteById(reactionId);
    }
}
