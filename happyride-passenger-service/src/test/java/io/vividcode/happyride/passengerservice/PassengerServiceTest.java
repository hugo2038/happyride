package io.vividcode.happyride.passengerservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.domain.Passenger;
import io.vividcode.happyride.passengerservice.service.PassengerService;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {TestApplication.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class, EmbeddedPostgresConfiguration.class})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Passenger service")
public class PassengerServiceTest {
  @Autowired
  PassengerService passengerService;

  @Test
  @DisplayName("Create passenger")
  public void testCreatePassenger() {
    CreatePassengerRequest request = buildCreatePassengerRequest();
    Passenger passenger = passengerService.createPassenger(request);
    assertNotNull(passenger.getId());
    assertEquals(1, passenger.getUserAddresses().size());
  }

  @Test
  @DisplayName("Add address to an existing passenger")
  public void testAddAddress() {
    CreatePassengerRequest request = buildCreatePassengerRequest();
    Passenger passenger = passengerService.createPassenger(request);
    CreateUserAddressRequest addressRequest = new CreateUserAddressRequest();
    addressRequest.setName("公司");
    addressRequest.setAddressId("address2");
    passenger = passengerService.addAddress(passenger.getId(), addressRequest);
    assertEquals(2, passenger.getUserAddresses().size());
  }

  private CreatePassengerRequest buildCreatePassengerRequest() {
    CreatePassengerRequest request = new CreatePassengerRequest();
    request.setName("乘客1");
    request.setEmail("p1@example.com");
    request.setMobilePhoneNumber("13800000000");
    CreateUserAddressRequest addressRequest = new CreateUserAddressRequest();
    addressRequest.setName("家");
    addressRequest.setAddressId("address1");
    request.setUserAddresses(Collections.singleton(addressRequest));
    return request;
  }
}