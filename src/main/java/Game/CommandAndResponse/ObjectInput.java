package Game.CommandAndResponse;

public interface ObjectInput {
    //reads the next Object from the stream
    Object read();
    // reads but does not remove the object from the stream
    Object observe();
    //return true if the Stream is empty
    boolean isEmpty();
}
