package GUI;

import GUI.MenuPanels.MenuPanel;

import java.util.ArrayList;
import java.util.List;

public class ChangingPageNotifier {
    private List<MenuPanel> registeredPanels;
    public void update(MenuPanel nextPanel) {
        for (MenuPanel registeredPanel : registeredPanels) {
            registeredPanel.notify(nextPanel);
        }
    }

    public void register(MenuPanel panel) {
        registeredPanels.add(panel);
    }

    private static ChangingPageNotifier instance;
    public static ChangingPageNotifier getInstance() {
        if (instance == null)
            instance = new ChangingPageNotifier();
        return instance;
    }
    private ChangingPageNotifier(){
        registeredPanels = new ArrayList<>();
    }

}
