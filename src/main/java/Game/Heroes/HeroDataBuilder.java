package Game.Heroes;

import Game.Cards.Deck;

import java.util.ArrayList;

public class HeroDataBuilder {
    HeroData res = new HeroData();

    HeroDataBuilder withName(String name) {
        res.setHeroName(name);
        return this;
    }
    HeroDataBuilder withHp(int hp) {
        res.setHp(hp);
        return this;
    }
    HeroDataBuilder withPower(String power) {
        res.setPower(power);
        return this;
    }
    HeroData build(){
        return res;
    }
}
