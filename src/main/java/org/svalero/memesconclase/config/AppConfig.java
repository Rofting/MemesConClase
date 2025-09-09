package org.svalero.memesconclase.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.ReactionInDto;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);

        mapper.typeMap(User.class, UserOutDto.class);

        Converter<Friendship.Status, String> statusToString =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().name();

        TypeMap<Friendship, FriendshipOutDto> tf =
                mapper.typeMap(Friendship.class, FriendshipOutDto.class);

        tf.addMappings(m -> m.using(statusToString)
                .map(Friendship::getStatus, FriendshipOutDto::setStatus));

        mapper.typeMap(CommentInDto.class, Comment.class)
              .addMappings(m -> m.skip(Comment::setId));
        mapper.typeMap(ReactionInDto.class, Reaction.class)
              .addMappings(m -> m.skip(Reaction::setId));

        return mapper;
    }
}
