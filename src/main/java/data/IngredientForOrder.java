package data;

import java.util.ArrayList;

public class IngredientForOrder {
    private String[] ingredients;

    public IngredientForOrder() {
    }

    public IngredientForOrder(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}