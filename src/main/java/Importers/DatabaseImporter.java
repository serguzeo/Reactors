package Importers;

import Reactors.Reactor;
import Reactors.ReactorType;
import Reactors.ReactorsTypesOwner;
import Regions.Regions;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseImporter {

    public static Map<String, List<Reactor>> importReactors(File file) throws SQLException {
        String DB_URL = "jdbc:sqlite:" + file.getAbsolutePath();

        ReactorsTypesOwner typesOwner = new ReactorsTypesOwner();
        Map<String, ReactorType> types = typesOwner.getReactorMap();

        Map<String, List<Reactor>> reactorsByCountry = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                // Read reactors data
                String reactorQuery = "SELECT * FROM reactors";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(reactorQuery)) {

                    while (rs.next()) {
                        String name = rs.getString("name");
                        String country = rs.getString("country");
                        ReactorType reactorType = types.get(rs.getString("type"));
                        String owner = rs.getString("owner");
                        String operator = rs.getString("operator");
                        String status = rs.getString("status");
                        Integer thermalCapacity = rs.getInt("thermalCapacity");
                        Integer firstGridConnection = rs.getInt("firstGridConnection");
                        Integer suspendedDate = rs.getInt("suspendedDate");
                        Integer permanentShutdownDate = rs.getInt("permanentShutdownDate");

                        Reactor reactor = new Reactor(name, country, reactorType, owner, operator, status,
                                thermalCapacity, firstGridConnection, suspendedDate, permanentShutdownDate);

                        reactorsByCountry.computeIfAbsent(country, k -> new ArrayList<>()).add(reactor);
                    }
                }

                // Read load_factors data and add to corresponding reactors
                String loadFactorQuery = "SELECT * FROM load_factors";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(loadFactorQuery)) {

                    while (rs.next()) {
                        String name = rs.getString("name");
                        Integer year = rs.getInt("year");
                        Double loadFactor = rs.getDouble("loadFactor");

                        // Find the reactor and add the load factor
                        for (List<Reactor> reactors : reactorsByCountry.values()) {
                            for (Reactor reactor : reactors) {
                                if (reactor.getName().equals(name)) {
                                    reactor.addLoadFactor(year, loadFactor);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            for (List<Reactor> reactors : reactorsByCountry.values()) {
                for (Reactor reactor : reactors) {
                    reactor.fixLoadFactors();
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException(e);
        }

        return reactorsByCountry;
    }

    public static Regions importRegions(File file) throws SQLException {
        Regions regions = new Regions();
        String DB_URL = "jdbc:sqlite:" + file.getAbsolutePath();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String regionsQuery = "SELECT * FROM countries_regions";
                try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(regionsQuery)) {
                    while (rs.next()) {
                        String country = rs.getString("country");
                        String region = rs.getString("region");
                        regions.addCountry(region, country);
                    }
                }
            }
        }

        return regions;
    }
}
