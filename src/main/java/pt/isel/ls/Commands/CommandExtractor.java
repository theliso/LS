package pt.isel.ls.Commands;

import pt.isel.ls.Parser.Result;

import java.util.HashMap;
import java.util.Map;

public class CommandExtractor {


    public static Map<String, Result> getParametersExtracted(String param) {
        if (param == null)
            return null;
        return processParameters(param.split("&"));

    }

    private static Map<String, Result> processParameters(String[] param) {
        Map<String, Result> paramsExtractor = new HashMap<>();
        for (String s : param) {
            String[] split = s.split("=");
            putInMap(paramsExtractor, split);
        }
        return paramsExtractor;
    }

    private static void putInMap(Map<String, Result> paramsExtractor, String[] param) {
        final int PARAM_NAME = 0;
        final int PARAM_VALUE = 1;
        if (paramsExtractor.containsKey(param[PARAM_NAME])) {
            paramsExtractor.get(param[PARAM_NAME]).joinList(param[PARAM_VALUE]);
        } else {
            if(param.length == 1) return;
            String key = param[PARAM_NAME].replace("+", " ");
            String value = param[PARAM_VALUE].replace("+", " ");
            paramsExtractor.put(
                    key,
                    new Result(key, value)
            );
        }
    }

}
