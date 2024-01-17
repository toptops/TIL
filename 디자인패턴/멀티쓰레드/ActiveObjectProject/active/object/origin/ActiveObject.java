package active.object.origin;

public interface ActiveObject {
    Result<String> makeString(int count, char fillchar);
    void displayString(String string);
}
