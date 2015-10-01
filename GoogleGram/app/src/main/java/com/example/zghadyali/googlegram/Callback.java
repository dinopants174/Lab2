package com.example.zghadyali.googlegram;

import java.util.ArrayList;

/**
 * Callback interface that will be defined when called in searchGoogle fragment, takes as input into
 * tracksCallback() method an ArrayList that will contain all of the links for the images returned
 * from the user's GET request
 */
public interface Callback {
    public void tracksCallback(ArrayList<String> itemLink);
}
