package com.example.library.controller.search;

import com.example.library.controller.mainScreen.Controller;

public abstract class SearchController extends Controller {
    protected abstract void handleSearchButton();
    protected abstract void handleDeleteButton();
    protected abstract void handleChangeInfoButton();
    protected abstract void handleChangeToManageButton();
    protected abstract void handleChangeButton();
}
