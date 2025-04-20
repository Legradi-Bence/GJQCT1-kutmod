package roboticoffee.utils;

import java.util.List;

import roboticoffee.states.UIState;

public class CodeWindowNames {
    public static List<String> getCodeWindowNames() {
        return UIState.getCodeWindowNames();
    }
    public static String getCode(String codeWindowName) {
        return UIState.getCode(codeWindowName);
    }

}
