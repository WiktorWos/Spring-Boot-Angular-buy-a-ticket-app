package springresttest.buyaticket.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TicketType {
    NORMAL_20(2.50, 20, "Normal"),
    NORMAL_50(3.50, 50, "Normal"),
    REDUCED_20(1.50, 20, "Reduced"),
    REDUCED_50(2, 50, "Reduced");

    private final String typeName;
    private final double price;
    private final int duration;

    TicketType(double price, int duration, String typeName) {
        this.price = price;
        this.duration = duration;
        this.typeName = typeName;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("duration")
    public int getDuration() {
        return duration;
    }

    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }
}
