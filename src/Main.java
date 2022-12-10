public class Main {

    public static void main(String[] args) throws Exception {
        String input = args[0];
        SHA256 hasher = new SHA256();
        hasher.computeHash(input);
    }
}