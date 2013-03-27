package commandline.parsing;

import commandline.parsing.parser.Parser;

/**
 * Hello world!
 */
public class App {
    public void setTest(int test) {
        this.test = test;
    }

    public int getTest() {
        return test;
    }

    private int test;

    public static void main(String[] args) throws Exception {
        App a = new App();
        System.out.println(a.getTest());
        Parser.parse(a);
        System.out.println(a.getTest());
    }
}
