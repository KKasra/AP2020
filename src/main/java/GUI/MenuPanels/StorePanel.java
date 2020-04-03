package GUI.MenuPanels;

public class StorePanel extends MenuPanel {


    private StorePanel() {

    }


    private static StorePanel instance;
    public static StorePanel getInstance() {
        if (instance == null)
            instance = new StorePanel();
        return instance;
    }

}
