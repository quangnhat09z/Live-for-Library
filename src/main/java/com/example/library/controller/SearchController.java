package com.example.library.controller;

import javafx.scene.control.Alert;

public abstract class SearchController extends Controller {
    protected abstract void handleSearchButton();
    protected abstract void handleDeleteButton();
    protected abstract void handleChangeInfoButton();
    protected abstract void handleChangeToManageButton();
    protected abstract void handleChangeButton();
}
