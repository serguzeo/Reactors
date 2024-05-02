package Importers;

public class UniversalReactorImporter {

    public static ReactorImporter getUniversalReactorImporter() {
        ReactorImporterJSON reactorImporterJSON = new ReactorImporterJSON();
        ReactorImporterXML reactorImporterXML = new ReactorImporterXML();
        ReactorImporterYAML reactorImporterYAML = new ReactorImporterYAML();

        reactorImporterXML.setNextImporter(reactorImporterYAML);
        reactorImporterJSON.setNextImporter(reactorImporterXML);

        return reactorImporterJSON;
    }
}
