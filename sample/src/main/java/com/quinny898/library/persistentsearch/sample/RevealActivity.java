package com.quinny898.library.persistentsearch.sample;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchBox.MenuListener;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

public class RevealActivity extends ActionBarActivity {

	private SearchBox search;
	private Toolbar toolbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reveal);
		getString(R.string.app_name);
		search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		this.setSupportActionBar(toolbar);
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				openSearch();
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public void openSearch() {
		toolbar.setTitle("");
		search.revealFromMenuItem(R.id.action_search, this);
		for (int x = 0; x < 10; x++) {
			SearchResult option = new SearchResult("Result "
					+ Integer.toString(x), getResources().getDrawable(
					R.drawable.ic_history));
			search.addSearchable(option);
		}
		search.setMenuListener(new MenuListener() {

			@Override
			public void onMenuClick() {
				// Hamburger has been clicked
				Toast.makeText(RevealActivity.this, "Menu click",
						Toast.LENGTH_LONG).show();
			}

		});
		search.setSearchListener(new SearchListener() {

			@Override
			public void onSearchOpened() {
				// Use this to tint the screen

			}

			@Override
			public void onSearchClosed() {
				// Use this to un-tint the screen
				closeSearch();
			}

			@Override
			public void onSearchTermChanged(String term) {
				// React to the search term changing
				// Called after it has updated results
			}

			@Override
			public void onSearch(String searchTerm) {
				Toast.makeText(RevealActivity.this, searchTerm + " Searched",
						Toast.LENGTH_LONG).show();
				toolbar.setTitle(searchTerm);

			}

			@Override
			public void onResultClick(SearchResult result) {
				//React to result being clicked
			}

			@Override
			public void onSearchCleared() {
				
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

	protected void closeSearch() {
		search.hideCircularly(this);
		if(search.getSearchText().isEmpty())toolbar.setTitle("");
	}

}
