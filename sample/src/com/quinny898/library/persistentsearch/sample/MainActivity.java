package com.quinny898.library.persistentsearch.sample;

import java.util.ArrayList;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchBox.MenuListener;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;
import com.quinny898.library.persistentsearch.SearchResult;

import android.speech.RecognizerIntent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	Boolean isSearch;
	private SearchBox search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		search = (SearchBox) findViewById(R.id.searchbox);
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
			public void onSearchTermChanged() {
				//React to the search term changing
				//Called after it has updated results
			}

			@Override
			public void onSearch(String searchTerm) {
				Toast.makeText(MainActivity.this, searchTerm +" Searched", Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onSearchCleared() {
				//Called when the clear button is clicked
				
			}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1234 && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			search.populateEditText(matches);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void reveal(View v){
		startActivity(new Intent(this, RevealActivity.class));
	}
	
	public void mic(View v) {
		search.micClick(this);
	}
	
}
