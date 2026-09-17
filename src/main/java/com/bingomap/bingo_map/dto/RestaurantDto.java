package com.bingomap.bingo_map.dto;


@Getter
@Setter
@NoArgsConstructor
public class RestaurantDto {

    private Long restaurantId;
    private String name;
    private String category;
    private String tags;
    private Double rating;
    private Integer reviewCount;
    private String description;
    private String address;

    }

    public Long getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Double getRating() { return rating; }
    public String getAddress() { return address; }
    public String getMainImageUrl() { return mainImageUrl; }
    public java.math.BigDecimal getLatitude() { return latitude; }
    public java.math.BigDecimal getLongitude() { return longitude; }
    public String getOpeningHours() { return openingHours; }
    public String getPhone() { return phone; }
    public String getPriceRange() { return priceRange; }
    public String getWebsiteUrl() { return websiteUrl; }
    public String getDescription() { return description; }
}
