package telran.collections;

import java.util.Map;

public record ParenthesesMaps(Map<Character, Character> openCloseMap, Map<Character, Character> closeOpenMap) {
    // openCloseMap - key is opening parentheses, value is closing parentheses
    // closeOpenMap - key is closing parentheses, value is opening parentheses
}
