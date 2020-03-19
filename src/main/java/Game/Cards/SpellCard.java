package Game.Cards;

import Game.Gamestructure.Hero;

public abstract class SpellCard extends Card{


    public SpellCard(Hero hero, String name, Rarity rarity, int manacost, String description) {
        super(hero, name,description, rarity, manacost);
    }
}
