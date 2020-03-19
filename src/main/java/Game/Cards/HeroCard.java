package Game.Cards;

import Game.Gamestructure.Hero;

public abstract class HeroCard extends Card{

    public HeroCard(Hero hero, String name, Rarity rarity, int manaCost, String description) {
        super(hero, name,description, rarity, manaCost);
    }
}
