package com.example.delivery.models;

import java.util.ArrayList;
import java.util.List;

public class ApiData {
    public List<Progresses> progresses = new ArrayList<>();

    public List<Progresses> getProgresses() {
        return progresses;
    }

    public static class Progresses {
        public Location location;

        public Location getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "Progresses{" + "Location='" + location + '\'' + '}';
        }

        public static class Location {
            public String name;

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return name;
            }
        }
    }


    public State state;

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ApiData{" +
                "state=" + state +
                '}';
    }

    public class State {
        public String text;

        @Override
        public String toString() {
            return "State{" +
                    "text='" + text + '\'' +
                    '}';
        }

        public String getText() {
            return text;
        }

    }
}

