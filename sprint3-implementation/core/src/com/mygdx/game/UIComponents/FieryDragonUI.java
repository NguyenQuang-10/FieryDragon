package com.mygdx.game.UIComponents;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.Board.Player;
import com.mygdx.game.ChitCards.ITurnManager;

public abstract class FieryDragonUI extends Group {
    protected float waitTimeLeft = 0;
    static float endTurnWaitTime = 1;
    protected ITurnManager turnManager;
    protected Player currentActivePlayer;

    public FieryDragonUI(ITurnManager turnManager) {
        this.turnManager = turnManager;
        currentActivePlayer = turnManager.getActivePlayer();
    }

    public void startTurnChange() {
        this.waitTimeLeft = endTurnWaitTime;
    }

    public void endTurnChange() {
        this.currentActivePlayer = turnManager.getActivePlayer();
    };
    public boolean isWaitingForTurnEnd() {
        return waitTimeLeft > 0;
    }

    static void setEndTurnWaitTime(float waitTime) {
        endTurnWaitTime = waitTime;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!isWaitingForTurnEnd() && currentActivePlayer != turnManager.getActivePlayer()) {
            startTurnChange();
        }

        if (waitTimeLeft > 0 ) {
            waitTimeLeft -= delta;

            if (waitTimeLeft <= 0) {
                waitTimeLeft = 0;
                endTurnChange();
            }
        }
    }

}
