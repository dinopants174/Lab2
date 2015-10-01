package com.example.zghadyali.googlegram;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * The SearchGoogleFragment will take the user's input and issue a GET request to the Google Image
 * Search engine and then display the 10 images from the request to the user. THe user can then save
 * images they want to see again later to their feed. The user can also click the View Feed button
 * to see their feed.
 */
public class SearchGoogleFragment extends Fragment {

    public Button searchGoogle;
    public Button nextImage;
    public Button prevImage;
    public Button save;
    public Button switchFeed;
    public EditText input;
    public ArrayList<String> imagesLink = new ArrayList<String>();
    public WebView web;
    public int image_index;
    public FeedReaderDbHelper DbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initializes the DbHelper to write to a SQLite database on the user's phone that
        //will store their saved images to their feed
        DbHelper = new FeedReaderDbHelper(getContext());
        //sets up the input field and the search button for the user to type in what they want to
        // search and renders an empty WebView when the user hasn't searched anything yet
        View rootView = inflater.inflate(R.layout.search_google, container, false);
        input = (EditText) rootView.findViewById(R.id.inputField);
        web = (WebView) rootView.findViewById(R.id.rendered_image);
        web.loadUrl("about:blank");
        searchGoogle = (Button) rootView.findViewById(R.id.searchGoogleButton);
        //on click, gets the user's input, initializes the HTTPHandler which uses the
        //searchGoogleImages method, returns an array that contains all of the image links from the
        //search and loads the first one using the WebView
        searchGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from input field here
                String searchText = input.getText().toString();
                HTTPHandler handler = new HTTPHandler(getActivity());
                handler.searchGoogleImages(searchText, new Callback() {
                    @Override
                    public void tracksCallback(ArrayList<String> itemLink) {    //define
                    //tracksCallback function here to load first image from search into WebView
                        image_index = 0;
                        imagesLink = itemLink;
                        web.loadUrl(imagesLink.get(image_index));
                    }
                });
            }
        });

        //when the user clicks the next or previous button, loops through the array of image links
        //to show the user the next/previous image, wraps around to the first image when the user
        //reaches the end of the array
        nextImage = (Button) rootView.findViewById(R.id.next);
        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesLink.size() > 0) {
                    image_index = image_index + 1;
                    if (image_index >= imagesLink.size()) {
                        image_index = 0;
                    }
                    web.loadUrl(imagesLink.get(image_index));
                }
            }
        });
        prevImage = (Button) rootView.findViewById(R.id.previous);
        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increment image_index and wrap image
                if (imagesLink.size() > 0) {
                    image_index = image_index - 1;
                    if (image_index < 0) {
                        image_index = imagesLink.size() - 1;
                    }
                    web.loadUrl(imagesLink.get(image_index));
                }
            }
        });
        //when the user clicks the save button, use the DbHelper to save that link to the SQLite
        //database
        save = (Button) rootView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DbHelper.addItemtoDB(imagesLink.get(image_index));
            }
        });
        //when the user clicks the feed button, initialize a new feed fragment and use the
        //transitionToFragment method in MainActivity to load the feed fragment in the container
        switchFeed = (Button) rootView.findViewById(R.id.switch_feed);
        switchFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedFragment feed = new FeedFragment();
                ((MainActivity) getActivity()).transitionToFragment(feed);
            }
        });

        return rootView;
    }
}
