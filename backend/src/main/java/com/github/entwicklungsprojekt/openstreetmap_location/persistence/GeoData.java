package com.github.entwicklungsprojekt.openstreetmap_location.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Holds {@code latitude} and {@code longitude} information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GeoData {

    Double latitude;

    Double longitude;
}
