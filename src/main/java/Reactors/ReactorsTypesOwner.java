package Reactors;

import Importers.ReactorTypeImporter;
import Importers.UniversalReactorImporter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class ReactorsTypesOwner {
    private Map<String, ReactorType> reactorsMap;

    public ReactorsTypesOwner() {
        this.reactorsMap = new HashMap<>();

        ReactorTypeImporter reactorImporter = UniversalReactorImporter.getUniversalReactorImporter();
        reactorImporter.importReactorsFromFile(new File("./resources/ReactorType.json"), this);
    }

    public void addReactor(String key, ReactorType reactor) {
        reactorsMap.put(key, reactor);
    }

    public Map<String, ReactorType> getReactorMap() {
        return reactorsMap;
    }
}
