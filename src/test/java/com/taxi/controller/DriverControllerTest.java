package com.taxi.controller;

import com.taxi.controller.specification.DriverSpecification;
import com.taxi.domainvalue.OnlineStatus;
import com.taxi.exception.CarAlreadyInUseException;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import com.taxi.service.driver.DriverService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.taxi.common.TestFixtures.CAR_ID;
import static com.taxi.common.TestFixtures.DRIVER_ID;
import static com.taxi.common.TestFixtures.aDriverDO;
import static com.taxi.common.TestFixtures.aDriverDTO;
import static com.taxi.common.TestFixtures.aDriverWithCarDO;
import static com.taxi.common.TestFixtures.aDriverWithCarDTO;
import static com.taxi.common.TestFixtures.aDriverWithCoordinateDO;
import static com.taxi.common.TestFixtures.aDriverWithCoordinateDTO;
import static com.taxi.common.TestFixtures.anInvalidDriverDTO;
import static com.taxi.common.TestFixtures.anOnlineDriverDO;
import static com.taxi.common.TestFixtures.asJsonString;
import static com.taxi.common.TestFixtures.latitude;
import static com.taxi.common.TestFixtures.longitude;
import static com.taxi.common.TestFixtures.username;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the driver web layer.
 * <p/>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(DriverController.class)
@WithMockUser(roles = "USER")
class DriverControllerTest
{
    private static final String API_DRIVER = "/v1/drivers";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DriverService driverService;

    /**
     * Find driver by id.
     * Expect status code 200.
     *
     * @throws Exception
     */
    @Test
    void get_driver_by_id() throws Exception
    {
        when(driverService.find(DRIVER_ID)).thenReturn(aDriverDO());
        var expected = aDriverDTO();

        mockMvc.perform(get(API_DRIVER + "/{driverId}", DRIVER_ID)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));

    }


    /**
     * Find a non-existing driver by id throws an EntityNotFoundException
     * Expect status code 404.
     *
     * @throws Exception
     */
    @Test
    void get_non_existing_driver_by_id() throws Exception
    {
        var message = "Could not find entity with id: " + DRIVER_ID;
        when(driverService.find(anyLong())).thenThrow(new EntityNotFoundException(message));

        mockMvc.perform(get(API_DRIVER + "/{driverId}", DRIVER_ID)).
            andDo(print()).
            andExpect(status().isNotFound()).andExpect(content().string(message));
    }


    /**
     * Create a driver.
     * Expect status code 201
     *
     * @throws Exception
     */
    @Test
    void create_driver() throws Exception
    {
        when(driverService.create(any())).thenReturn(aDriverDO());
        var expected = aDriverDTO();

        mockMvc.perform(post(API_DRIVER).contentType(APPLICATION_JSON).
            content(asJsonString(aDriverDTO()))).
            andDo(print()).
            andExpect(status().isCreated()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Create a driver when already one has the same username throws a ConstraintsViolationException.
     * Expect status code 400.
     *
     * @throws Exception
     */
    @Test
    void create_driver_not_allowed_when_violating_constraint() throws Exception
    {
        when(driverService.create(any())).thenThrow(ConstraintsViolationException.class);

        mockMvc.perform(post(API_DRIVER).contentType(APPLICATION_JSON).
            content(asJsonString(aDriverDTO()))).
            andDo(print()).
            andExpect(status().isBadRequest());
    }


    /**
     * Create a driver with input violating defined constraint is not allowed.
     * Expect status code 400.
     *
     * @throws Exception
     */
    @Test
    void create_driver_not_allowed_with_invalid_input() throws Exception
    {
        Map<String, String> expected = new HashMap<>();
        expected.put("username", "Username can not be null!");
        expected.put("password", "Password can not be null!");

        mockMvc.perform(post(API_DRIVER).contentType(APPLICATION_JSON).
            content(asJsonString(anInvalidDriverDTO()))).
            andDo(print()).
            andExpect(status().isBadRequest()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Delete driver by id.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void delete_driver() throws Exception
    {
        doNothing().when(driverService).delete(anyLong());
        mockMvc.perform(delete(API_DRIVER + "/{driverId}", DRIVER_ID)).
            andDo(print()).
            andExpect(status().isNoContent());
    }


    /**
     * Update driver location.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void update_location() throws Exception
    {
        when(driverService.updateLocation(anyLong(), anyDouble(), anyDouble())).thenReturn(aDriverWithCoordinateDO());
        var expected = aDriverWithCoordinateDTO();
        mockMvc.perform(put(API_DRIVER + "/{driverId}", DRIVER_ID).param("longitude", String.valueOf(longitude))
            .param("latitude", String.valueOf(latitude))
            .contentType(APPLICATION_JSON)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Find drivers by online status.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void find_by_online_status() throws Exception
    {
        OnlineStatus status = OnlineStatus.ONLINE;
        when(driverService.find(status)).thenReturn(List.of(anOnlineDriverDO()));
        var expected = List.of(aDriverDTO());

        mockMvc.perform(get(API_DRIVER).param("onlineStatus", String.valueOf(status))).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Select a car for driver.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void select_car_for_driver() throws Exception
    {
        when(driverService.selectCar(anyLong(), anyLong())).thenReturn(aDriverWithCarDO());
        var expected = aDriverWithCarDTO();

        mockMvc.perform(put(API_DRIVER + "/{driverId}/cars/select/{carId}", DRIVER_ID, CAR_ID).contentType(APPLICATION_JSON)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Select an available car for driver throws CarAlreadyInUseException.
     * Expect status 409.
     *
     * @throws Exception
     */
    @Test
    void select_unavailable_car_for_driver_not_possible() throws Exception
    {
        when(driverService.selectCar(anyLong(), anyLong())).thenThrow(CarAlreadyInUseException.class);

        mockMvc.perform(put(API_DRIVER + "/{driverId}/cars/select/{carId}", DRIVER_ID, CAR_ID).contentType(APPLICATION_JSON)).
            andDo(print()).
            andExpect(status().isConflict());
    }


    /**
     * Deselect a car for driver.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void deselect_car() throws Exception
    {
        when(driverService.deselectCar(anyLong())).thenReturn(aDriverDO());
        var expected = aDriverDTO();

        mockMvc.perform(delete(API_DRIVER + "/{driverId}/cars/deselect", DRIVER_ID).contentType(APPLICATION_JSON)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Find all drivers with driver and car attributes and operations defined in DriverSpecification.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void search_drivers() throws Exception
    {
        DriverSpecification params = new DriverSpecification();
        params.setUsername(username);
        when(driverService.search(any())).thenReturn(List.of(aDriverDO()));
        var expected = List.of(aDriverDTO());

        mockMvc.perform(get(API_DRIVER + "/search").contentType(APPLICATION_JSON).content(asJsonString(params))).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }
}
