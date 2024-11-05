package tn.esprit.spring.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Optional;

class RegistrationServicesImplTest {

    @InjectMocks
    private RegistrationServicesImpl registrationService;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        Skier skier = new Skier();
        skier.setNumSkier(1L);

        Registration registration = new Registration();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration savedRegistration = registrationService.addRegistrationAndAssignToSkier(registration, 1L);

        assertNotNull(savedRegistration);
        assertEquals(skier, savedRegistration.getSkier());
    }
}
