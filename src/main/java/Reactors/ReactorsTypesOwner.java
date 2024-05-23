package Reactors;

import Importers.ReactorTypeImporter;
import Importers.ReactorTypeImporterJSON;
import Importers.UniversalReactorImporter;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class ReactorsTypesOwner {
    private Map<String, ReactorType> reactorsMap;

    public ReactorsTypesOwner() {
        this.reactorsMap = new HashMap<>();

        ReactorTypeImporterJSON reactorImporter = new ReactorTypeImporterJSON();

        try (InputStream inputStream = getClass().getResourceAsStream("/ReactorType.json")) {
            if (inputStream == null) {
                System.out.println("Файл ReactorType.json не найден внутри JAR");
            } else {
                reactorImporter.importReactorsFromStream(inputStream, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addReactor(String key, ReactorType reactor) {
        reactorsMap.put(key, reactor);
    }

    public Map<String, ReactorType> getReactorMap() {
        return reactorsMap;
    }
}
