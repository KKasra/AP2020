package GUI.MenuPanels;

public class StatusPanel extends MenuPanel{

    private StatusPanel() {

    }
    private static StatusPanel instance;
    public static StatusPanel getInstance() {
        if(instance == null)
            instance = new StatusPanel();
        return instance;
    }
}
