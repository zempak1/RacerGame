package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private ProgressBar progressBar;
    private int score;

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        isGameStopped = false;
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        finishLine = new FinishLine();
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        roadManager = new RoadManager();
        score = 3500;
        setScore(score);
        setTurnTimer(40);
        drawScene();
    }

    private void drawScene() {
        drawField();
        roadMarking.draw(this);
        roadManager.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
        player.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                if (x == CENTER_X) {
                    setCellColor(CENTER_X, y, Color.WHITESMOKE);
                } else if (x >= ROADSIDE_WIDTH && x < (WIDTH - ROADSIDE_WIDTH)) {
                    setCellColor(x, y, Color.GRAY);
                } else {
                    setCellColor(x, y, Color.GREEN);
                }
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x < 0 || x > WIDTH - 1) {
        } else if (y < 0 || y > HEIGHT - 1) {
        } else {
            super.setCellColor(x, y, color);
        }
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void onTurn(int x) {
        if (roadManager.checkCrush(player)) {
            gameOver();
            drawScene();
        } else if (finishLine.isCrossed(player)) {
            win();
            drawScene();
        } else {

            if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
                finishLine.show();
            }
            score -= 5;
            setScore(score);
            moveAll();
            roadManager.generateNewRoadObjects(this);
            drawScene();
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.RIGHT) {
            player.setDirection(Direction.RIGHT);
        } else if (key == Key.LEFT) {
            player.setDirection(Direction.LEFT);
        } else if (key == Key.SPACE && isGameStopped) {
            createGame();
        } else if (key == Key.UP) {
            player.speed = 2;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.RIGHT && player.getDirection() == Direction.RIGHT) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.LEFT && player.getDirection() == Direction.LEFT) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.UP) {
            player.speed = 1;
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.AQUA, "Game Over", Color.BLACK, 50);
        stopTurnTimer();
        player.stop();

    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.AQUA, "YOU WINNER", Color.BLACK, 50);
        stopTurnTimer();
    }
}
