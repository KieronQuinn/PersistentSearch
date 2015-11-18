package com.quinny898.library.persistentsearch.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchBox.MenuListener;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

public class MainActivity extends Activity {
	Boolean isSearch;
	private SearchBox search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
		for(int x = 0; x < 10; x++){
			SearchResult option = new SearchResult("Result " + Integer.toString(x), getResources().getDrawable(R.drawable.ic_history));
			search.addSearchable(option);
		}
		search.setMenuListener(new MenuListener(){

			@Override
			public void onMenuClick() {
				//Hamburger has been clicked
				Toast.makeText(MainActivity.this, "Menu click", Toast.LENGTH_LONG).show();				
			}
			
		});
		search.setSearchListener(new SearchListener(){

			@Override
			public void onSearchOpened() {
				//Use this to tint the screen
			}

			@Override
			public void onSearchClosed() {
				//Use this to un-tint the screen
			}

			@Override
			public void onSearchTermChanged(String term) {
				//React to the search term changing
				//Called after it has updated results
			}

			@Override
			public void onSearch(String searchTerm) {
				Toast.makeText(MainActivity.this, searchTerm +" Searched", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResultClick(SearchResult result) {
				//React to a result being clicked
			}

			@Override
			public void onSearchCleared() {
				//Called when the clear button is clicked
			}
			
		});
        search.setOverflowMenu(R.menu.overflow_menu);
        search.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test_menu_item:
                        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1234 && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			search.populateEditText(matches.get(0));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void reveal(View v){
		startActivity(new Intent(this, RevealActivity.class));
	}

	
}
