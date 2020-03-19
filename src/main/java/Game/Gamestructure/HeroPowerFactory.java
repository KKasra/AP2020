package Game.Gamestructure;

public class HeroPowerFactory {
    public static HeroPower build(String name, Hero hero) {
        HeroPower res;
        switch (name) {
            case "FireBlast" :
                res = new HeroPower(hero, 2) {
                    @Override
                    public void usePower() throws Exception{
                        manaCheck();

                        Object target =  hero.getPlayer().getUserInterface().read();
                        if (target instanceof Attackable)
                            ((Attackable)target).receiveDamage(1);
                        else
                            throw new Exception("not That one");
                    }
                };
                break;
            case "Rogue's Power :|":
                res = new HeroPower(hero, 3) {
                    @Override
                    public void usePower() throws Exception {
                        manaCheck();
                        //TODO
                    }
                };
            //TODO
            break;
        }
        return null;
    }
}
