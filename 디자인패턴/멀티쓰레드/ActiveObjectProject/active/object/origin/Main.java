package active.object.origin;

public class Main {
    public static void main(String args[]) {
        // 구독자
        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();

        // 발행자
        new MakerClientThread("Alice", activeObject).start();
        new MakerClientThread("Bobby", activeObject).start();
        new DisplayClientThread("Chris", activeObject).start();
    }
}
