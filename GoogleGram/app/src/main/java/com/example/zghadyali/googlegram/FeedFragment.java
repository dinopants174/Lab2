package com.example.zghadyali.googlegram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import java.util.ArrayList;

/**
 * The FeedFragment reads any of the links stored in the SQLite database on the user's phone and
 * loads them for the user to see and scroll through. The user can delete images from their feed and
 * can switch back to the searchGoogle fragment to search for more images
 */
public class FeedFragment extends Fragment {

    public Button switchGoogle;
    public Button nextImage;
    public Button prevImage;
    public Button delete;
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
        //initializes the DbHelper to read from a SQLite database on the user's phone and load the
        //saved images in the feed
        DbHelper = new FeedReaderDbHelper(getContext());
        //initializes the WebView and read the links stored in the database and returns them as an
        //ArrayList of strings and loads the first link in the list. If the ArrayList has nothing in
        // it, loads an empty WebView
        View rootView = inflater.inflate(R.layout.feed, container, false);
        web = (WebView) rootView.findViewById(R.id.rendered_image);
        imagesLink = DbHelper.readItemsfromDB();
        image_index = 0;
        if (imagesLink.size() > 0) {
            web.loadUrl(imagesLink.get(image_index));
        }
        else{
            web.loadUrl("about:blank");
        }
        //when the user clicks the next or previous button, loops through the array of image links
        //to show the user the next/previous saved image, wraps around to the first image when the
        //user reaches the end of the array
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
                if (imagesLink.size() > 0) {
                    image_index = image_index - 1;
                    if (image_index < 0) {
                        image_index = imagesLink.size() - 1;
                    }
                    web.loadUrl(imagesLink.get(image_index));
                }
            }
            });
        //when the user clicks on the delete button, loads the WebView with the next image if there
        //is one to see and deletes the link to the previous image from the database and the
        //ArrayList
        delete = (Button) rootView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesLink.size() > 1) {
                    String link = imagesLink.get(image_index);
                    DbHelper.deleteItemtoDB(link);
                    imagesLink.remove(link);
                    web.loadUrl(imagesLink.get(image_index));

                }
                else if (imagesLink.size() == 0){   //loads a blank WebView if deleted all images in
                // feed
                    web.loadUrl("about:blank");
                }
                else{   //deletes the only image in the ArrayList and loads a blank WebView
                    image_index = 0;
                    String link = imagesLink.get(image_index);
                    DbHelper.deleteItemtoDB(link);
                    imagesLink.remove(link);
                    web.loadUrl("about:blank");
                }
            }
        });
        //when the user clicks the search button, initialize a new searchGoogle fragment and use the
        //transitionToFragment method in MainActivity to load the searchGoogle fragment in the
        //container
        switchGoogle = (Button) rootView.findViewById(R.id.switch_google);
        switchGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchGoogleFragment searchGoogle = new SearchGoogleFragment();
                ((MainActivity)getActivity()).transitionToFragment(searchGoogle);
            }
        });

        return rootView;
    }
}
