package com.example.library.controller;

public abstract class ManageController extends Controller {
    protected abstract void handleAddButton();
    protected abstract void handleShowButton();
    protected abstract void handleDeleteButton();
    protected abstract void handleSearchButton();
    protected abstract void handleChangeButton();
}
