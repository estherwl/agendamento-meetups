package com.bootcamp.microservicemeetup.service;

import com.bootcamp.microservicemeetup.controller.dto.MeetupFilterDTO;
import com.bootcamp.microservicemeetup.model.entity.Meetup;
import com.bootcamp.microservicemeetup.model.entity.Registration;
import com.bootcamp.microservicemeetup.repository.MeetupRepository;
import com.bootcamp.microservicemeetup.service.impl.MeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MeetupServiceTest {

    MeetupService meetupService;

    @MockBean
    MeetupRepository repository;

    @BeforeEach
    public void setUp() {
        this.meetupService = new MeetupServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a meetup")
    public void saveMeetup() {
        Meetup meetup = createValidMeetup();

        Mockito.when(repository.save(meetup)).thenReturn(createValidMeetup());

        Meetup savedMeetup = meetupService.save(meetup);

        assertThat(savedMeetup.getId()).isEqualTo(100);
        assertThat(savedMeetup.getEvent()).isEqualTo("Test event");
        assertThat(savedMeetup.getRegistration()).isEqualTo(registration());
        assertThat(savedMeetup.getMeetupDate()).isEqualTo("06/06/2022");
        assertThat(savedMeetup.getRegistered()).isEqualTo(true);
    }

    @Test
    @DisplayName("Should get a meetup by Id")
    public void getByIdMeetup(){
        Meetup meetup = createValidMeetup();
        Integer id = 100;

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(meetup));

        Optional<Meetup> foundMeetup = meetupService.getById(id);

        assertThat(foundMeetup.isPresent()).isTrue();
        assertThat(foundMeetup.get().getId()).isEqualTo(id);
        assertThat(foundMeetup.get().getEvent()).isEqualTo("Test event");
        assertThat(foundMeetup.get().getRegistration()).isEqualTo(registration());
        assertThat(foundMeetup.get().getMeetupDate()).isEqualTo("06/06/2022");
        assertThat(foundMeetup.get().getRegistered()).isEqualTo(true);
    }

    @Test
    @DisplayName("Should return not found when get a meetup by Id")
    public void meetupGetByIdNotFound(){
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        Optional<Meetup> foundMeetup = meetupService.getById(1);

        assertThat(foundMeetup.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should update a meetup")
    public void updateMeetup(){
        Meetup updatingMeetup = Meetup.builder().id(10).build();

        Meetup updatedMeetup = createValidMeetup();

        Mockito.when(repository.save(updatingMeetup)).thenReturn(updatedMeetup);
        Meetup meetup = meetupService.update(updatingMeetup);

        assertThat(meetup.getId()).isEqualTo(updatedMeetup.getId());
        assertThat(meetup.getEvent()).isEqualTo("Test event");
        assertThat(meetup.getRegistration()).isEqualTo(registration());
        assertThat(meetup.getMeetupDate()).isEqualTo("06/06/2022");
        assertThat(meetup.getRegistered()).isEqualTo(true);
    }

    @Test
    @DisplayName("Should find a list of meetups")
    public void findMeetup(){
        Meetup meetup = createValidMeetup();
        MeetupFilterDTO meetupDto = meetupDto();
        PageRequest pageRequest = PageRequest.of(0,10);

        List<Meetup> listMeetups = Arrays.asList(meetup);
        Page<Meetup> page = new PageImpl<Meetup>(Arrays.asList(meetup),
                PageRequest.of(0,10), 1);

        Mockito.when(repository.findByRegistrationOnMeetup(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Meetup> result = meetupService.find(meetupDto, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listMeetups);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should find a list of meetups")
    public void getRegistrationsByMeetup(){
        Meetup meetup = createValidMeetup();
        Registration registration = registration();
        PageRequest pageRequest = PageRequest.of(0,10);

        List<Meetup> listMeetups = Arrays.asList(meetup);
        Page<Meetup> page = new PageImpl<Meetup>(Arrays.asList(meetup),
                PageRequest.of(0,10), 1);

        Mockito.when(repository.findByRegistration(Mockito.any(Registration.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Meetup> result = meetupService.getRegistrationsByMeetup(registration, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listMeetups);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    private Registration registration() {
        return Registration.builder()
                .id(101)
                .name("Ana Neri")
                .dateOfRegistration("01/04/2022")
                .registration("001")
                .build();
    }

    private Meetup createValidMeetup() {
        return Meetup.builder()
                .id(100)
                .event("Test event")
                .registration(registration())
                .meetupDate("06/06/2022")
                .registered(true)
                .build();
    }

    private MeetupFilterDTO meetupDto(){
        return  MeetupFilterDTO.builder()
                .event("Test event")
                .registration("Test registration")
                .build();
    }
}
