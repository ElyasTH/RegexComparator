
import DataStructures.DFA.DFA;
import DataStructures.NFA.NFA;
import Utils.PostFixGenerator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Regex1: ");
        NFA nfa1 = NFA.fromRegex(scanner.next());
        DFA dfa1 = DFA.fromNFA(nfa1);
        System.out.print("Enter Regex2: ");
        NFA nfa2 = NFA.fromRegex(scanner.next());
        DFA dfa2 = DFA.fromNFA(nfa2);
        boolean equal = dfa1.equals(dfa2);

        System.out.println("Regex1 Minimized DFA: ");
        dfa1.printMinimized();
        System.out.println("Regex2 Minimized DFA: ");
        dfa2.printMinimized();

        if (equal) System.out.println("Regex1 & Regex2 are equal.");
        else System.out.println("Regex1 & Regex2 are not equal.");
    }
}
