# Android Persistent Search Library

A library that implements the persistent search bar seen on apps such as Google Now, Google Maps and Google Play

![GIF of its use](https://raw.githubusercontent.com/Quinny898/PersistentSearch/master/resources/search.gif)

## Dependencies
material-menu: https://github.com/balysv/material-menu

## Usage

Import it as a library project 

(Method 1. Click "Import Module" from the File and Select the Persistent search folder you downloaded and click ok. Now right click the app directory in project settings and select "Open Module Settings" there click app from left menu and select dependencies from the top right corner now add persistent search option shown in the window, click ok and syn the gradle.)

In your layout:
```
<com.quinny898.library.persistentsearch.SearchBox
        android:layout_width="wrap_content"
		android:layout_height="wrap_content"
        android:id="@+id/searchbox"
        />
```
Please include this after any elements you wish to be hidden by it in a releativelayout.

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
	
	@Override
	public void onSearchCleared() {
				
	}
			
});
```

##Showing from a MenuItem
```
search.revealFromMenuItem(R.id.action_search, this);
```
Note that when a search occurs, the box closes. You should react to this in onSearch, maybe set your toolbar title?

## SearchResult
This is a class that holds two parameters - Title and icon<br />
The title is displayed as a suggested result and will be used for searching, the icon is displayed to the left of the title in the suggestions (eg. a history icon)<br />
You can make a SearchResult as follows<br />
```
new SearchResult("Title", getResources().getDrawable(R.drawable.icon));
```

## All usage methods
See here for the documentation: http://quinny898.co.uk/PersistentSearch/

## The Future
If there is sufficient demand, I may backport to Android 2.1+

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
