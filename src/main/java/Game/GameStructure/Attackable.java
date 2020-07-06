package Game.GameStructure;

public interface Attackable {
    //return true if hp is less then the amount of damage
    void receiveDamage(int amount);
    void die();
    Player getPlayer();
}
