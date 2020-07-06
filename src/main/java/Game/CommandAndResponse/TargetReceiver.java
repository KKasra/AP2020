package Game.CommandAndResponse;

public class TargetReceiver {
    GameProcessor processor;
    public TargetReceiver(GameProcessor processor) {
        this.processor = processor;
    }
    public Object receiveTarget(TargetPicker picker) {
        processor.respond(Response.Message.selectATarget);
        Object target = null;
        while (true) {
            Command command = processor.receiveCommand();
            if ((!(command instanceof SelectionCommand))) {
                processor.respond(Response.Message.reject);
                continue;
            }
            target = ((SelectionCommand) command).getTarget();
            if (!picker.isValid(target)) {
                processor.respond(Response.Message.reject);
                continue;
            }
            break;
        }
        processor.respond(Response.Message.targetIsValid);
        return target;
    }
}
