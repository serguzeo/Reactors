package Importers;


import Reactors.ReactorsOwner;

import java.io.File;

public abstract class ReactorImporter {
    protected ReactorImporter nextImporter;

    public void setNextImporter(ReactorImporter reactorImporter) {
        this.nextImporter = reactorImporter;
    };

    public abstract void importReactorsFromFile(File file, ReactorsOwner reactorsOwner);
}
