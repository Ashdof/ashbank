package com.ashbank.objects.scenes.dashboard.deletescenes;

/**
 * A model for the Delete Transaction object scene
 *
 * @author Emmanuel Amoaful Enchill
 */

import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

import java.util.logging.Logger;

public class BankAccountsDeleteScene {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(TransactionDeleteScene.class.getName());
    private Scene bankAccountsDeleteScene;

    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final SceneController sceneController;

    /**
     * Create a new object to represent the delete scene
     * @param sceneController the scene
     */
    public BankAccountsDeleteScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Delete Scene
     * @return the delete scene
     */
    public Scene getBankAccountsDeleteScene() {
        return this.bankAccountsDeleteScene;
    }

    /**
     * Root Scene:
     * create the root node for the delete scene
     * @param accountsID the ID of the bank account
     * @return a scroll pane object
     */
    public ScrollPane getBankAccountsDeleteRoot(String accountsID) {
    }
}
