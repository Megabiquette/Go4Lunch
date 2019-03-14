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

        @SerializedName("day")
        @Expose
        public Integer day;
        @SerializedName("time")
        @Expose
        public String time;

        public Integer getDay() { return day; }

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

        public Open getOpen() { return open; }
    }

    public class Photo {

        @SerializedName("height")
        @Expose
        public Integer height;
        @SerializedName("html_attributions")
        @Expose
        public List<String> htmlAttributions = null;
        @SerializedName("photo_reference")
        @Expose
        public String photoReference;
        @SerializedName("width")
        @Expose
        public Integer width;

        public Integer getHeight() { return height; }

        public List<String> getHtmlAttributions() { return htmlAttributions; }

        public String getPhotoReference() { return photoReference; }

        public Integer getWidth() { return width; }
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
        @SerializedName("formatted_phone_number")
        @Expose
        public String formattedPhoneNumber;
        @SerializedName("geometry")
        @Expose
        public Geometry geometry;
        @SerializedName("icon")
        @Expose
        public String icon;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("opening_hours")
        @Expose
        public OpeningHours openingHours;
        @SerializedName("photos")
        @Expose
        public List<Photo> photos = null;
        @SerializedName("place_id")
        @Expose
        public String placeId;
        @SerializedName("rating")
        @Expose
        public Double rating;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("website")
        @Expose
        public String website;

        public List<AddressComponent> getAddressComponents() {
            return addressComponents;
        }

        public String getFormattedPhoneNumber() {
            return formattedPhoneNumber;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getIcon() {
            return icon;
        }

        public String getId() {
            return id;
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

        public String getPlaceId() {
            return placeId;
        }

        public Double getRating() {
            return rating;
        }

        public String getUrl() {
            return url;
        }

        public String getWebsite() {
            return website;
        }
    }

}
