package com.albanfontaine.go4lunch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponsePlaceDetails {

    @SerializedName("result")
    @Expose
    public Result result;

    public Result getResult() { return result; }


    public class Geometry {

        @SerializedName("location")
        @Expose
        public Location location;

        public Location getLocation() { return location; }
    }

    public class Location {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;

        public Double getLat() { return lat; }

        public Double getLng() { return lng; }
    }

    public class Open {

        @SerializedName("time")
        @Expose
        public String time;

        public String getTime() { return time; }
    }

    public class Close {

        @SerializedName("time")
        @Expose
        public String time;

        public String getTime() { return time; }
    }

    public class OpeningHours {

        @SerializedName("open_now")
        @Expose
        public Boolean openNow;
        @SerializedName("periods")
        @Expose
        public List<Period> periods = null;
        @SerializedName("weekday_text")
        @Expose
        public List<String> weekdayText = null;

        public Boolean getOpenNow() { return openNow; }

        public List<Period> getPeriods() { return periods; }

        public List<String> getWeekdayText() { return weekdayText; }
    }

    public class Period {

        @SerializedName("open")
        @Expose
        public Open open;
        @SerializedName("close")
        @Expose
        public Close close;

        public Open getOpen() { return open; }

        public Close getClose() { return close; }
    }

    public class Photo {

        @SerializedName("photo_reference")
        @Expose
        public String photoReference;

        public String getPhotoReference() { return photoReference; }
    }

    public class AddressComponent {

        @SerializedName("short_name")
        @Expose
        public String shortName;


        public String getShortName() { return shortName; }
    }

    public class Result {
        @SerializedName("address_components")
        @Expose
        public List<AddressComponent> addressComponents = null;
        @SerializedName("international_phone_number")
        @Expose
        public String internationalPhoneNumber;
        @SerializedName("geometry")
        @Expose
        public Geometry geometry;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("opening_hours")
        @Expose
        public OpeningHours openingHours;
        @SerializedName("photos")
        @Expose
        public List<Photo> photos = null;
        @SerializedName("rating")
        @Expose
        public Double rating;
        @SerializedName("website")
        @Expose
        public String website;

        public List<AddressComponent> getAddressComponents() { return addressComponents; }

        public String getInternationalPhoneNumber() { return internationalPhoneNumber; }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getName() {
            return name;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public Double getRating() {
            return rating;
        }

        public String getWebsite() {
            return website;
        }
    }

}
