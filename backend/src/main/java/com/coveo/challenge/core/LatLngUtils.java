package com.coveo.challenge.core;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class LatLngUtils {
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // in kilometers

        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);

        lat1 = toRadians(lat1);
        lat2 = toRadians(lat2);

        double a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1) * cos(lat2) *
                        sin(dLon / 2) * sin(dLon / 2);

        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return earthRadius * c;
    }
}
