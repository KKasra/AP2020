package GUI.MenuPanels;



public class CollectionPanel extends MenuPanel{
    private CollectionPanel() {

    }
    private static CollectionPanel instance;
    public static CollectionPanel getInstance() {
        if (instance == null)
            instance = new CollectionPanel();
        return instance;
    }


}
