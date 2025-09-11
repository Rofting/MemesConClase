package org.svalero.memesconclase.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.svalero.memesconclase.domain.*;
import org.svalero.memesconclase.domain.dto.*;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Friendship.Status, String> fStatusToString =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().name();
        Converter<String, Friendship.Status> stringToFStatus =
                ctx -> {
                    String s = ctx.getSource();
                    return (s == null || s.isBlank()) ? null : Friendship.Status.valueOf(s.trim().toUpperCase());
                };

        Converter<Reaction.ReactionType, String> rxToString =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().name();
        Converter<String, Reaction.ReactionType> stringToRx =
                ctx -> {
                    String s = ctx.getSource();
                    return (s == null || s.isBlank()) ? null : Reaction.ReactionType.valueOf(s.trim().toUpperCase());
                };

        // ======== PUBLICATION

        Converter<Publication.ContentType, String> ctToString =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().name();
        Converter<String, Publication.ContentType> stringToCt =
                ctx -> {
                    String s = ctx.getSource();
                    return (s == null || s.isBlank()) ? null : Publication.ContentType.valueOf(s.trim().toUpperCase());
                };

        Converter<Publication.Privacy, String> privacyToString =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().name();
        Converter<String, Publication.Privacy> stringToPrivacy =
                ctx -> {
                    String s = ctx.getSource();
                    return (s == null || s.isBlank()) ? null : Publication.Privacy.valueOf(s.trim().toUpperCase());
                };

        DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        Converter<LocalDateTime, String> ldtToString =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().format(ISO);
        Converter<String, LocalDateTime> stringToLdt =
                ctx -> {
                    String s = ctx.getSource();
                    return (s == null || s.isBlank()) ? null : LocalDateTime.parse(s.trim(), ISO);
                };

        // Publication → PublicationOutDto
        TypeMap<Publication, PublicationOutDto> pub2out =
                mapper.typeMap(Publication.class, PublicationOutDto.class);
        pub2out.addMappings(m -> {

            m.using(ctToString).map(Publication::getTypeContent, PublicationOutDto::setTypeContent);
            m.using(privacyToString).map(Publication::getPrivacy, PublicationOutDto::setPrivacy);
            m.map(src -> src.getUser() != null ? src.getUser().getName() : null,
                    PublicationOutDto::setUserName);
        });

        // PublicationInDto → Publication
        TypeMap<PublicationInDto, Publication> in2pub =
                mapper.typeMap(PublicationInDto.class, Publication.class);
        in2pub.addMappings(m -> {
            m.skip(Publication::setId);
            m.using(stringToCt).map(PublicationInDto::getTypeContent, Publication::setTypeContent);
            m.using(stringToPrivacy).map(PublicationInDto::getPrivacy, Publication::setPrivacy);
            m.skip(Publication::setUser);
            m.skip(Publication::setPublicationDate);
        });
        // ======== /PUBLICATION ========

        // ===== User → UserOutDto =====
        mapper.typeMap(User.class, UserOutDto.class);

        // ===== Friendship → FriendshipOutDto =====
        TypeMap<Friendship, FriendshipOutDto> f2out = mapper.typeMap(Friendship.class, FriendshipOutDto.class);
        f2out.addMappings(m -> m.using(fStatusToString).map(Friendship::getStatus, FriendshipOutDto::setStatus));

        // ===== FriendshipInDto → Friendship =====
        mapper.typeMap(FriendshipInDto.class, Friendship.class).addMappings(m -> {
            m.skip(Friendship::setId);
            m.using(stringToFStatus).map(FriendshipInDto::getStatus, Friendship::setStatus);
            m.skip(Friendship::setUser);
            m.skip(Friendship::setFriend);
        });

        // ===== CommentInDto → Comment =====
        mapper.typeMap(CommentInDto.class, Comment.class).addMappings(m -> {
            m.skip(Comment::setId);
            m.skip(Comment::setUser);
            m.skip(Comment::setPublication);
        });

        // ===== ReactionInDto → Reaction =====
        mapper.typeMap(ReactionInDto.class, Reaction.class).addMappings(m -> {
            m.skip(Reaction::setId);
            m.using(stringToRx).map(ReactionInDto::getType, Reaction::setType);
            m.skip(Reaction::setUser);
            m.skip(Reaction::setPublication);
        });

        return mapper;
    }
}
