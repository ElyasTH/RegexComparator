
import DataStructures.DFA.DFA;
import DataStructures.NFA.NFA;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        NFA nfa = NFA.fromRegex(scanner.next());
        NFA.print(nfa);
        DFA dfa = DFA.fromNFA(nfa);
//        dfa.print();
    }
}
