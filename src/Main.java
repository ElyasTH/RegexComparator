
import DataStructures.DFA.DFA;
import DataStructures.NFA.NFA;
import Utils.PostFixGenerator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Regex: ");
        NFA nfa = NFA.fromRegex(scanner.next());
        System.out.println("NFA:");
        NFA.print(nfa);
        DFA dfa = DFA.fromNFA(nfa);
        System.out.println("DFA:");
        dfa.print();
    }
}
