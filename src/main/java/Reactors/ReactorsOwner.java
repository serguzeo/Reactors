package Reactors;

import java.util.HashMap;
import java.util.Map;


public class ReactorsOwner {
    private Map<String, Reactor> reactorsMap;

    public ReactorsOwner() {
        this.reactorsMap = new HashMap<>();
    }

    public void addReactor(String key, Reactor reactor) {
        reactorsMap.put(key, reactor);
    }

    public Map<String, Reactor> getReactorMap() {
        return reactorsMap;
    }
}
