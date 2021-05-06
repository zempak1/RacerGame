package com.javarush.games.racer;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

public class GameObject {
    public int x;
    public int y;
    public int width;
    public int height;
    public int[][] matrix;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GameObject(int x, int y, int[][] matrix) {
        this.x = x;
        this.y = y;
        this.matrix = matrix;
        width = matrix[0].length;
        height = matrix.length;
    }

    public void draw(Game game) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int colorIndex = matrix[j][i];
                game.setCellColor(x + i, y + j, Color.values()[colorIndex]);
            }
        }
    }               // Есди координаты тачки за пределами игровых объектов, то столкновение невозможно

    public boolean isCollisionPossible(GameObject otherGameObject) {  //Координаты x и y в данном случае = координаты тачки
        if (x > otherGameObject.x + otherGameObject.width || x + width < otherGameObject.x) {
            return false;
        }

        if (y > otherGameObject.y + otherGameObject.height || y + height < otherGameObject.y) {
            return false;
        }
        return true;
    }

    public boolean isCollision(GameObject gameObject) {      //Если столкновение невозможно, метод ничего не проверяет
        if (!isCollisionPossible(gameObject)) {
            return false;
        }

        for (int carX = 0; carX < gameObject.width; carX++) {
            for (int carY = 0; carY < gameObject.height; carY++) {
                if (gameObject.matrix[carY][carX] != 0) {           //отбрасывает пустые ячейки матричы тачки
                    if (isCollision(carX + gameObject.x, carY + gameObject.y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCollision(int x, int y) {
        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                if (matrix[matrixY][matrixX] != 0 && matrixX + this.x == x && matrixY + this.y == y) { // Координаты объектов сравнивают с координатами тачки на пересечение
                    return true;
                }
            }
        }
        return false;
    }
}