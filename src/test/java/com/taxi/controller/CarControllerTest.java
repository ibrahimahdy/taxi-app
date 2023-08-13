package com.taxi.controller;

import com.taxi.domainvalue.EngineType;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import com.taxi.service.car.CarService;
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
import static com.taxi.common.TestFixtures.aCarDO;
import static com.taxi.common.TestFixtures.aCarDTO;
import static com.taxi.common.TestFixtures.aCarWithRatingDO;
import static com.taxi.common.TestFixtures.aCarWithRatingDTO;
import static com.taxi.common.TestFixtures.aDeletedCarDO;
import static com.taxi.common.TestFixtures.anElectricCarDO;
import static com.taxi.common.TestFixtures.anElectricCarDTO;
import static com.taxi.common.TestFixtures.anInvalidCarDTO;
import static com.taxi.common.TestFixtures.asJsonString;
import static com.taxi.common.TestFixtures.rating;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
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
 * Test the car web layer.
 * <p/>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CarController.class)
@WithMockUser(roles = "USER")
class CarControllerTest
{
    private static final String API_CAR = "/v1/cars";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;


    /**
     * Find car by id.
     * Expect status code 200.
     *
     * @throws Exception
     */
    @Test
    void get_car_by_id() throws Exception
    {
        when(carService.find(CAR_ID)).thenReturn(aCarDO());
        var expected = aCarDTO();

        mockMvc.perform(get(API_CAR + "/{carId}", CAR_ID)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Find a non-existing car by id throws an EntityNotFoundException
     * Expect status code 404.
     *
     * @throws Exception
     */
    @Test
    void get_non_existing_car_by_id_not_possible() throws Exception
    {
        var message = "Could not find entity with id: " + CAR_ID;
        when(carService.find(anyLong())).thenThrow(new EntityNotFoundException(message));

        mockMvc.perform(get(API_CAR + "/{carId}", CAR_ID)).
            andDo(print()).
            andExpect(status().isNotFound()).andExpect(content().string(message));
    }


    /**
     * Create a car.
     * Expect status code 201
     *
     * @throws Exception
     */
    @Test
    void create_car() throws Exception
    {
        when(carService.create(any())).thenReturn(aCarDO());
        var expected = aCarDTO();

        mockMvc.perform(post(API_CAR).contentType(APPLICATION_JSON).
            content(asJsonString(aCarDTO()))).
            andDo(print()).
            andExpect(status().isCreated()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Create a car when already one has the same licence plate throws ConstraintsViolationException.
     * Expect status code 400.
     *
     * @throws Exception
     */
    @Test
    void create_car_not_allowed_when_violating_constraint() throws Exception
    {
        when(carService.create(any())).thenThrow(ConstraintsViolationException.class);

        mockMvc.perform(post(API_CAR).contentType(APPLICATION_JSON).
            content(asJsonString(aCarDTO()))).
            andDo(print()).
            andExpect(status().isBadRequest());
    }


    /**
     * Create a car with input violating defined constraint is not allowed.
     * Expect status code 400.
     *
     * @throws Exception
     */
    @Test
    void create_car_not_allowed_with_invalid_input() throws Exception
    {
        Map<String, String> expected = new HashMap<>();
        expected.put("licensePlate", "size must be between 2 and 14");
        expected.put("rating", "rating can not be negative value");
        expected.put("engineType", "Please provide valid engine type!");
        expected.put("seatCount", "Car should have at most 10 seats");

        mockMvc.perform(post(API_CAR).contentType(APPLICATION_JSON).
            content(asJsonString(anInvalidCarDTO()))).
            andDo(print()).
            andExpect(status().isBadRequest()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Delete car by id.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void delete_car() throws Exception
    {
        when(carService.delete(anyLong())).thenReturn(aDeletedCarDO());
        var expected = aCarDTO();
        mockMvc.perform(delete(API_CAR + "/{carId}", CAR_ID)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Update car rating.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void update_rating() throws Exception
    {
        when(carService.updateRating(anyLong(), anyDouble())).thenReturn(aCarWithRatingDO());
        var expected = aCarWithRatingDTO();
        mockMvc.perform(put(API_CAR + "/{carId}", CAR_ID).param("rating", String.valueOf(rating)).contentType(APPLICATION_JSON)).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }


    /**
     * Find cars by engine type.
     * Expect status 200.
     *
     * @throws Exception
     */
    @Test
    void find_by_engine_type() throws Exception
    {
        EngineType type = EngineType.ELECTRIC;
        when(carService.find(type)).thenReturn(List.of(anElectricCarDO()));
        var expected = List.of(anElectricCarDTO());

        mockMvc.perform(get(API_CAR).param("engineType", String.valueOf(type))).
            andDo(print()).
            andExpect(status().isOk()).andExpect(content().string(asJsonString(expected)));
    }

}
