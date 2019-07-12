package pt.isel.ls.Parser;


import pt.isel.ls.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Result {

    private Map<String, Pair<String, Integer>> map = new HashMap<>();
    private List<String> list = new ArrayList<>();
    private int index = 0;

    public Result() {
    }

    public Result(String key, String value) {
        map.put(key, new Pair<>(value, index));
        list.add(value);
        ++index;
    }

    public void joinList(String value) {
        list.add(value);
    }

    public String getString(String key) {
        if (map.containsKey(key)) {
            return list.get(map.get(key).getR());
        }
        return null;
    }

    public String getString(int idx) {
        return list.get(idx);
    }

    public int getInt(int idx) {
        return parseInt(list.get(idx));
    }

    public int getInt(String key) {
        if (map.containsKey(key))
            return parseInt(list.get(map.get(key).getR()));
        return -1;
    }

    public List<String> getList() {
        return list;
    }

}
