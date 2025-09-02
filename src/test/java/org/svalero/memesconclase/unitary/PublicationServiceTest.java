package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;
import org.svalero.memesconclase.service.PublicationService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PublicationServiceTest {

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PublicationService publicationService;

    private Publication publication;
    private PublicationInDto publicationInDto;
    private PublicationOutDto publicationOutDto;

    @BeforeEach
    void setUp() {
        openMocks(this);

        publication = new Publication();
        publication.setId(1L);
        publication.setTypeContent(Publication.ContentType.IMAGE);
        publication.setPrivacy(Publication.Privacy.PUBLIC);

        publicationInDto = new PublicationInDto();
        publicationInDto.setTypeContent("image");
        publicationInDto.setPrivacy("public");
        publicationInDto.setUserId(1L);

        publicationOutDto = new PublicationOutDto();
        publicationOutDto.setId(1L);
        publicationOutDto.setTypeContent("image");
        publicationOutDto.setPrivacy("public");
    }

    @Test
    void shouldGetPublicationById() throws PublicationNotFoundException {
        when(publicationRepository.findById(1L)).thenReturn(Optional.of(publication));
        when(modelMapper.map(publication, PublicationOutDto.class)).thenReturn(publicationOutDto);

        PublicationOutDto result = publicationService.get(1L);

        assertEquals("image", result.getTypeContent());
    }

    @Test
    void shouldThrowWhenPublicationNotFound() {
        when(publicationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PublicationNotFoundException.class, () -> publicationService.get(999L));
    }

    @Test
    void shouldAddPublication() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(publicationInDto, Publication.class)).thenReturn(publication);
        when(publicationRepository.save(publication)).thenReturn(publication);
        when(modelMapper.map(publication, PublicationOutDto.class)).thenReturn(publicationOutDto);

        PublicationOutDto result = publicationService.add(publicationInDto);

        assertEquals("image", result.getTypeContent());
    }

    @Test
    void shouldDeletePublication() throws PublicationNotFoundException {
        when(publicationRepository.findById(1L)).thenReturn(Optional.of(publication));
        doNothing().when(publicationRepository).deleteById(1L);

        assertDoesNotThrow(() -> publicationService.delete(1L));
    }
}
