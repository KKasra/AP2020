package Game.CommandAndResponse;

import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;

public class TargetReceiver {
    private GameProcessor processor;
    public TargetReceiver(GameProcessor processor) {
        this.processor = processor;
    }
    public Object receiveTarget(TargetPicker picker) {
        processor.respond(Response.Message.selectATarget);
       return getValidateTarget(picker);
    }

    public Object discover(List objects) {
        List<Integer> list = new ArrayList<>();
        list.add(1 - processor.getGame().getPlayers().indexOf(processor.getGame().getPlayerOnTurn()));
        processor.sendResponse(new DiscoverRequest(processor.getGame(), objects, list));
        return getValidateTarget(Target -> {
            //TODO check if target is in the list
            return true;
        });
    }

    private synchronized Object getValidateTarget(TargetPicker picker) {
        while (true) {
            Command command = processor.receiveCommand();
            if (!(command instanceof SelectionCommand)) {
                processor.respond(Response.Message.reject);
                continue;
            }
            try {
                processor.validateCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
                //TODO log
                processor.respond(Response.Message.reject);
                continue;

            }
            Object target = SelectionCommand.getTarget(processor.getGame(), (SelectionCommand) command);
            if (!picker.isValid(target)) {
                processor.respond(Response.Message.reject);
                continue;
            }

            processor.respond(Response.Message.targetIsValid);
            processor.notifyObservers(command);
            return target;
        }
    }


}
