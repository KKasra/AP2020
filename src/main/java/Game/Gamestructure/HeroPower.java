package Game.Gamestructure;

public abstract class HeroPower {
    private int manaCost;
    private Hero hero;
    private int damage;
    public HeroPower(Hero hero, int manaCost){
        this.hero = hero;
        this.manaCost = manaCost;
    }

    public abstract void usePower() throws Exception;

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    void manaCheck() throws Exception{
        if (this.getManaCost() > hero.getPlayer().getMana())
            throw new Exception("insufficient mana !");
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
