package com.spider.routes.dto;

import java.util.List;

public class RouteDto {
    private List<Route> routes;
    private List<String> unservicedOrderIds;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<String> getUnservicedOrderIds() {
        return unservicedOrderIds;
    }

    public void setUnservicedOrderIds(List<String> unservicedOrderIds) {
        this.unservicedOrderIds = unservicedOrderIds;
    }

    public static class Route {
        private String vehicleId;
        private List<Visit> visits;
        private List<TravelData> travelData;
        private List<Geometry> geometries;

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public List<Visit> getVisits() {
            return visits;
        }

        public void setVisits(List<Visit> visits) {
            this.visits = visits;
        }

        public List<TravelData> getTravelData() {
            return travelData;
        }

        public void setTravelData(List<TravelData> travelData) {
            this.travelData = travelData;
        }

        public List<Geometry> getGeometries() {
            return geometries;
        }

        public void setGeometries(List<Geometry> geometries) {
            this.geometries = geometries;
        }
    }

    public static class Visit {
        private String type;
        private String orderId;
        private String startTime;
        private String duration;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }

    public static class TravelData {
        private String travelTime;
        private double distance;
        private String waitingTime;

        public String getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(String travelTime) {
            this.travelTime = travelTime;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getWaitingTime() {
            return waitingTime;
        }

        public void setWaitingTime(String waitingTime) {
            this.waitingTime = waitingTime;
        }
    }

    public static class Geometry {
        private String type;
        private List<Integer> bbox;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getBbox() {
            return bbox;
        }

        public void setBbox(List<Integer> bbox) {
            this.bbox = bbox;
        }
    }
}
