package springresttest.buyaticket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Document(collection = "connection")
public class Connection {
    @Id
    private String connectionId;

    @NotBlank(message = "Please provide a starting city")
    private String cityFrom;

    @NotBlank(message = "Please provide a destination city")
    private String cityTo;

    @NotNull(message = "Please provide a distance")
    private Integer distance;

    @NotNull(message = "Please provide a time")
    private Integer time;

    @NotNull(message = "Please provide a price per km")
    private Double pricePerKm;

    private List<@Valid BusStop> busStops;

    @NotNull(message = "Please provide a schedule")
    private @Valid Schedule schedule;

    public Connection() {
    }

    public Connection(String cityFrom, String cityTo, Integer distance, Integer time, Double pricePerKm,
                      List<BusStop> busStops, Schedule schedule) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.distance = distance;
        this.time = time;
        this.pricePerKm = pricePerKm;
        this.busStops = busStops;
        this.schedule = schedule;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public List<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(List<BusStop> busStops) {
        this.busStops = busStops;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
