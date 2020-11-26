package springresttest.buyaticket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import springresttest.buyaticket.exceptions.DuplicateConnectionException;
import springresttest.buyaticket.jackson.EntityToJson;
import springresttest.buyaticket.model.BusStop;
import springresttest.buyaticket.model.Connection;
import springresttest.buyaticket.model.Schedule;
import springresttest.buyaticket.service.ConnectionService;
import springresttest.buyaticket.service.MyUserDetailsService;
import springresttest.buyaticket.util.JwtUtil;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConnectionController.class)
@WithMockUser
class ConnectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    private EntityToJson entityToJson;

    @BeforeEach
    void setUp() {
        entityToJson = new EntityToJson();
    }

    private Connection generateConnection() {
        BusStop busStop = new BusStop("location", 20, 15);
        List<BusStop> busStops = new ArrayList<>();
        busStops.add(busStop);
        Map<DayOfWeek, List<LocalTime>> connectionSchedule = new HashMap<>();
        List<LocalTime> times = new ArrayList<>();
        times.add(LocalTime.of(10,30));
        connectionSchedule.put(DayOfWeek.of(5), times);
        Schedule schedule = new Schedule(connectionSchedule);
        Connection connection =
                new Connection("from", "to", 100, 90, 0.2, busStops, schedule);
        return connection;
    }

    private List<Connection> generateConnectionList() {
        Connection connection1 = generateConnection();
        Connection connection2 = generateConnection();
        return Arrays.asList(connection1, connection2);
    }

    private String getLocation(Connection connection) {
        BusStop busStop = connection.getBusStops().get(0);
        return busStop.getLocation();
    }

    private Integer getDistanceFromFirstStop(Connection connection) {
        BusStop busStop = connection.getBusStops().get(0);
        return busStop.getDistanceFromFirstStop();
    }

    private Integer getTimeeFromFirstStop(Connection connection) {
        BusStop busStop = connection.getBusStops().get(0);
        return busStop.getTimeFromFirstStop();
    }

    private String getDayName(Connection connection) {
        Schedule schedule = connection.getSchedule();
        Map<DayOfWeek, List<LocalTime>> connectionDailySchedule = schedule.getConnectionDailySchedule();
        DayOfWeek day = connectionDailySchedule.keySet().stream().findFirst().get();
        return day.name();
    }

    private List<String> getTimes(Connection connection) {
        Schedule schedule = connection.getSchedule();
        Map<DayOfWeek, List<LocalTime>> connectionDailySchedule = schedule.getConnectionDailySchedule();
        DayOfWeek day = connectionDailySchedule.keySet().stream().findFirst().get();
        LocalTime time = connectionDailySchedule.get(day).get(0);
        String timeString = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        return Arrays.asList(timeString);
    }

    @Test
    void getAllConnections() throws Exception {
        List<Connection> connections = generateConnectionList();
        given(connectionService.getConnections()).willReturn(connections);

        Connection connection1 = connections.get(0);

        mockMvc.perform(get("/api/connections")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].cityFrom", is(connection1.getCityFrom())))
                .andExpect(jsonPath("$[0].cityTo", is(connection1.getCityTo())))
                .andExpect(jsonPath("$[0].distance", is(connection1.getDistance())))
                .andExpect(jsonPath("$[0].time", is(connection1.getTime())))
                .andExpect(jsonPath("$[0].pricePerKm", is(connection1.getPricePerKm())))
                .andExpect(jsonPath("$[0].busStops", hasSize(1)))
                .andExpect(jsonPath("$[0].busStops[0].location", is(getLocation(connection1))))
                .andExpect(jsonPath("$[0].busStops[0].distanceFromFirstStop",
                        is(getDistanceFromFirstStop(connection1))))
                .andExpect(jsonPath("$[0].busStops[0].timeFromFirstStop",
                        is(getTimeeFromFirstStop(connection1))))
                .andExpect(jsonPath("$[0].schedule.connectionDailySchedule." + getDayName(connection1),
                        is(getTimes(connection1))));

    }

    @Test
    void getOneConnection() throws Exception {
        Connection connection = generateConnection();
        given(connectionService.findById("1")).willReturn(Optional.of(connection));

        mockMvc.perform(get("/api/connections/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.connectionId",is(connection.getConnectionId())))
                .andExpect(jsonPath("$.cityFrom", is(connection.getCityFrom())))
                .andExpect(jsonPath("$.cityTo", is(connection.getCityTo())))
                .andExpect(jsonPath("$.distance", is(connection.getDistance())))
                .andExpect(jsonPath("$.time", is(connection.getTime())))
                .andExpect(jsonPath("$.pricePerKm", is(connection.getPricePerKm())))
                .andExpect(jsonPath("$.busStops", hasSize(1)))
                .andExpect(jsonPath("$.busStops[0].location", is(getLocation(connection))))
                .andExpect(jsonPath("$.busStops[0].distanceFromFirstStop",
                        is(getDistanceFromFirstStop(connection))))
                .andExpect(jsonPath("$.busStops[0].timeFromFirstStop",
                        is(getTimeeFromFirstStop(connection))))
                .andExpect(jsonPath("$.schedule.connectionDailySchedule." + getDayName(connection),
                        is(getTimes(connection))));
    }

    @Test
    void getOneConnection_EntityNotFound() throws Exception {
        given(connectionService.findById("1")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/connections/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message[0]", is("Connection not found")));
    }

    @Test
    void addConnection() throws Exception {
        Connection connection = generateConnection();
        String jsonConnectionString = entityToJson.convertToJson(connection);

        mockMvc.perform(post("/api/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConnectionString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityFrom", is(connection.getCityFrom())))
                .andExpect(jsonPath("$.cityTo", is(connection.getCityTo())))
                .andExpect(jsonPath("$.distance", is(connection.getDistance())))
                .andExpect(jsonPath("$.time", is(connection.getTime())))
                .andExpect(jsonPath("$.pricePerKm", is(connection.getPricePerKm())))
                .andExpect(jsonPath("$.busStops", hasSize(1)))
                .andExpect(jsonPath("$.busStops[0].location", is(getLocation(connection))))
                .andExpect(jsonPath("$.busStops[0].distanceFromFirstStop",
                        is(getDistanceFromFirstStop(connection))))
                .andExpect(jsonPath("$.busStops[0].timeFromFirstStop",
                        is(getTimeeFromFirstStop(connection))))
                .andExpect(jsonPath("$.schedule.connectionDailySchedule." + getDayName(connection),
                        is(getTimes(connection))));
    }

    @Test
    void addConnection_ConnectionWithoutCityFrom() throws Exception{
        Connection connectionWithoutCityFrom = generateConnection();
        connectionWithoutCityFrom.setCityFrom("");

        String jsonUserString = entityToJson.convertToJson(connectionWithoutCityFrom);

        mockMvc.perform(post("/api/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message[0]", is("Please provide a starting city")));
    }

    @Test
    void addConnection_DuplicateConnection() throws Exception{
        Connection connection = generateConnection();

        doThrow(new DuplicateConnectionException("Duplicate connection.")).when(connectionService).saveConnection(any());

        String jsonUserString = entityToJson.convertToJson(connection);

        mockMvc.perform(post("/api/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message[0]", is("Duplicate connection.")));
    }

    @Test
    void updateConnection() throws Exception {
        Connection connection = generateConnection();
        connection.setConnectionId("1");
        Connection updatedConnection = connection;
        updatedConnection.setDistance(connection.getDistance() + 10);

        given(connectionService.findById("1")).willReturn(Optional.of(connection));

        String jsonUpdatedConnectionString = entityToJson.convertToJson(updatedConnection);

        mockMvc.perform(put("/api/connections/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdatedConnectionString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityFrom", is(connection.getCityFrom())))
                .andExpect(jsonPath("$.cityTo", is(connection.getCityTo())))
                .andExpect(jsonPath("$.distance", is(connection.getDistance())))
                .andExpect(jsonPath("$.time", is(connection.getTime())))
                .andExpect(jsonPath("$.pricePerKm", is(connection.getPricePerKm())))
                .andExpect(jsonPath("$.busStops", hasSize(1)))
                .andExpect(jsonPath("$.busStops[0].location", is(getLocation(connection))))
                .andExpect(jsonPath("$.busStops[0].distanceFromFirstStop",
                        is(getDistanceFromFirstStop(connection))))
                .andExpect(jsonPath("$.busStops[0].timeFromFirstStop",
                        is(getTimeeFromFirstStop(connection))))
                .andExpect(jsonPath("$.schedule.connectionDailySchedule." + getDayName(connection),
                        is(getTimes(connection))));
    }

    @Test
    void deleteConnection() throws Exception {
        Connection connection = generateConnection();
        connection.setConnectionId("1");

        given(connectionService.findById("1")).willReturn(Optional.of(connection));

        mockMvc.perform(delete("/api/connections/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.connectionId",is(connection.getConnectionId())))
                .andExpect(jsonPath("$.cityFrom", is(connection.getCityFrom())))
                .andExpect(jsonPath("$.cityTo", is(connection.getCityTo())))
                .andExpect(jsonPath("$.distance", is(connection.getDistance())))
                .andExpect(jsonPath("$.time", is(connection.getTime())))
                .andExpect(jsonPath("$.pricePerKm", is(connection.getPricePerKm())))
                .andExpect(jsonPath("$.busStops", hasSize(1)))
                .andExpect(jsonPath("$.busStops[0].location", is(getLocation(connection))))
                .andExpect(jsonPath("$.busStops[0].distanceFromFirstStop",
                        is(getDistanceFromFirstStop(connection))))
                .andExpect(jsonPath("$.busStops[0].timeFromFirstStop",
                        is(getTimeeFromFirstStop(connection))))
                .andExpect(jsonPath("$.schedule.connectionDailySchedule." + getDayName(connection),
                        is(getTimes(connection))));
    }
}