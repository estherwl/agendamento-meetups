package com.bootcamp.microservicemeetup.repository;

import com.bootcamp.microservicemeetup.model.entity.Meetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MeetupRepository repository;

    @Test
    @DisplayName("Should save a meetup")
    public void saveRegistrationTest() {
        Meetup meetup_Class_attribute = createValidMeetup();

        Meetup savedRegistration = repository.save(meetup_Class_attribute);

        assertThat(savedRegistration.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should get a meetup by id")
    public void findByIdTest() {
        Meetup meetup = createValidMeetup();
        entityManager.persist(meetup);

        Optional<Meetup> foundMeetup = repository.findById(meetup.getId());

        assertThat(foundMeetup.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should delete a meetup by id")
    public void deleteTest() {
        Meetup meetup = createValidMeetup();
        entityManager.persist(meetup);

        Meetup foundMeetup = entityManager.find(Meetup.class, meetup.getId());

        repository.delete(foundMeetup);

        Meetup deletedMeetup = entityManager.find(Meetup.class, meetup.getId());

        assertThat(deletedMeetup).isNull();
    }

    private Meetup createValidMeetup() {
        return Meetup.builder()
                .id(100)
                .event("Test event")
                .meetupDate("06/06/2022")
                .build();
    }


}
