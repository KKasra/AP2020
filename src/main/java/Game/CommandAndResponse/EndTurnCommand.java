package Game.CommandAndResponse;

public class EndTurnCommand extends Command {
    private Object producer;
    public EndTurnCommand(Object producer) {
        this.producer = producer;
    }

    public Object getProducer() {
        return producer;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
