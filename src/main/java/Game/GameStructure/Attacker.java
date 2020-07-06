package Game.GameStructure;

public interface Attacker {
    void setDamge(int damage);
    int getDamage();
    void attack(Attackable attackable);
    Player getPlayer();
}
