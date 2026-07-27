package ca.uwaterloo.watform.bar;

public class Main {
    public static void main(String args[]) {
        System.out.println("hello, this is a successful test from foo submodule");
    }

    // this is supposed to be broken
    NonExistentClass t = new NonExistentClass();
}
