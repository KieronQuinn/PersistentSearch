# Android Persistent Search Library

A library that implements the persistent search bar seen on apps such as Google Now, Google Maps and Google Play

## Dependencies
material-menu: https://github.com/balysv/material-menu

## Usage

Import it as a library project

In your layout:
```
<com.quinny898.library.persistentsearch.SearchBox
        android:layout_width="wrap_content"
		android:layout_height="wrap_content"
        android:id="@+id/searchbox"
        />
```

**Absolute requirements in the activity code**
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == 1234 && resultCode == RESULT_OK) {
		ArrayList<String> matches = data
				.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		search.populateEditText(matches);
	}
	super.onActivityResult(requestCode, resultCode, data);
}

public void mic(View v) {
	search.micClick(this);
}
```

More on implementation:
```
search = (SearchBox) findViewById(R.id.searchbox);
for(int x = 0; x < 10; x++){
	SearchResult option = new SearchResult("Result " + Integer.toString(x), getResources().getDrawable(R.drawable.ic_history));
	search.addSearchable(option);
}		
search.setLogoText("My App");
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
			
});
```

## SearchResult
This is a class that holds two parameters - Title and icon
The title is displayed as a suggested result and will be used for searching, the icon is displayed to the left of the title in the suggestions (eg. a history icon)

## All usage methods
setMenuListener(MenuListener listener) - Sets the menu listener (see above)<br />
setSearchListener(SearchListener listener) - Sets the search listener (see above)<br />
toggleSearch() - Forces the toggling of the search bar<br />
micClick(Activity activity) - Used internally, but could be run by other code - Note that if the search box is open with the clear cross as an option, this will clear the search as they are the same button<br />
setMaxLength(int length) - Set the max length of the search EditText (untested)<br />
setLogoText() - Sets the logo text for when the search box is not in use (see above)<br />
startVoiceRecognitionActivity(Activity a) - Used internally, when used by other code it would force the start of voice recognition, use with caution<br />
populateEditText(ArrayList<String> matches) - Used internally for a result from the voice recognition. Not really much use otherwise<br />
setSearchString(String text) - Sets the search string<br />
String getSearchText() - Gets the search term<br />
clearResults() - Clears all the results & refereshes them<br />
int getNumberOfResults() - Returns the number of results<br />
addSearchable(SearchResult result) - Add a suggestion (see above)<br />
removeSuggestion(SearchResult result) - Remove a suggestion<br />
clearSearchable() - Clear all the searchable items<br />
updateResults() - Refreshes the results (run after adding, removing or clearing searchables)<br />
ArrayList<SearchResult> getSearchables() - Returns an ArrayList of all the searchable SearchResults

## Notes
This library's methods are subject to change, don't release an app with this in just yet unless you don't mind changing names when it updates

## The Future
I hope to implement the reveal animation for showing it post-hiding a toolbar from a search button press, like the Play Store appears to do<br />
If there is sufficient demand, I may also backport to Android 2.1+

## Licence
Copyright 2015 Kieron Quinn<br />
<br />
Licensed under the Apache License, Version 2.0 (the "License");<br />
you may not use this file except in compliance with the License.<br />
You may obtain a copy of the License at<br />
<br />
   http://www.apache.org/licenses/LICENSE-2.0<br />
<br />
Unless required by applicable law or agreed to in writing, software<br />
distributed under the License is distributed on an "AS IS" BASIS,<br />
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br />
See the License for the specific language governing permissions and<br />
limitations under the License.