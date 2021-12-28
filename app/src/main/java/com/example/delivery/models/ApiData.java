package com.example.delivery.models;

import java.util.ArrayList;
import java.util.List;

public class ApiData {
    public List<Progresses> progresses = new ArrayList<>();
    public List<Progresses> getProgresses() { return progresses; }
    @Override
    public String toString() { return "Progresses{" + "progresses=" + progresses + '}'; }
    public class Progresses{
        public Location location;
        public Location getLocation() {return location;}
        @Override
        public String toString() { return "Progresses{" + "Location='" + location + '\'' + '}'; }

        public class Location{
            public String name;
            public String getName() { return name; }
            @Override
            public String toString() { return name; }
        }
    }
}

