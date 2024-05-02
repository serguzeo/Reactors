package Importers;

import Reactors.Reactor;
import Reactors.ReactorsOwner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ReactorImporterJSON extends ReactorImporter{

    @Override
    public void importReactorsFromFile(File file, ReactorsOwner reactorsOwner) {
        if (file.getName().endsWith(".json")) {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(file);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            for (Iterator<String> fieldNameIterator = rootNode.fieldNames(); fieldNameIterator.hasNext(); ) {
                String fieldName = fieldNameIterator.next();
                JsonNode reactorNode = rootNode.get(fieldName);
                if (reactorNode != null && reactorNode.isObject()) {
                    Reactor reactor = parseReactorFromJsonNode(reactorNode);
                    reactorsOwner.addReactor(fieldName, reactor);
                }
            }
        } else if (nextImporter != null) {
            nextImporter.importReactorsFromFile(file, reactorsOwner);
        } else {
            System.out.println("Неподдерживаемый формат");
        }
    }

    private Reactor parseReactorFromJsonNode(JsonNode reactorNode) {
        return new Reactor (
                reactorNode.has("type") ? reactorNode.get("type").asText() : null,
                reactorNode.has("class") ? reactorNode.get("class").asText() : null,
                reactorNode.has("burnup") ? reactorNode.get("burnup").asDouble() : 0.0,
                reactorNode.has("kpd") ? reactorNode.get("kpd").asDouble() : 0.0,
                reactorNode.has("enrichment") ? reactorNode.get("enrichment").asDouble() : 0.0,
                reactorNode.has("termal_capacity") ? reactorNode.get("termal_capacity").asDouble() : 0.0,
                reactorNode.has("electrical_capacity") ? reactorNode.get("electrical_capacity").asDouble() : 0.0,
                reactorNode.has("life_time") ? reactorNode.get("life_time").asInt() : 0,
                reactorNode.has("first_load") ? reactorNode.get("first_load").asDouble() : 0.0,
                "JSON"
        );
    }
}
