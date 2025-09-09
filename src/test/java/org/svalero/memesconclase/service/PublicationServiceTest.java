package org.svalero.memesconclase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // para ignorar stubbings no usados según el camino
class PublicationServiceTest {

    @Mock private PublicationRepository publicationRepository;
    @Mock private UserRepository userRepository;

    @Mock private ModelMapper modelMapper;

    @Mock private TypeMap<PublicationInDto, Publication> publicationInTypeMap;
    @Mock private TypeMap<Publication, PublicationOutDto> publicationOutTypeMap;

    @InjectMocks
    private PublicationService publicationService;

    private void stubModelMapperBothWays() {
        lenient().when(modelMapper.map(any(Publication.class), eq(PublicationOutDto.class)))
                .thenReturn(new PublicationOutDto());
        lenient().when(modelMapper.typeMap(Publication.class, PublicationOutDto.class))
                .thenReturn(publicationOutTypeMap);
        lenient().when(publicationOutTypeMap.map(any(Publication.class)))
                .thenReturn(new PublicationOutDto());

        lenient().when(modelMapper.typeMap(PublicationInDto.class, Publication.class))
                .thenReturn(publicationInTypeMap);
        lenient().when(publicationInTypeMap.map(any(PublicationInDto.class)))
                .thenReturn(new Publication());
    }

    @Test
    void get_modify_delete_paths() throws Exception {

        Publication existing = new Publication();
        when(publicationRepository.findById(1L)).thenReturn(Optional.of(existing));

        PublicationInDto dto = new PublicationInDto();
        dto.setUserId(10L);
        dto.setContent("hola mundo");
        dto.setPrivacy("PUBLIC");
        dto.setTypeContent("TEXT");
        dto.setLatitude(40.0);
        dto.setLongitude(-3.0);

        when(userRepository.findById(10L)).thenReturn(Optional.of(new User()));
        when(publicationRepository.save(any(Publication.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        stubModelMapperBothWays();


        publicationService.get(1L);
        publicationService.modify(1L, dto);
        publicationService.delete(1L);


        verify(publicationRepository, times(3)).findById(1L);
        verify(publicationRepository, times(1)).save(any(Publication.class));
        verify(publicationRepository, times(1)).deleteById(1L);
    }
}
