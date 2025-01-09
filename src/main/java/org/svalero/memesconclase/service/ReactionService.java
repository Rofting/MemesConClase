package org.svalero.memesconclase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.repository.ReactionRepository;

import java.util.List;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    public List<Reaction> getAll() {
        return reactionRepository.findAll();
    }

    public Reaction get(long reactionId) throws ReactionNotFoundException {
        return reactionRepository.findById(reactionId)
                .orElseThrow(ReactionNotFoundException::new);
    }

    public Reaction add(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    public void delete(long reactionId) throws ReactionNotFoundException {
        reactionRepository.findById(reactionId).orElseThrow(ReactionNotFoundException::new);
        reactionRepository.deleteById(reactionId);
    }
}
