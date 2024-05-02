package Importers;

import Reactors.Reactor;
import Reactors.ReactorsOwner;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class ReactorImporterYAML extends ReactorImporter {

    @Override
    public void importReactorsFromFile(File file, ReactorsOwner reactorsOwner) {
        if (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml")) {
            try {
                Yaml yaml = new Yaml();
                FileInputStream inputStream = new FileInputStream(file);
                Iterable<Object> objects = yaml.loadAll(inputStream);
                for (Object object : objects) {
                    Map<String, ?> map = (Map<String, ?>) object;
                    for (String key : map.keySet()) {
                        Map<?, ?> innerMap = (Map<?, ?>) map.get(key);
                        Reactor reactor = parseReactorFromDict(innerMap);
                        reactorsOwner.addReactor(key, reactor);
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (nextImporter != null) {
            nextImporter.importReactorsFromFile(file, reactorsOwner);
        } else {
            System.out.println("Неподдерживаемый формат");
        }
    }

    private Reactor parseReactorFromDict(Map<?, ?> innerMap) {
        return new Reactor(
                (String) innerMap.get("type"),
                (String) innerMap.get("class"),
                ((Number) innerMap.get("burnup")).doubleValue(),
                ((Number) innerMap.get("kpd")).doubleValue(),
                ((Number) innerMap.get("enrichment")).doubleValue(),
                ((Number) innerMap.get("termal_capacity")).doubleValue(),
                ((Number) innerMap.get("electrical_capacity")).doubleValue(),
                (Integer) innerMap.get("life_time"),
                ((Number) innerMap.get("first_load")).doubleValue(),
                "YAML"
        );
    }
}
