package Game.CommandAndResponse;

public class SelectionCommand extends Command{
    private Object target;

    public SelectionCommand(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "SelectionCommand{" +
                "target=" + target +
                '}';
    }
}
