import org.hamcrest.collection.IsEmptyCollection;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightTests {

    private Flight testFlight;

    @Test
    public void constructor_WhenCalledWithEvenColumns_ConstructsTheArrayCorrectly() {
        testFlight = new Flight(1, 2);
        Reservation[][] flightSeats = testFlight.getSeats();

        assertAll(
            () -> assertNull(flightSeats[0][0]),
            () -> assertEquals("AISLE", flightSeats[0][1].getPassengerName()),
            () -> assertNull(flightSeats[0][2])
        );
    }

    @Test
    public void constructor_WhenCalledWithOddColumns_ConstructsTheArrayCorrectly() {
        testFlight = new Flight(1, 3);
        Reservation[][] flightSeats = testFlight.getSeats();

        assertAll(
            () -> assertNull(flightSeats[0][0]),
            () -> assertNull(flightSeats[0][1]),
            () -> assertEquals("AISLE", flightSeats[0][2].getPassengerName()),
            () -> assertNull(flightSeats[0][3])
        );
    }

    @Test
    public void getFrequentFlyers_WhenThereAreNoFrequentFlyers_ShouldReturnAnEmptyArrayList() {
        testFlight = new Flight(1,1);
        assertThat(testFlight.getFrequentFlyers(), IsEmptyCollection.empty());
    }

    @Test
    public void getFrequentFlyers_WhenThereAreFrequentFlyers_ShouldReturnAnArrayListOfNames() {
        testFlight = new Flight(2,2);

        testFlight.reserveNextAvailableSeat("TestName1", true);
        testFlight.reserveNextAvailableSeat("TestName2", true);
        testFlight.reserveNextAvailableSeat("TestName3", false);

        assertThat(testFlight.getFrequentFlyers(), containsInAnyOrder("TestName1", "TestName2"));
    }

    @Test
    public void reserveNextAvailableSeat_WhenThereAreAvailableSeats_ShouldReturnTrue() {
        testFlight = new Flight(1, 1);
        assertTrue(testFlight.reserveNextAvailableSeat("TestName1", true));
    }

    @Test
    public void reserveNextAvailableSeat_WhenThereAreAvailableSeats_ShouldReserveTheNextSeatInOrder() {
        testFlight = new Flight(2,2);

        testFlight.reserveNextAvailableSeat("TestName1", true);
        testFlight.reserveNextAvailableSeat("TestName2", true);
        testFlight.reserveNextAvailableSeat("TestName3", false);

        Reservation[][] flightSeats = testFlight.getSeats();

        assertAll(
            () -> assertEquals("TestName1", flightSeats[0][0].getPassengerName()),
            () -> assertEquals("AISLE", flightSeats[0][1].getPassengerName()),
            () -> assertEquals("TestName2", flightSeats[0][2].getPassengerName()),
            
            () -> assertEquals("TestName3", flightSeats[1][0].getPassengerName()),
            () -> assertEquals("AISLE", flightSeats[1][1].getPassengerName()),
            () -> assertNull(flightSeats[1][2])
        );
    }

    @Test
    public void reserveNextAvailableSeat_WhenThereAreNoAvailableSeats_ShouldReturnFalse() {
        testFlight = new Flight(1, 0);
        assertFalse(testFlight.reserveNextAvailableSeat("TestName1", true));
    }

    @Test
    public void reserveAdjacentSeats_WhenThereAreAvailablePairsOfSeats_ShouldReturnTrue() {
        testFlight = new Flight(2, 3);
        assertTrue(testFlight.reserveAdjacentSeats("TestName1", true, "TestName2", false));
    }

    @Test
    public void reserveAdjacentSeats_WhenThereAreAvailablePairsOfSeats_ShouldReserveAdjacentSeats() {
        testFlight = new Flight(2,3);

        testFlight.reserveAdjacentSeats("TestName1", true, "TestName2", false);
        testFlight.reserveAdjacentSeats("TestName3", true, "TestName4", false);
        Reservation[][] flightSeats = testFlight.getSeats();

        assertAll(
            () -> assertEquals("TestName1", flightSeats[0][0].getPassengerName()),
            () -> assertEquals("TestName2", flightSeats[0][1].getPassengerName()),
            () -> assertEquals("AISLE", flightSeats[0][2].getPassengerName()),
            () -> assertNull(flightSeats[0][3]),
            
            () -> assertEquals("TestName3", flightSeats[1][0].getPassengerName()),
            () -> assertEquals("TestName4", flightSeats[1][1].getPassengerName()),
            () -> assertEquals("AISLE", flightSeats[1][2].getPassengerName()),
            () -> assertNull(flightSeats[1][3])
        );
    }

    @Test
    public void reserveAdjacentSeats_WhenThereAreNoAvailablePairsOfSeats_ShouldReturnFalse() {
        testFlight = new Flight(2, 2);
        assertFalse(testFlight.reserveAdjacentSeats("TestName1", true, "TestName2", false));
    }

    @Test
    public void reserveAisleSeat_WhenThereAreAvailableAisleSeats_ShouldReturnTrue() {
        testFlight = new Flight(2,2);
        assertTrue(testFlight.reserveAisleSeat("TestName1", true));
    }

    @Test
    public void reserveAisleSeat_WhenThereAreAvailableAisleSeats_ShouldReserveTheNextSeatInOrder() {
        testFlight = new Flight(2,3);

        testFlight.reserveAisleSeat("TestName1", true);
        testFlight.reserveAisleSeat("TestName2", true);
        testFlight.reserveAisleSeat("TestName3", false);

        Reservation[][] flightSeats = testFlight.getSeats();

        assertAll(
            () -> assertNull(flightSeats[0][0]),
            () -> assertEquals("TestName1", flightSeats[0][1].getPassengerName()),
            () -> assertEquals("AISLE", flightSeats[0][2].getPassengerName()),
            () -> assertEquals("TestName2", flightSeats[0][3].getPassengerName()),
            
            () -> assertNull(flightSeats[1][0]),
            () -> assertEquals("TestName3", flightSeats[1][1].getPassengerName()),
            () -> assertEquals("AISLE", flightSeats[1][2].getPassengerName()),
            () -> assertNull(flightSeats[1][3])
        );
    }

    @Test
    public void reserveAisleSeat_WhenThereAreNoAvailableAisleSeats_ShouldReturnFalse() {
        testFlight = new Flight(1, 0);
        
        assertFalse(testFlight.reserveAisleSeat("TestName1", true));
    }

    @Test
    public void reserveWindowSeat_WhenThereAreAvailableWindowSeats_ShouldReturnTrue() {
        testFlight = new Flight(2,2);
        assertTrue(testFlight.reserveWindowSeat("TestName1", true));
    }

    @Test
    public void reserveWindowSeat_WhenThereAreAvailableWindowSeats_ShouldReserveTheNextSeatInOrder() {
        testFlight = new Flight(2,3);

        testFlight.reserveWindowSeat("TestName1", true);
        testFlight.reserveWindowSeat("TestName2", true);
        testFlight.reserveWindowSeat("TestName3", false);

        Reservation[][] flightSeats = testFlight.getSeats();

        assertAll(
            () -> assertEquals("TestName1", flightSeats[0][0].getPassengerName()),
            () -> assertNull(flightSeats[0][1]),
            () -> assertEquals("AISLE", flightSeats[0][2].getPassengerName()),
            () -> assertEquals("TestName2", flightSeats[0][3].getPassengerName()),
            
            () -> assertEquals("TestName3", flightSeats[1][0].getPassengerName()),
            () -> assertNull(flightSeats[1][1]),
            () -> assertEquals("AISLE", flightSeats[1][2].getPassengerName()),
            () -> assertNull(flightSeats[1][3])
        );
    }

    @Test
    public void reserveWindowSeat_WhenThereAreNoAvailableWindowSeats_ShouldReturnFalse() {
        testFlight = new Flight(1, 0);
        
        assertFalse(testFlight.reserveWindowSeat("TestName1", true));
    }

    @Test
    public void isolatedPassengers_WhenPlaneIsEmpty_ShouldReturnAnEmptyArrayList() {
        testFlight = new Flight(1,1);
        assertThat(testFlight.isolatedPassengers(), IsEmptyCollection.empty());
    }

    @Test
    public void isolatedPassengers_WhenPlaneHasNoIsolatedPassengers_ShouldReturnAnEmptyArrayList() {
        testFlight = new Flight(1, 4);

        Reservation[][] seats = testFlight.getSeats();
        seats[0][0] = new Reservation("TestName1", true);
        seats[0][1] = new Reservation("TestName2", true);
        seats[0][3] = new Reservation("TestName3", false);
        seats[0][4] = new Reservation("TestName4", false);

        assertThat(testFlight.isolatedPassengers(), IsEmptyCollection.empty());
    }

    @Test
    public void isolatedPassengers_WhenPlaneHasIsolatedPassengers_ShouldReturnAnArrayListWithNames(){
        testFlight = new Flight(4,4);

        Reservation[][] seats = testFlight.getSeats();
        seats[0][0] = new Reservation("TestName1", true);
        seats[1][0] = new Reservation("TestName2", true);
        seats[1][1] = new Reservation("TestName3", false);
        seats[2][0] = new Reservation("TestName4", false);

        assertThat(testFlight.isolatedPassengers(), containsInAnyOrder("TestName1", "TestName4"));
    }

    @Test
    public void toString_WhenCalled_ShouldPrintFormattedToTheSpecs() {

        String expected = "TestName1 AISLE EMPTY\nEMPTY AISLE TestName2";
        testFlight = new Flight(2,2);

        Reservation[][] seats = testFlight.getSeats();
        seats[0][0] = new Reservation("TestName1", true);
        seats[1][2] = new Reservation("TestName2", false);
        String result = testFlight.toString();

        assertEquals(expected, result.trim());
    }
}
