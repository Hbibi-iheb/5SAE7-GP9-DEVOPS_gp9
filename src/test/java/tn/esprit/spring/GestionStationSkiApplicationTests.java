package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GestionStationSkiApplicationTests {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @Test
    public void testRetrieveAllPistes() {
        // Arrange
        Piste piste1 = new Piste();
        Piste piste2 = new Piste();
        Mockito.when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste1, piste2));

        // Act
        List<Piste> pistes = pisteServices.retrieveAllPistes();

        // Assert
        Assertions.assertEquals(2, pistes.size());
        Mockito.verify(pisteRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testAddPiste() {
        // Arrange
        Piste piste = new Piste();
        Mockito.when(pisteRepository.save(piste)).thenReturn(piste);

        // Act
        Piste savedPiste = pisteServices.addPiste(piste);

        // Assert
        Assertions.assertNotNull(savedPiste);
        Mockito.verify(pisteRepository, Mockito.times(1)).save(piste);
    }

    @Test
    public void testRemovePiste() {
        // Arrange
        Long pisteId = 1L;
        Mockito.doNothing().when(pisteRepository).deleteById(pisteId);

        // Act
        pisteServices.removePiste(pisteId);

        // Assert
        Mockito.verify(pisteRepository, Mockito.times(1)).deleteById(pisteId);
    }

    @Test
    public void testRetrievePiste() {
        // Arrange
        Long pisteId = 1L;
        Piste piste = new Piste();
        Mockito.when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));

        // Act
        Piste retrievedPiste = pisteServices.retrievePiste(pisteId);

        // Assert
        Assertions.assertNotNull(retrievedPiste);
        Mockito.verify(pisteRepository, Mockito.times(1)).findById(pisteId);
    }
}
