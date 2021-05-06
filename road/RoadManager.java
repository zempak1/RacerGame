package com.javarush.games.racer.road;

import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {

        if (type == RoadObjectType.THORN) {
            return new Thorn(x, y);
        } else if (type == RoadObjectType.DRUNK_CAR) {
          return new MovingCar(x, y);
        }

        else {
            return new Car(type, x, y);
        }
    }

    private void addRoadObject(RoadObjectType type, Game game) {

        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        if (createRoadObject(type, x, y) != null && isRoadSpaceFree(createRoadObject(type, x, y)) == true) {
            items.add(createRoadObject(type, x, y));
        }
    }

    public void draw(Game game) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(game);
        }
    }

    public void move(int boost) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).move(boost + items.get(i).speed, items);
        }
        deletePassedItems();
    }

    private boolean isThornExists () {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Thorn) {
                return true;
            }
        }
        return false;
    }

    private boolean isMovingCarExists () {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof MovingCar) {
                return true;
            }
        }
        return false;
    }

    private void generateThorn (Game game) {
        int x = game.getRandomNumber(100);
        if (x < 10 && isThornExists() == false) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    public void generateNewRoadObjects (Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void generateMovingCar (Game game) {
        int x = game.getRandomNumber(100);
        if (x < 10 && isMovingCarExists() == false) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    private void deletePassedItems () {
        List<RoadObject> itemsCopy = new ArrayList<>(items);

        for (RoadObject object: itemsCopy) {
            if (object.y >= RacerGame.HEIGHT) {
                items.remove(object);
                if (object instanceof Thorn == false) {
                    passedCarsCount++;
                }
            }
        }
    }

    public boolean checkCrush (PlayerCar car) {

        for (RoadObject obj: items) {
            if (obj.isCollision(car) == true) {
                return true;
            }
        }
        return false;
    }

    private void generateRegularCar (Game game) {
        int x = game.getRandomNumber(100);
        int carTypeNumber = game.getRandomNumber(4);

        if (x < 25) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    private boolean isRoadSpaceFree (RoadObject object) {
        for (RoadObject obj: items) {
            if (obj.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE) == true) {
                return false;
            }
        }
        return true;
    }

    public int getPassedCarsCount() {
        return passedCarsCount;
    }
}
