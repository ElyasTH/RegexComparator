package Utils;

import java.util.Stack;

public class PostFixGenerator {

    public static String generate(String regex){
        regex = addDot(regex) + "#";

        Stack<Character> s = new Stack<>();
        char ch = '#', ch1, op;
        s.push(ch);
        String out_string = "";
        int read_location = 0;
        ch = regex.charAt(read_location++);
        while (!s.empty()) {
            if (Character.isLetter(ch)) {
                out_string = out_string + ch;
                ch = regex.charAt(read_location++);
            } else {
                ch1 = s.peek();
                if (isp(ch1) < icp(ch)) {
                    s.push(ch);
                    ch = regex.charAt(read_location++);
                } else if (isp(ch1) > icp(ch)) {
                    op = s.pop();
                    out_string = out_string + op;
                } else {
                    op = s.pop();
                    if (op == '(')
                        ch = regex.charAt(read_location++);
                }
            }
        }
        System.out.println("postfix:" + out_string);
        System.out.println();
        return out_string;
    }

    private static String addDot(String regex){
        int length = regex.length();
        if(length==1) {
            return regex;
        }
        int return_string_length = 0;
        char[] return_string = new char[2 * length + 2];
        char first, second = '0';
        for (int i = 0; i < length - 1; i++) {
            first = regex.charAt(i);
            second = regex.charAt(i + 1);
            return_string[return_string_length++] = first;
            if (first != '(' && first != '+' && Character.isLetter(second)) {
                return_string[return_string_length++] = '.';
            }
            else if (second == '(' && first != '+' && first != '(') {
                return_string[return_string_length++] = '.';
            }
        }
        return_string[return_string_length++] = second;
        String rString = new String(return_string, 0, return_string_length);
        System.out.println();
        return rString;
    }

    private static int isp(char c) {
        return switch (c) {
            case '#' -> 0;
            case '(' -> 1;
            case '*' -> 7;
            case '.' -> 5;
            case '+' -> 3;
            case ')' -> 8;
            default -> -1;
        };
    }

    private static int icp(char c) {
        return switch (c) {
            case '#' -> 0;
            case '(' -> 8;
            case '*' -> 6;
            case '.' -> 4;
            case '+' -> 2;
            case ')' -> 1;
            default -> -1;
        };
    }
}
