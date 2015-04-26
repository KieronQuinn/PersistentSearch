# Android Persistent Search Library

A library that implements the persistent search bar seen on apps such as Google Now, Google Maps and Google Play

![GIF of its use](https://raw.githubusercontent.com/Quinny898/PersistentSearch/master/resources/search.gif)


## Usage

Android Studio:
Add the Sonatype respository if you have not already:
```
maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }
```
Import it as a dependency:
```
compile 'com.quinny898.library.persistentsearch:library:1.0.0-SNAPSHOT'
```

Eclipse:
Import it as a library project

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

In your onCreate/onCreateView (activity or fragment):
```
search.enableVoiceRecognition(this);
```
And in the same class:
```
@Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (isAdded() && requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == getActivity().RESULT_OK) {
      ArrayList<String> matches = data
          .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      search.populateEditText(matches);
    }
    super.onActivityResult(requestCode, resultCode, data);
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