package src.Exceptions;
//https://www.javatpoint.com/custom-exception#:~:text=In%20Java%2C%20we%20can%20create,exception%20according%20to%20user%20need.
public class UserNotInEntry extends Exception {
    public UserNotInEntry(String message) {
        super(message);
    }
}
